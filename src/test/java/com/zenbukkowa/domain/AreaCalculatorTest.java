package com.zenbukkowa.domain;

import com.zenbukkowa.breaker.AreaCalculator;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AreaCalculatorTest {

    @Test
    void tier1ReturnsSingleBlock() {
        AreaCalculator calc = new AreaCalculator();
        Block center = mockBlock(0, 64, 0);
        List<Block> result = calc.calculate(center, 1, 1);
        assertEquals(1, result.size());
    }

    @Test
    void tier3ReturnsNineBlocks() {
        AreaCalculator calc = new AreaCalculator();
        Block center = mockBlock(0, 64, 0);
        List<Block> result = calc.calculate(center, 3, 1);
        assertEquals(9, result.size());
    }

    @Test
    void tier5Returns25Blocks() {
        AreaCalculator calc = new AreaCalculator();
        Block center = mockBlock(0, 64, 0);
        List<Block> result = calc.calculate(center, 5, 1);
        assertEquals(25, result.size());
    }

    @Test
    void tier9x9x9Returns729Blocks() {
        AreaCalculator calc = new AreaCalculator();
        Block center = mockBlock(0, 64, 0);
        List<Block> result = calc.calculate(center, 9, 9);
        assertEquals(729, result.size());
    }

    @Test
    void deduplicateRemovesDuplicates() {
        AreaCalculator calc = new AreaCalculator();
        Block center = mockBlock(0, 64, 0);
        List<Block> result = calc.calculate(center, 3, 3);
        List<Block> deduped = calc.deduplicate(result);
        assertEquals(result.size(), deduped.size());
        // Force a duplicate
        result.add(center);
        deduped = calc.deduplicate(result);
        assertEquals(result.size() - 1, deduped.size());
    }

    private Block mockBlock(int x, int y, int z) {
        Block block = mock(Block.class);
        World world = mock(World.class);
        when(world.getName()).thenReturn("world");
        when(world.getMinHeight()).thenReturn(-64);
        when(world.getMaxHeight()).thenReturn(320);
        when(block.getWorld()).thenReturn(world);
        when(block.getX()).thenReturn(x);
        when(block.getY()).thenReturn(y);
        when(block.getZ()).thenReturn(z);
        for (int dx = -10; dx <= 10; dx++) {
            for (int dy = -10; dy <= 10; dy++) {
                for (int dz = -10; dz <= 10; dz++) {
                    Block b = mock(Block.class);
                    when(b.getWorld()).thenReturn(world);
                    when(b.getX()).thenReturn(x + dx);
                    when(b.getY()).thenReturn(y + dy);
                    when(b.getZ()).thenReturn(z + dz);
                    when(world.getBlockAt(x + dx, y + dy, z + dz)).thenReturn(b);
                }
            }
        }
        when(world.getBlockAt(x, y, z)).thenReturn(block);
        return block;
    }
}
