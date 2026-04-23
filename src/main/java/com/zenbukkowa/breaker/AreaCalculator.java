package com.zenbukkowa.breaker;

import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class AreaCalculator {

    public List<Block> calculate(Block center, int radiusValue, int depthValue) {
        return calculate(center, radiusValue, depthValue, 0, 0);
    }

    public List<Block> calculate(Block center, int radiusValue, int depthValue, int bonusRadius, int bonusDepth) {
        int radius = Math.max(0, (radiusValue - 1) / 2 + bonusRadius);
        int depth = Math.max(0, (depthValue - 1) / 2 + bonusDepth);
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

    public List<Block> deduplicate(List<Block> blocks) {
        Set<String> seen = new LinkedHashSet<>();
        List<Block> unique = new ArrayList<>();
        for (Block b : blocks) {
            String key = b.getWorld().getName() + ":" + b.getX() + ":" + b.getY() + ":" + b.getZ();
            if (seen.add(key)) {
                unique.add(b);
            }
        }
        return unique;
    }
}
