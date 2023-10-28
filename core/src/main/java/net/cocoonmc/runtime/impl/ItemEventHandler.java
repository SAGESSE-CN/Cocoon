package net.cocoonmc.runtime.impl;

import com.google.common.collect.Iterables;
import net.cocoonmc.Cocoon;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.Direction;
import net.cocoonmc.core.block.BlockEntity;
import net.cocoonmc.core.item.Item;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.item.context.BlockHitResult;
import net.cocoonmc.core.item.context.UseOnContext;
import net.cocoonmc.core.math.Vector3f;
import net.cocoonmc.core.world.InteractionHand;
import net.cocoonmc.core.world.InteractionResult;
import net.cocoonmc.core.world.InteractionResultHolder;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.LoomInventory;

import java.util.Arrays;

public class ItemEventHandler implements Listener {

    @EventHandler
    public void onInteractBlock(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = ItemStack.of(event.getItem());
        Item item = itemStack.getItem();
        InteractionHand hand = InteractionHand.by(event.getHand());
        if (event.hasBlock() && event.getClickedBlock() != null) {
            World world = player.getWorld();
            BlockPos blockPos = BlockPos.of(event.getClickedBlock().getLocation());
            // for attack(left-click) will pass to vanilla.
            if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                // for we custom block, check it and cancel event.
                BlockEntity blockEntity = Cocoon.API.BLOCK.getBlockEntity(world, blockPos);
                if (blockEntity != null && blockEntity.getBlockState().attack(world, blockPos, player) != InteractionResult.PASS) {
                    event.setCancelled(true);
                    return;
                }
                return;
            }
            // order: useOnFirst -> block.use -> useOn
            Vector3f loc = Vector3f.of(event.getClickedBlock().getLocation());
            Direction dir = Direction.of(event.getBlockFace());
            BlockHitResult hitResult = BlockHitResult.hitting(loc, dir, blockPos, false);
            UseOnContext context = new UseOnContext(world, player, hand, itemStack, hitResult);
            if (event.useItemInHand() != Event.Result.DENY && item.useOnFirst(itemStack, context) != InteractionResult.PASS) {
                event.setCancelled(true);
                return;
            }
            // block.use
            ItemStack mainHandItem = ItemStack.of(player.getInventory().getItemInMainHand());
            ItemStack offHandItem = ItemStack.of(player.getInventory().getItemInOffHand());
            boolean flag = !mainHandItem.isEmpty() || !offHandItem.isEmpty();
            boolean flag1 = player.isSneaking() && flag && (!mainHandItem.doesSneakBypassUse(player, world, blockPos) || !offHandItem.doesSneakBypassUse(player, world, blockPos));
            if (event.useItemInHand() == Event.Result.ALLOW || (event.useItemInHand() != Event.Result.DENY && !flag1)) {
                // for we custom block, check it and cancel event.
                BlockEntity blockEntity = Cocoon.API.BLOCK.getBlockEntity(world, blockPos);
                if (blockEntity != null && blockEntity.getBlockState().use(world, blockPos, player, hand) != InteractionResult.PASS) {
                    event.setCancelled(true);
                    return;
                }
                // for vanilla interactive block, we pass it directly.
                if (event.getClickedBlock().getType().isInteractable()) {
                    return;
                }
            }
            // use item on context.
            if (event.useItemInHand() != Event.Result.DENY && item.useOn(context) != InteractionResult.PASS) {
                event.setCancelled(true);
                return;
            }
        } else {
            // order: use
            InteractionResultHolder<ItemStack> result = item.use(itemStack, player, hand);
            if (result.getObject() != itemStack) {
                player.getInventory().setItem(hand.asBukkit(), result.getObject().asBukkit());
            }
            if (result.getResult() != InteractionResult.PASS) {
                event.setCancelled(true);
            }
        }
        // safe: we need to cancel all unknown events.
        if (item.asMaterial() == null) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        if (!(entity instanceof LivingEntity)) {
            return;
        }
        LivingEntity livingEntity = (LivingEntity) entity;
        InteractionHand hand = InteractionHand.by(event.getHand());
        ItemStack itemStack = ItemStack.of(event.getPlayer().getInventory().getItem(event.getHand()));
        if (itemStack.getItem().interactLivingEntity(itemStack, player, livingEntity, hand) != InteractionResult.PASS) {
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (Arrays.stream(event.getInventory().getMatrix()).anyMatch(Cocoon.API.ITEM::isRedirectedItem)) {
            event.getInventory().setResult(ItemStack.EMPTY.asBukkit());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory() instanceof LoomInventory) {
            if (Cocoon.API.ITEM.isRedirectedItem(event.getCurrentItem())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onFurnaceBurn(FurnaceBurnEvent event) {
        if (Cocoon.API.ITEM.isRedirectedItem(event.getFuel())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFurnaceSmelt(FurnaceSmeltEvent event) {
        if (Cocoon.API.ITEM.isRedirectedItem(event.getSource())) {
            event.setCancelled(true);
        }
    }
}
