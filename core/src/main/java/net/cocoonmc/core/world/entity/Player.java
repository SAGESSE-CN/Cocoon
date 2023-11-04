package net.cocoonmc.core.world.entity;

import net.cocoonmc.Cocoon;
import net.cocoonmc.core.Direction;
import net.cocoonmc.core.inventory.Menu;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.network.Component;
import net.cocoonmc.core.utils.ObjectHelper;
import net.cocoonmc.core.world.InteractionHand;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.runtime.impl.ConstantKeys;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;

import java.lang.reflect.Method;

public class Player extends LivingEntity {

    private final org.bukkit.entity.Player player;

    public Player(org.bukkit.entity.Player player) {
        super(player);
        this.player = player;
    }

    public static Player of(org.bukkit.entity.Player player) {
        return Cocoon.API.CACHE.computeIfAbsent(player, ConstantKeys.PLAYER_KEY, Player::new);
    }

    public void addChannel(String channel) {
        try {
            Class<? extends CommandSender> senderClass = player.getClass();
            Method addChannel = senderClass.getDeclaredMethod("addChannel", String.class);
            addChannel.setAccessible(true);
            addChannel.invoke(player, channel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeChannel(String channel) {

    }

    public void sendMessage(String message) {
        player.sendMessage(message);
    }

    public void sendMessage(Component message) {
        player.spigot().sendMessage(message.getContents());
    }

    public void setItem(InteractionHand hand, ItemStack itemStack) {

    }

    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {
        player.getInventory().setItem(equipmentSlot, itemStack.asBukkit());
    }

    public ItemStack getItemBySlot(EquipmentSlot equipmentSlot) {
        return ItemStack.of(player.getInventory().getItem(equipmentSlot));
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
        return player.getGameMode();
    }

    public String getDisplayName() {
        return player.getDisplayName();
    }

    public Direction getDirection() {
        return Direction.by(player.getFacing());
    }

    public Menu getActivedMenu() {
        return Cocoon.API.MENU.getActivedMenu(this);
    }

    public Level getLevel() {
        return Level.of(player.getWorld());
    }

    public boolean isSecondaryUseActive() {
        return player.isSneaking();
    }

    public Inventory getInventory() {
        return player.getInventory();
    }

    @Override
    public org.bukkit.entity.Player asBukkit() {
        return player;
    }

    @Override
    public String toString() {
        return ObjectHelper.makeDescription(this, "uuid", getUUID());
    }
}
