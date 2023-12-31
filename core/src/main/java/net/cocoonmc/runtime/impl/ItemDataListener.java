package net.cocoonmc.runtime.impl;

import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.Direction;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.item.Item;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.item.context.BlockHitResult;
import net.cocoonmc.core.item.context.UseOnContext;
import net.cocoonmc.core.math.Vector3d;
import net.cocoonmc.core.utils.BukkitHelper;
import net.cocoonmc.core.world.InteractionHand;
import net.cocoonmc.core.world.InteractionResult;
import net.cocoonmc.core.world.InteractionResultHolder;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.core.world.entity.Entity;
import net.cocoonmc.core.world.entity.LivingEntity;
import net.cocoonmc.core.world.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.LoomInventory;

import java.util.Arrays;

public class ItemDataListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onInteractBlock(PlayerInteractEvent event) {
        Player player = Player.of(event.getPlayer());
        Level level = player.getLevel();
        ItemStack itemStack = ItemStack.of(event.getItem());
        Item item = itemStack.getItem();
        InteractionHand hand = InteractionHand.by(event.getHand());
        if (event.hasBlock() && event.getClickedBlock() != null) {
            BlockPos blockPos = BlockPos.of(event.getClickedBlock());
            // for attack(left-click) will pass to vanilla.
            if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                // for we custom block, check it and cancel event.
                BlockState blockState = level.getBlockState(blockPos);
                if (blockState != null && blockState.attack(level, blockPos, player) != InteractionResult.PASS) {
                    event.setCancelled(true);
                    return;
                }
                return;
            }
            // order: useOnFirst -> block.use -> useOn
            Vector3d loc = Vector3d.of(BukkitHelper.getClickedLocation(event));
            Direction dir = Direction.by(event.getBlockFace());
            BlockHitResult hitResult = BlockHitResult.hitting(loc, dir, blockPos, false);
            UseOnContext context = new UseOnContext(level, player, hand, itemStack, hitResult);
            if (event.useItemInHand() != Event.Result.DENY && item.useOnFirst(itemStack, context) != InteractionResult.PASS) {
                event.setCancelled(true);
                return;
            }
            // block.use
            ItemStack mainHandItem = player.getMainHandItem();
            ItemStack offHandItem = player.getOffhandItem();
            boolean flag = !mainHandItem.isEmpty() || !offHandItem.isEmpty();
            boolean flag1 = player.isSecondaryUseActive() && flag && (!mainHandItem.doesSneakBypassUse(player, level, blockPos) || !offHandItem.doesSneakBypassUse(player, level, blockPos));
            if (event.useItemInHand() == Event.Result.ALLOW || (event.useItemInHand() != Event.Result.DENY && !flag1)) {
                // for we custom block, check it and cancel event.
                BlockState blockState = level.getBlockState(blockPos);
                if (blockState != null && blockState.use(level, blockPos, player, hand) != InteractionResult.PASS) {
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
                player.setItemInHand(hand, result.getObject());
            }
            if (result.getResult() != InteractionResult.PASS) {
                event.setCancelled(true);
            }
        }
        // safe: we need to cancel all unknown events.
        if (item.asBukkit() == null) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof org.bukkit.entity.LivingEntity)) {
            return;
        }
        Player player = Player.of(event.getPlayer());
        LivingEntity livingEntity = LivingEntity.of((org.bukkit.entity.LivingEntity) event.getRightClicked());
        InteractionHand hand = InteractionHand.by(event.getHand());
        ItemStack itemStack = ItemStack.of(event.getPlayer().getInventory().getItem(event.getHand()));
        if (itemStack.getItem().interactLivingEntity(itemStack, player, livingEntity, hand) != InteractionResult.PASS) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInteractEntityAt(PlayerInteractAtEntityEvent event) {
        onInteractEntity(event);
    }

    @EventHandler(ignoreCancelled = true)
    public void onAttackEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof org.bukkit.entity.Player)) {
            return;
        }
        Entity entity = Entity.of(event.getEntity());
        Player player = Player.of((org.bukkit.entity.Player) event.getDamager());
        ItemStack itemStack = player.getMainHandItem();
        if (itemStack.getItem().attackLivingEntity(itemStack, player, entity) != InteractionResult.PASS) {
            event.setCancelled(true);
        }
    }

//    @EventHandler(ignoreCancelled = true)
//    public void onAttackEntityBy(EntityDamageByBlockEvent event) {
//    }

    @EventHandler(ignoreCancelled = true)
    public void onCraft(CraftItemEvent event) {
        if (Arrays.stream(event.getInventory().getMatrix()).anyMatch(BukkitHelper::isRedirectedItem)) {
            event.getInventory().setResult(ItemStack.EMPTY.asBukkit());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory() instanceof LoomInventory) {
            if (BukkitHelper.isRedirectedItem(event.getCurrentItem())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onFurnaceBurn(FurnaceBurnEvent event) {
        if (BukkitHelper.isRedirectedItem(event.getFuel())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onFurnaceSmelt(FurnaceSmeltEvent event) {
        if (BukkitHelper.isRedirectedItem(event.getSource())) {
            event.setCancelled(true);
        }
    }


    @EventHandler(ignoreCancelled = true)
    public void onPistonExtend(BlockPistonExtendEvent event) {
        if (event.getBlocks().stream().anyMatch(BukkitHelper::isRedirectedBlock)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPistonRetract(BlockPistonRetractEvent event) {
        if (event.getBlocks().stream().anyMatch(BukkitHelper::isRedirectedBlock)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onLiquidDispense(BlockFromToEvent event) {
        if (BukkitHelper.isRedirectedBlock(event.getToBlock())) {
            event.setCancelled(true);
        }
    }
}
