package com.zenbukkowa.breaker;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

public class PistonListener implements Listener {
    private final PlacedBlockCache placedBlockCache;

    public PistonListener(PlacedBlockCache placedBlockCache) {
        this.placedBlockCache = placedBlockCache;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPistonExtend(BlockPistonExtendEvent event) {
        BlockFace direction = event.getDirection();
        for (Block block : event.getBlocks()) {
            Block source = block;
            Block target = block.getRelative(direction);
            moveTracking(source, target);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPistonRetract(BlockPistonRetractEvent event) {
        BlockFace direction = event.getDirection();
        for (Block block : event.getBlocks()) {
            Block source = block;
            Block target = block.getRelative(direction.getOppositeFace());
            moveTracking(source, target);
        }
    }

    private void moveTracking(Block source, Block target) {
        String world = source.getWorld().getName();
        boolean wasPlaced = placedBlockCache.isPlayerPlaced(world, source.getX(), source.getY(), source.getZ());
        placedBlockCache.delete(world, source.getX(), source.getY(), source.getZ());
        if (wasPlaced) {
            placedBlockCache.record(world, target.getX(), target.getY(), target.getZ(), "piston_moved");
        } else {
            placedBlockCache.delete(target.getWorld().getName(), target.getX(), target.getY(), target.getZ());
        }
    }
}
