package net.cocoonmc.core.world.entity;

import net.cocoonmc.Cocoon;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.Direction;
import net.cocoonmc.core.inventory.Menu;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.network.Component;
import net.cocoonmc.core.utils.ObjectHelper;
import net.cocoonmc.core.world.InteractionHand;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.runtime.impl.ConstantKeys;
import net.cocoonmc.runtime.impl.LevelData;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;

import java.lang.reflect.Method;

@SuppressWarnings("unused")
public class Player extends LivingEntity {

    private org.bukkit.entity.Player delegate;

    public Player(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public static Player of(org.bukkit.entity.Player player) {
        return Cocoon.API.CACHE.computeIfAbsent(player, ConstantKeys.PLAYER_KEY, it -> {
            Level level = Level.of(it.getWorld());
            Player entity1 = EntityTypes.PLAYER.create(level, BlockPos.ZERO, null);
            entity1.setDelegate(it);
            LevelData.loadEntityTag(entity1);
            return entity1;
        });
    }

    public void addChannel(String channel) {
        try {
            Class<? extends CommandSender> senderClass = delegate.getClass();
            Method addChannel = senderClass.getDeclaredMethod("addChannel", String.class);
            addChannel.setAccessible(true);
            addChannel.invoke(delegate, channel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeChannel(String channel) {

    }

    public void sendMessage(String message) {
        delegate.sendMessage(message);
    }

    public void sendMessage(Component message) {
        delegate.spigot().sendMessage(message.getContents());
    }

    public void setItem(InteractionHand hand, ItemStack itemStack) {

    }

    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {
        delegate.getInventory().setItem(equipmentSlot, itemStack.asBukkit());
    }

    public ItemStack getItemBySlot(EquipmentSlot equipmentSlot) {
        return ItemStack.of(delegate.getInventory().getItem(equipmentSlot));
    }

    public boolean hasItemInSlot(EquipmentSlot equipmentSlot) {
        return !getItemBySlot(equipmentSlot).isEmpty();
    }

    public ItemStack getItemInHand(InteractionHand interactionHand) {
        if (interactionHand == InteractionHand.MAIN_HAND) {
            return getItemBySlot(EquipmentSlot.HAND);
        }
        if (interactionHand == InteractionHand.OFF_HAND) {
            return getItemBySlot(EquipmentSlot.OFF_HAND);
        }
        throw new IllegalArgumentException("Invalid hand " + interactionHand);
    }

    public void setItemInHand(InteractionHand interactionHand, ItemStack itemStack) {
        if (interactionHand == InteractionHand.MAIN_HAND) {
            setItemSlot(EquipmentSlot.HAND, itemStack);
        } else if (interactionHand == InteractionHand.OFF_HAND) {
            setItemSlot(EquipmentSlot.OFF_HAND, itemStack);
        } else {
            throw new IllegalArgumentException("Invalid hand " + interactionHand);
        }
    }

    public ItemStack getMainHandItem() {
        return getItemInHand(InteractionHand.MAIN_HAND);
    }

    public ItemStack getOffhandItem() {
        return getItemInHand(InteractionHand.OFF_HAND);
    }


    public GameMode getGameMode() {
        return delegate.getGameMode();
    }

    public String getDisplayName() {
        return delegate.getDisplayName();
    }

    public Direction getDirection() {
        return Direction.by(delegate.getFacing());
    }

    public Menu getActivedMenu() {
        return Cocoon.API.MENU.getActivedMenu(this);
    }

    @Override
    public Level getLevel() {
        return Level.of(delegate.getWorld());
    }

    public boolean isSneaking() {
        return delegate.isSneaking();
    }

    public boolean isSecondaryUseActive() {
        return delegate.isSneaking();
    }

    public boolean isCreative() {
        return delegate.getGameMode() == GameMode.CREATIVE;
    }

    public boolean isSpectator() {
        return delegate.getGameMode() == GameMode.SPECTATOR;
    }

    public Inventory getInventory() {
        return delegate.getInventory();
    }

    public Inventory getEnderInventory() {
        return delegate.getEnderChest();
    }

    @Override
    public void setDelegate(org.bukkit.entity.Entity delegate) {
        super.setDelegate(delegate);
        if (delegate instanceof org.bukkit.entity.Player) {
            this.delegate = (org.bukkit.entity.Player) delegate;
        }
    }

    @Override
    public org.bukkit.entity.Player asBukkit() {
        return delegate;
    }

    @Override
    public String toString() {
        return ObjectHelper.makeDescription(this, "uuid", getUUID());
    }
}
