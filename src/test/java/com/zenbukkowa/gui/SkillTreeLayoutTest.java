package com.zenbukkowa.gui;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SkillTreeLayoutTest {

    @Test
    void allNodesWithinGrid() {
        for (SkillTreeLayout.Node node : SkillTreeLayout.nodes()) {
            assertTrue(node.row() >= 0 && node.row() < SkillTreeLayout.GRID_ROWS,
                    "Node " + node.skill() + " row out of bounds: " + node.row());
            assertTrue(node.col() >= 0 && node.col() < SkillTreeLayout.GRID_COLS,
                    "Node " + node.skill() + " col out of bounds: " + node.col());
        }
    }

    @Test
    void noOverlappingNodes() {
        Set<String> occupied = new HashSet<>();
        for (SkillTreeLayout.Node node : SkillTreeLayout.nodes()) {
            String key = node.row() + "," + node.col();
            assertFalse(occupied.contains(key), "Overlapping node at " + key);
            occupied.add(key);
        }
    }

    @Test
    void allConnectionsWithinGrid() {
        for (SkillTreeLayout.Connection conn : SkillTreeLayout.connections()) {
            assertTrue(conn.row() >= 0 && conn.row() < SkillTreeLayout.GRID_ROWS,
                    "Connection row out of bounds: " + conn.row());
            assertTrue(conn.col() >= 0 && conn.col() < SkillTreeLayout.GRID_COLS,
                    "Connection col out of bounds: " + conn.col());
        }
    }

    @Test
    void maxScrollIsNonNegative() {
        assertTrue(SkillTreeLayout.MAX_SCROLL >= 0);
    }

    @Test
    void terraRootIsAtBottom() {
        SkillTreeLayout.Node areaRadius = SkillTreeLayout.findNode(com.zenbukkowa.domain.SkillType.AREA_RADIUS);
        assertNotNull(areaRadius);
        assertEquals(SkillTreeLayout.GRID_ROWS - 1, areaRadius.row());
    }
}
