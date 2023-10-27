package net.cocoonmc.impl;

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
import net.cocoonmc.utils.BukkitUtils;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ItemEventHandler implements Listener {

    @EventHandler
    public void onInteractBlock(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = ItemStack.of(event.getItem());
        Item item = itemStack.getItem();
        InteractionHand hand = InteractionHand.of(event.getHand());
        if (event.hasBlock() && event.getClickedBlock() != null) {
            World world = player.getWorld();
            BlockPos blockPos = BlockPos.of(event.getClickedBlock().getLocation());
            // for attack(left-click) will pass to vanilla.
            if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                // for we custom block, check it and cancel event.
                BlockEntity blockEntity = BukkitUtils.getBlockEntity(world, blockPos);
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
            ItemStack mainHandItem = BukkitUtils.getItemInHand(player, InteractionHand.MAIN_HAND);
            ItemStack offHandItem = BukkitUtils.getItemInHand(player, InteractionHand.OFF_HAND);
            boolean flag = !mainHandItem.isEmpty() || !offHandItem.isEmpty();
            boolean flag1 = player.isSneaking() && flag && (!mainHandItem.doesSneakBypassUse(player, world, blockPos) || !offHandItem.doesSneakBypassUse(player, world, blockPos));
            if (event.useItemInHand() == Event.Result.ALLOW || (event.useItemInHand() != Event.Result.DENY && !flag1)) {
                // for we custom block, check it and cancel event.
                BlockEntity blockEntity = BukkitUtils.getBlockEntity(world, blockPos);
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
                BukkitUtils.setItemInHand(player, hand, result.getObject());
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
        InteractionHand hand = InteractionHand.of(event.getHand());
        ItemStack itemStack = BukkitUtils.getItemInHand(player, hand);
        if (itemStack.getItem().interactLivingEntity(itemStack, player, livingEntity, hand) != InteractionResult.PASS) {
            event.setCancelled(true);
        }
    }


//    @EventHandler
//    public void onCraft(CraftItemEvent event) {
//        for (org.bukkit.inventory.ItemStack itemStack : event.getInventory().getMatrix()) {
//            if (itemStack != null) {
//                if (NBTEditor.contains(itemStack, "__redirected_id__")) {
//                    event.getInventory().setResult(new org.bukkit.inventory.ItemStack(Material.AIR));
//                }
//            }
//        }
//    }
//
//    @EventHandler(priority = EventPriority.HIGHEST)
//    public void onInventoryClick(InventoryClickEvent event) {
//        if (event.getInventory() instanceof LoomInventory) {
//            org.bukkit.inventory.ItemStack currentItem = event.getCurrentItem();
//            if (NBTEditor.contains(currentItem, "__redirected_id__")) {
//                event.setCancelled(true);
//            }
//        }
//    }
//
//    @EventHandler
//    public void onFurnaceBurn(FurnaceBurnEvent event) {
//        if (NBTEditor.contains(event.getFuel(), "__redirected_id__")) {
//            event.setCancelled(true);
//        }
//    }
//
//    @EventHandler
//    public void onFurnaceSmelt(FurnaceSmeltEvent event) {
//        if (NBTEditor.contains(event.getSource(), "__redirected_id__")) {
//            event.setCancelled(true);
//        }
//    }
}
