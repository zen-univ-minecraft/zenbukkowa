package com.zenbukkowa.gui;

import com.zenbukkowa.domain.SkillType;

import java.util.ArrayList;
import java.util.List;

public class SkillTreeLayout {

    public static final int GRID_ROWS = 12;
    public static final int GRID_COLS = 9;
    public static final int VIEWPORT_ROWS = 5;
    public static final int MAX_SCROLL = GRID_ROWS - VIEWPORT_ROWS;

    public record Node(SkillType skill, int row, int col) {}
    public record Connection(int row, int col) {}

    private static final List<Node> NODES = List.of(
            // TERRA trunk (col 4)
            new Node(SkillType.AREA_RADIUS, 11, 4),
            new Node(SkillType.AREA_DEPTH, 9, 4),
            new Node(SkillType.PILLAR_BREAK, 7, 4),
            new Node(SkillType.GRAVITY_WELL, 5, 4),
            new Node(SkillType.TERRA_BLESSING, 3, 4),
            // MINERAL branch (col 2)
            new Node(SkillType.HASTE_AURA, 10, 2),
            new Node(SkillType.FORTUNE_TOUCH, 8, 2),
            new Node(SkillType.VEIN_MINER, 6, 2),
            new Node(SkillType.MAGNET, 4, 2),
            new Node(SkillType.CRYSTAL_VISION, 2, 2),
            // ORGANIC branch (col 1)
            new Node(SkillType.LEAF_CONSUME, 8, 1),
            new Node(SkillType.ROOT_RAZE, 6, 1),
            new Node(SkillType.SAPLING_REPLANT, 4, 1),
            new Node(SkillType.BONEMEAL_AURA, 2, 1),
            new Node(SkillType.NATURE_TOUCH, 0, 1),
            // AQUATIC branch (col 6)
            new Node(SkillType.TIDE_BREAKER, 10, 6),
            new Node(SkillType.SALVAGE, 8, 6),
            new Node(SkillType.CONDUIT_AURA, 6, 6),
            new Node(SkillType.FROST_WALKER, 4, 6),
            new Node(SkillType.DEEP_DIVE, 2, 6),
            // VOID branch (col 7-8)
            new Node(SkillType.VOID_SIPHON, 8, 7),
            new Node(SkillType.STRUCTURE_SENSE, 6, 8),
            new Node(SkillType.NIGHT_VISION, 4, 8),
            new Node(SkillType.FIRE_RESISTANCE, 2, 8),
            new Node(SkillType.VOID_WALK, 4, 7),
            // Cross-branch
            new Node(SkillType.EFFICIENCY, 9, 3)
    );

    public static List<Node> nodes() {
        return NODES;
    }

    public static Node findNode(SkillType skill) {
        for (Node n : NODES) {
            if (n.skill == skill) return n;
        }
        return null;
    }

    public static List<Connection> connections() {
        List<Connection> list = new ArrayList<>();
        // TERRA trunk
        addVertical(list, findNode(SkillType.AREA_RADIUS), findNode(SkillType.AREA_DEPTH));
        addVertical(list, findNode(SkillType.AREA_DEPTH), findNode(SkillType.PILLAR_BREAK));
        addVertical(list, findNode(SkillType.PILLAR_BREAK), findNode(SkillType.TERRA_BLESSING));
        addVertical(list, findNode(SkillType.AREA_DEPTH), findNode(SkillType.GRAVITY_WELL));
        // MINERAL branch
        addVertical(list, findNode(SkillType.HASTE_AURA), findNode(SkillType.FORTUNE_TOUCH));
        addVertical(list, findNode(SkillType.FORTUNE_TOUCH), findNode(SkillType.VEIN_MINER));
        addVertical(list, findNode(SkillType.VEIN_MINER), findNode(SkillType.MAGNET));
        addVertical(list, findNode(SkillType.MAGNET), findNode(SkillType.CRYSTAL_VISION));
        // ORGANIC branch
        addVertical(list, findNode(SkillType.LEAF_CONSUME), findNode(SkillType.ROOT_RAZE));
        addVertical(list, findNode(SkillType.ROOT_RAZE), findNode(SkillType.SAPLING_REPLANT));
        addVertical(list, findNode(SkillType.SAPLING_REPLANT), findNode(SkillType.BONEMEAL_AURA));
        addVertical(list, findNode(SkillType.BONEMEAL_AURA), findNode(SkillType.NATURE_TOUCH));
        // AQUATIC branch
        addVertical(list, findNode(SkillType.TIDE_BREAKER), findNode(SkillType.CONDUIT_AURA));
        addVertical(list, findNode(SkillType.CONDUIT_AURA), findNode(SkillType.DEEP_DIVE));
        addVertical(list, findNode(SkillType.TIDE_BREAKER), findNode(SkillType.FROST_WALKER));
        // VOID branch
        addVertical(list, findNode(SkillType.STRUCTURE_SENSE), findNode(SkillType.NIGHT_VISION));
        addVertical(list, findNode(SkillType.NIGHT_VISION), findNode(SkillType.FIRE_RESISTANCE));
        addVertical(list, findNode(SkillType.VOID_SIPHON), findNode(SkillType.VOID_WALK));
        // Cross-branch
        addLShape(list, findNode(SkillType.HASTE_AURA), findNode(SkillType.EFFICIENCY));
        return list;
    }

    private static void addVertical(List<Connection> list, Node parent, Node child) {
        if (parent == null || child == null) return;
        for (int r = child.row + 1; r < parent.row; r++) {
            list.add(new Connection(r, parent.col));
        }
    }

    private static void addLShape(List<Connection> list, Node parent, Node child) {
        if (parent == null || child == null) return;
        for (int r = child.row + 1; r < parent.row; r++) {
            list.add(new Connection(r, parent.col));
        }
        int minC = Math.min(parent.col, child.col);
        int maxC = Math.max(parent.col, child.col);
        for (int c = minC + 1; c < maxC; c++) {
            list.add(new Connection(child.row, c));
        }
        if (parent.col != child.col) {
            list.add(new Connection(child.row, parent.col));
        }
    }
}
