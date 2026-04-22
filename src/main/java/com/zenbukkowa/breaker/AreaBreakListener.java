package com.zenbukkowa.breaker;

import com.zenbukkowa.domain.BreakService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class AreaBreakListener implements Listener {
    private final BreakService breakService;

    public AreaBreakListener(BreakService breakService) {
        this.breakService = breakService;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().getInventory().getHeldItemSlot() == 8) {
            return;
        }
        breakService.onPlayerBreak(event.getPlayer(), event.getBlock());
    }
}
