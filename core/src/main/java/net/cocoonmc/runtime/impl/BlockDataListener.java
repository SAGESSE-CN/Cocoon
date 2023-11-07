package net.cocoonmc.runtime.impl;

import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.block.Blocks;
import net.cocoonmc.core.utils.BukkitHelper;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.core.world.chunk.Chunk;
import net.cocoonmc.core.world.entity.Player;
import net.cocoonmc.core.world.loot.LootContext;
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

import java.util.List;

public class BlockDataListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        updateBlockDropItemsIfNeeded(event);
        removeBlockIfNeeded(event.getBlock());
        updateBlockNeighboursIfNeeded(event.getBlock(), 1);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        removeBlockIfNeeded(event.getBlock());
        setBlockIfNeeded(event.getBlock());
        updateBlockNeighboursIfNeeded(event.getBlock(), 2);
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
        updateBlockNeighboursIfNeeded(event.getBlock(), 0);
        Logs.debug("{}", event.getBlock());
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
        BlockPlaceTask task = BlockPlaceTask.last();
        if (task != null && block.getType().equals(task.getDelegate().asBukkit())) {
            Chunk chunk = Chunk.of(block);
            BlockPos blockPos = BlockPos.of(block);
            LevelData.beginUpdates();
            chunk.setBlock(blockPos, task.getBlockState(), task.getEntityTag());
            task.getBlock().setPlacedBy(chunk.getLevel(), blockPos, task.getBlockState(), task.getContext());
            LevelData.endUpdates();
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

    private void updateBlockNeighboursIfNeeded(org.bukkit.block.Block block, int flags) {
        Level level = Level.of(block);
        BlockPos blockPos = BlockPos.of(block);
        level.updateNeighborsAt(blockPos, Blocks.AIR);
    }

    private void updateBlockDropItemsIfNeeded(BlockBreakEvent event) {
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
        LootContext context = new LootContext();
        context.level = level;
        context.player = player;
        context.blockEntity = level.getBlockEntity(blockPos);
        BukkitHelper.dropItems(blockState.getDrops(context), event.getBlock());
    }
}


