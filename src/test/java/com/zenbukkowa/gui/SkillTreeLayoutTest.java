package com.zenbukkowa.gui;

import com.zenbukkowa.domain.SkillType;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
        assertTrue(SkillTreeLayout.MAX_SCROLL_V >= 0);
        assertTrue(SkillTreeLayout.MAX_SCROLL_H >= 0);
        assertEquals(13, SkillTreeLayout.MAX_SCROLL_H);
    }

    @Test
    void terraRootIsAtBottom() {
        SkillTreeLayout.Node areaRadius = SkillTreeLayout.findNode(com.zenbukkowa.domain.SkillType.AREA_RADIUS);
        assertNotNull(areaRadius);
        int maxRow = SkillTreeLayout.nodes().stream().mapToInt(SkillTreeLayout.Node::row).max().orElse(0);
        assertEquals(maxRow, areaRadius.row());
    }

    @Test
    void noConnectionOverlapsUnrelatedNode() {
        Map<String, SkillTreeLayout.Node> nodeMap = new HashMap<>();
        Map<SkillTreeLayout.Node, SkillType> nodeToParent = new HashMap<>();
        for (SkillTreeLayout.Node node : SkillTreeLayout.nodes()) {
            nodeMap.put(node.row() + "," + node.col(), node);
            SkillType p = SkillTreeLayout.parentOf(node.skill());
            if (p != null) nodeToParent.put(node, p);
        }

        for (SkillTreeLayout.Connection conn : SkillTreeLayout.connections()) {
            String key = conn.row() + "," + conn.col();
            SkillTreeLayout.Node nodeAtConn = nodeMap.get(key);
            if (nodeAtConn == null) continue;

            boolean isParentOrChild = false;
            for (Map.Entry<SkillTreeLayout.Node, SkillType> e : nodeToParent.entrySet()) {
                SkillTreeLayout.Node child = e.getKey();
                SkillType parentSkill = e.getValue();
                SkillTreeLayout.Node parent = SkillTreeLayout.findNode(parentSkill);
                if (parent == null) continue;

                boolean connIsBetweenThisPair =
                        (conn.row() > child.row() && conn.row() < parent.row() && conn.col() == parent.col())
                                || (conn.row() == child.row() && conn.col() >= Math.min(parent.col(), child.col()) && conn.col() <= Math.max(parent.col(), child.col()));

                if (connIsBetweenThisPair) {
                    if (nodeAtConn.skill() == child.skill() || nodeAtConn.skill() == parent.skill()) {
                        isParentOrChild = true;
                        break;
                    }
                    var nodeParent = SkillTreeLayout.parentOf(nodeAtConn.skill());
                    if (nodeParent != null && nodeParent == parentSkill) {
                        isParentOrChild = true;
                        break;
                    }
                }
            }

            if (!isParentOrChild) {
                fail("Connection at " + key + " overlaps unrelated node " + nodeAtConn.skill());
            }
        }
    }

    @Test
    void everyNodeHasCorrectParent() {
        for (SkillTreeLayout.Node node : SkillTreeLayout.nodes()) {
            var parent = SkillTreeLayout.parentOf(node.skill());
            if (parent != null) {
                var parentNode = SkillTreeLayout.findNode(parent);
                assertNotNull(parentNode, "Parent node missing for " + node.skill());
                assertTrue(parentNode.row() >= node.row(), "Parent must be at or below child");
            }
        }
    }

    @Test
    void wasdSlotsDoNotOverlapSkillsAtDefaultScroll() {
        int scrollV = 8;
        int scrollH = 0;
        int[] arrowSlots = {47, 48, 49, 50};
        for (int slot : arrowSlots) {
            assertNull(SkillTreeViewport.skillAtSlot(slot, scrollV, scrollH),
                    "Arrow slot " + slot + " overlaps a skill at default scroll");
        }
    }
}
