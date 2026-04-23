package com.zenbukkowa.breaker;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {
    private final PlacedBlockCache placedBlockCache;

    public BlockPlaceListener(PlacedBlockCache placedBlockCache) {
        this.placedBlockCache = placedBlockCache;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        placedBlockCache.record(
                event.getBlock().getWorld().getName(),
                event.getBlock().getX(),
                event.getBlock().getY(),
                event.getBlock().getZ(),
                event.getPlayer().getUniqueId().toString()
        );
    }
}
