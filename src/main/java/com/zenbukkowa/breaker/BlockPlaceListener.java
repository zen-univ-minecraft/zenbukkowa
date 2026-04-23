package com.zenbukkowa.breaker;

import com.zenbukkowa.persistence.PlayerPlacedBlockDao;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {
    private final PlayerPlacedBlockDao playerPlacedBlockDao;

    public BlockPlaceListener(PlayerPlacedBlockDao playerPlacedBlockDao) {
        this.playerPlacedBlockDao = playerPlacedBlockDao;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        var block = event.getBlock();
        playerPlacedBlockDao.record(
                block.getWorld().getName(),
                block.getX(), block.getY(), block.getZ(),
                event.getPlayer().getUniqueId().toString()
        );
    }
}
