package com.zenbukkowa.breaker;

import com.zenbukkowa.persistence.PlayerPlacedBlockDao;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

public class PistonListener implements Listener {
    private final PlayerPlacedBlockDao playerPlacedBlockDao;
    private static final String PISTON_UUID = "00000000-0000-0000-0000-000000000000";

    public PistonListener(PlayerPlacedBlockDao playerPlacedBlockDao) {
        this.playerPlacedBlockDao = playerPlacedBlockDao;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPistonExtend(BlockPistonExtendEvent event) {
        BlockFace direction = event.getDirection();
        for (Block block : event.getBlocks()) {
            Block target = block.getRelative(direction);
            record(target);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPistonRetract(BlockPistonRetractEvent event) {
        BlockFace direction = event.getDirection();
        for (Block block : event.getBlocks()) {
            // For retract, if it's sticky, the block moves towards the piston.
            // direction is the direction the piston retracts (towards the piston head).
            // Actually direction is where the piston was EXTENDING.
            // Retract direction is opposite to direction? 
            // Bukkit docs: getDirection() returns the direction the piston is facing.
            // So extend moves blocks in direction. Retract moves blocks in opposite direction?
            // No, retract event direction is also the direction the piston is facing.
            // Sticky piston pulls block from (pistonHead + direction) to (pistonHead).
            Block target = block.getRelative(direction.getOppositeFace());
            record(target);
        }
    }

    private void record(Block block) {
        playerPlacedBlockDao.record(
                block.getWorld().getName(),
                block.getX(),
                block.getY(),
                block.getZ(),
                PISTON_UUID
        );
    }
}
