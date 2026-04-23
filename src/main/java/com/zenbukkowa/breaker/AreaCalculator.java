package com.zenbukkowa.breaker;

import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class AreaCalculator {

    public List<Block> calculate(Block center, int radiusTier, int depthTier) {
        return calculate(center, radiusTier, depthTier, 0, 0);
    }

    public List<Block> calculate(Block center, int radiusTier, int depthTier, int bonusRadius, int bonusDepth) {
        int radius = (radiusTier - 1) / 2 + bonusRadius;
        int depth = (depthTier - 1) / 2 + bonusDepth;
        List<Block> result = new ArrayList<>();
        World world = center.getWorld();
        int cx = center.getX();
        int cy = center.getY();
        int cz = center.getZ();
        int minY = world.getMinHeight();
        int maxY = world.getMaxHeight();

        result.add(center);

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                for (int dy = -depth; dy <= depth; dy++) {
                    if (dx == 0 && dy == 0 && dz == 0) continue;
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
