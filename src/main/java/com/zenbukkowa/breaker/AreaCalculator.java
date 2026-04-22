package com.zenbukkowa.breaker;

import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class AreaCalculator {

    public List<Block> calculate(Block center, int radiusTier, int depthTier) {
        int radius = (radiusTier - 1) / 2;
        int depth = (depthTier - 1) / 2;
        List<Block> result = new ArrayList<>();
        World world = center.getWorld();
        int cx = center.getX();
        int cy = center.getY();
        int cz = center.getZ();
        int minY = world.getMinHeight();
        int maxY = world.getMaxHeight();

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                for (int dy = -depth; dy <= depth; dy++) {
                    int y = cy + dy;
                    if (y < minY || y >= maxY) {
                        continue;
                    }
                    result.add(world.getBlockAt(cx + dx, y, cz + dz));
                }
            }
        }
        return result;
    }
}
