package com.zenbukkowa.structure;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class StructureBonusListener implements Listener {
    private final StructureService structureService;

    public StructureBonusListener(StructureService structureService) {
        this.structureService = structureService;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        StructureService.PlayerContext ctx = new StructureService.PlayerContext(
                event.getPlayer().getUniqueId(), event.getPlayer());
        structureService.checkOceanMonument(ctx, event.getBlock());
        structureService.checkWoodlandMansion(ctx, event.getBlock());
    }
}
