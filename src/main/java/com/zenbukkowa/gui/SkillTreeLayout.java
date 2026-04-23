package com.zenbukkowa.gui;

import com.zenbukkowa.domain.SkillType;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class SkillTreeLayout {

    public static final int GRID_ROWS = 12;
    public static final int GRID_COLS = 15;
    public static final int VIEWPORT_ROWS = 5;
    public static final int VIEWPORT_COLS = 9;
    public static final int MAX_SCROLL_V = GRID_ROWS - VIEWPORT_ROWS;
    public static final int MAX_SCROLL_H = GRID_COLS - VIEWPORT_COLS;

    public record Node(SkillType skill, int row, int col) {}
    public record Connection(int row, int col) {}

    private static final List<Node> NODES = List.of(
            // TERRA trunk (col 7)
            new Node(SkillType.AREA_RADIUS, 11, 7),
            new Node(SkillType.AREA_DEPTH, 9, 7),
            new Node(SkillType.PILLAR_BREAK, 7, 7),
            new Node(SkillType.GRAVITY_WELL, 5, 7),
            new Node(SkillType.TERRA_BLESSING, 3, 7),
            // MINERAL branch (col 4)
            new Node(SkillType.HASTE_AURA, 10, 4),
            new Node(SkillType.FORTUNE_TOUCH, 8, 4),
            new Node(SkillType.VEIN_MINER, 6, 4),
            new Node(SkillType.MAGNET, 4, 4),
            new Node(SkillType.CRYSTAL_VISION, 2, 4),
            // ORGANIC branch (col 2)
            new Node(SkillType.LEAF_CONSUME, 8, 2),
            new Node(SkillType.ROOT_RAZE, 6, 2),
            new Node(SkillType.SAPLING_REPLANT, 4, 2),
            new Node(SkillType.BONEMEAL_AURA, 2, 2),
            new Node(SkillType.NATURE_TOUCH, 0, 2),
            // AQUATIC branch (cols 10-11)
            new Node(SkillType.TIDE_BREAKER, 10, 10),
            new Node(SkillType.SALVAGE, 10, 12),
            new Node(SkillType.CONDUIT_AURA, 6, 10),
            new Node(SkillType.FROST_WALKER, 8, 11),
            new Node(SkillType.DEEP_DIVE, 2, 10),
            // VOID branch (cols 12-14)
            new Node(SkillType.VOID_SIPHON, 8, 12),
            new Node(SkillType.STRUCTURE_SENSE, 8, 14),
            new Node(SkillType.NIGHT_VISION, 4, 14),
            new Node(SkillType.FIRE_RESISTANCE, 2, 14),
            new Node(SkillType.VOID_WALK, 4, 12),
            // CROP branch (cols 0-1)
            new Node(SkillType.GREEN_THUMB, 10, 0),
            new Node(SkillType.HARVEST_AURA, 8, 0),
            new Node(SkillType.COMPOST_MASTER, 6, 0),
            new Node(SkillType.SEED_SATCHEL, 4, 1),
            new Node(SkillType.FARMERS_FORTUNE, 2, 0),
            // Cross-branch
            new Node(SkillType.EFFICIENCY, 9, 5)
    );

    private static final Map<SkillType, SkillType> PARENTS = buildParents();

    private static Map<SkillType, SkillType> buildParents() {
        Map<SkillType, SkillType> map = new EnumMap<>(SkillType.class);
        map.put(SkillType.AREA_DEPTH, SkillType.AREA_RADIUS);
        map.put(SkillType.PILLAR_BREAK, SkillType.AREA_DEPTH);
        map.put(SkillType.GRAVITY_WELL, SkillType.AREA_DEPTH);
        map.put(SkillType.TERRA_BLESSING, SkillType.PILLAR_BREAK);
        map.put(SkillType.FORTUNE_TOUCH, SkillType.HASTE_AURA);
        map.put(SkillType.VEIN_MINER, SkillType.FORTUNE_TOUCH);
        map.put(SkillType.MAGNET, SkillType.VEIN_MINER);
        map.put(SkillType.CRYSTAL_VISION, SkillType.MAGNET);
        map.put(SkillType.ROOT_RAZE, SkillType.LEAF_CONSUME);
        map.put(SkillType.SAPLING_REPLANT, SkillType.ROOT_RAZE);
        map.put(SkillType.BONEMEAL_AURA, SkillType.SAPLING_REPLANT);
        map.put(SkillType.NATURE_TOUCH, SkillType.BONEMEAL_AURA);
        map.put(SkillType.CONDUIT_AURA, SkillType.TIDE_BREAKER);
        map.put(SkillType.FROST_WALKER, SkillType.TIDE_BREAKER);
        map.put(SkillType.DEEP_DIVE, SkillType.CONDUIT_AURA);
        map.put(SkillType.NIGHT_VISION, SkillType.STRUCTURE_SENSE);
        map.put(SkillType.FIRE_RESISTANCE, SkillType.NIGHT_VISION);
        map.put(SkillType.VOID_WALK, SkillType.VOID_SIPHON);
        map.put(SkillType.HARVEST_AURA, SkillType.GREEN_THUMB);
        map.put(SkillType.COMPOST_MASTER, SkillType.HARVEST_AURA);
        map.put(SkillType.SEED_SATCHEL, SkillType.COMPOST_MASTER);
        map.put(SkillType.FARMERS_FORTUNE, SkillType.SEED_SATCHEL);
        map.put(SkillType.EFFICIENCY, SkillType.HASTE_AURA);
        return map;
    }

    public static List<Node> nodes() {
        return NODES;
    }

    public static Node findNode(SkillType skill) {
        for (Node n : NODES) {
            if (n.skill == skill) return n;
        }
        return null;
    }

    public static SkillType parentOf(SkillType skill) {
        return PARENTS.get(skill);
    }

    public static List<Connection> connections() {
        List<Connection> list = new ArrayList<>();
        for (Map.Entry<SkillType, SkillType> e : PARENTS.entrySet()) {
            Node child = findNode(e.getKey());
            Node parent = findNode(e.getValue());
            if (child == null || parent == null) continue;
            if (parent.col == child.col) {
                addVertical(list, parent, child);
            } else {
                addLShape(list, parent, child);
            }
        }
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
