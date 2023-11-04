package net.cocoonmc.runtime.impl;

import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.block.Blocks;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.utils.BukkitHelper;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.core.world.chunk.Chunk;
import net.cocoonmc.core.world.entity.Player;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.world.StructureGrowEvent;

import java.util.ArrayList;
import java.util.List;

public class BlockDataListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        setBlockDropItemsIfNeeded(event);
        removeBlockIfNeeded(event.getBlock());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        removeBlockIfNeeded(event.getBlock());
        setBlockIfNeeded(event.getBlock());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockExplode(BlockExplodeEvent event) {
        removeBlockIfNeeded(event.blockList());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBurn(BlockBurnEvent event) {
        removeBlockIfNeeded(event.getBlock());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPiston(BlockPistonExtendEvent event) {
        removeBlockIfNeeded(event.getBlocks());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPiston(BlockPistonRetractEvent event) {
        removeBlockIfNeeded(event.getBlocks());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockFade(BlockFadeEvent event) {
        if (event.getBlock().getType() == Material.FIRE) {
            return;
        }
        if (event.getNewState().getType() != event.getBlock().getType()) {
            removeBlockIfNeeded(event.getBlock());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockGrow(StructureGrowEvent event) {
        removeBlockStateIfNeeded(event.getBlocks());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockFertilize(BlockFertilizeEvent event) {
        removeBlockStateIfNeeded(event.getBlocks());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockRedstone(BlockRedstoneEvent event) {
        updateBlockRedstone(event.getBlock(), event.getOldCurrent(), event.getNewCurrent());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        removeBlockIfNeeded(event.blockList());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityChange(EntityChangeBlockEvent event) {
        if (event.getTo() != event.getBlock().getType()) {
            removeBlockIfNeeded(event.getBlock());
        }
    }


    private void setBlockIfNeeded(org.bukkit.block.Block block) {
        ChunkDataPlaceTask task = ChunkDataPlaceTask.last();
        if (task != null && block.getType().equals(task.getWrapper().asBukkit())) {
            Chunk chunk = Chunk.of(block);
            BlockPos blockPos = BlockPos.of(block);
            chunk.setBlock(blockPos, task.getBlockState(), task.getEntityTag());
        }
    }

    private void removeBlockIfNeeded(org.bukkit.block.Block block) {
        Chunk chunk = Chunk.of(block);
        BlockPos blockPos = BlockPos.of(block);
        if (chunk.getBlockState(blockPos) != null) {
            chunk.setBlock(blockPos, Blocks.AIR.defaultBlockState(), null);
        }
    }

    private void removeBlockIfNeeded(List<org.bukkit.block.Block> blocks) {
        blocks.forEach(this::removeBlockIfNeeded);
    }

    private void removeBlockStateIfNeeded(List<org.bukkit.block.BlockState> blocks) {
    }

    private void updateBlockRedstone(org.bukkit.block.Block block, int oldValue, int newValue) {

        Logs.debug("{}, {}, {}", block, oldValue, newValue);

    }

    private void setBlockDropItemsIfNeeded(BlockBreakEvent event) {
        Level level = Level.of(event.getBlock().getWorld());
        BlockPos blockPos = BlockPos.of(event.getBlock());
        BlockState blockState = level.getBlockState(blockPos);
        if (blockState == null) {
            return;
        }
        // if remove event change the block, we need to cancel the event.
        if (!event.isDropItems()) {
            return;
        }
        // we not need cancel this event, but block can't drop items.
        event.setDropItems(false);
        Player player = Player.of(event.getPlayer());
        if (player.getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }
        // replace with we custom items.
        ArrayList<ItemStack> dropItems = new ArrayList<>();
        event.getBlock().getDrops().stream().map(ItemStack::of).forEach(dropItems::add);
        BukkitHelper.replaceDrops(dropItems, blockState.getBlock());
        BukkitHelper.dropItems(dropItems, event.getBlock());
    }
}

