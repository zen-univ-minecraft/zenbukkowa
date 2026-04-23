package com.zenbukkowa.breaker;

import com.zenbukkowa.domain.BreakService;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class AreaBreakListener implements Listener {
    private final BreakService breakService;
    private final ThreadLocal<Boolean> processing = ThreadLocal.withInitial(() -> false);

    public AreaBreakListener(BreakService breakService) {
        this.breakService = breakService;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (processing.get()) {
            return;
        }
        if (event.getPlayer().getInventory().getHeldItemSlot() == 8) {
            return;
        }
        if (isImmatureCrop(event.getBlock().getBlockData())) {
            return;
        }
        processing.set(true);
        try {
            breakService.onPlayerBreak(event.getPlayer(), event.getBlock());
        } finally {
            processing.set(false);
        }
    }

    private boolean isImmatureCrop(org.bukkit.block.data.BlockData data) {
        if (!(data instanceof Ageable ageable)) return false;
        return ageable.getAge() < ageable.getMaximumAge();
    }
}
