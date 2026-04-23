package com.zenbukkowa.gui;

import com.zenbukkowa.domain.SkillType;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class SkillTreeLayout {

    public static final int GRID_ROWS = 13;
    public static final int GRID_COLS = 20;
    public static final int VIEWPORT_ROWS = 5;
    public static final int VIEWPORT_COLS = 9;
    public static final int MAX_SCROLL_V = GRID_ROWS - VIEWPORT_ROWS;
    public static final int MAX_SCROLL_H = GRID_COLS - VIEWPORT_COLS;

    public record Node(SkillType skill, int row, int col) {}
    public record Connection(int row, int col) {}

    private static final List<Node> NODES = List.of(
            new Node(SkillType.ANGEL_WINGS, 0, 8),
            new Node(SkillType.TITAN_STRIKE, 0, 10),
            new Node(SkillType.AREA_RADIUS, 12, 9),
            new Node(SkillType.AREA_DEPTH, 10, 9),
            new Node(SkillType.PILLAR_BREAK, 8, 9),
            new Node(SkillType.GRAVITY_WELL, 6, 9),
            new Node(SkillType.TERRA_BLESSING, 4, 9),
            new Node(SkillType.HASTE_AURA, 11, 6),
            new Node(SkillType.FORTUNE_TOUCH, 9, 6),
            new Node(SkillType.VEIN_MINER, 7, 6),
            new Node(SkillType.MAGNET, 5, 6),
            new Node(SkillType.CRYSTAL_VISION, 3, 6),
            new Node(SkillType.BLAST_MINING, 1, 6),
            new Node(SkillType.LEAF_CONSUME, 11, 3),
            new Node(SkillType.ROOT_RAZE, 9, 3),
            new Node(SkillType.SAPLING_REPLANT, 7, 3),
            new Node(SkillType.BONEMEAL_AURA, 5, 3),
            new Node(SkillType.NATURE_TOUCH, 3, 3),
            new Node(SkillType.WILD_GROWTH, 1, 3),
            new Node(SkillType.TIDE_BREAKER, 11, 12),
            new Node(SkillType.SALVAGE, 11, 14),
            new Node(SkillType.CONDUIT_AURA, 7, 12),
            new Node(SkillType.FROST_WALKER, 9, 13),
            new Node(SkillType.DEEP_DIVE, 3, 12),
            new Node(SkillType.TSUNAMI, 1, 12),
            new Node(SkillType.VOID_SIPHON, 9, 15),
            new Node(SkillType.STRUCTURE_SENSE, 9, 17),
            new Node(SkillType.NIGHT_VISION, 5, 17),
            new Node(SkillType.FIRE_RESISTANCE, 3, 17),
            new Node(SkillType.VOID_WALK, 5, 15),
            new Node(SkillType.VOID_RIFT, 1, 15),
            new Node(SkillType.GREEN_THUMB, 11, 0),
            new Node(SkillType.HARVEST_AURA, 9, 0),
            new Node(SkillType.COMPOST_MASTER, 7, 0),
            new Node(SkillType.SEED_SATCHEL, 5, 1),
            new Node(SkillType.FARMERS_FORTUNE, 3, 0),
            new Node(SkillType.HARVEST_WAVE, 1, 0),
            new Node(SkillType.EFFICIENCY, 10, 10),
            new Node(SkillType.CURIOUS_MINER, 11, 18),
            new Node(SkillType.GEOLOGIST, 9, 18),
            new Node(SkillType.SURVEYOR, 7, 18),
            new Node(SkillType.CARTOGRAPHER, 5, 18),
            new Node(SkillType.PATHFINDER, 3, 18),
            new Node(SkillType.WORLD_WALKER, 1, 18)
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
        map.put(SkillType.BLAST_MINING, SkillType.CRYSTAL_VISION);
        map.put(SkillType.ROOT_RAZE, SkillType.LEAF_CONSUME);
        map.put(SkillType.SAPLING_REPLANT, SkillType.ROOT_RAZE);
        map.put(SkillType.BONEMEAL_AURA, SkillType.SAPLING_REPLANT);
        map.put(SkillType.NATURE_TOUCH, SkillType.BONEMEAL_AURA);
        map.put(SkillType.WILD_GROWTH, SkillType.NATURE_TOUCH);
        map.put(SkillType.CONDUIT_AURA, SkillType.TIDE_BREAKER);
        map.put(SkillType.FROST_WALKER, SkillType.TIDE_BREAKER);
        map.put(SkillType.DEEP_DIVE, SkillType.CONDUIT_AURA);
        map.put(SkillType.TSUNAMI, SkillType.DEEP_DIVE);
        map.put(SkillType.NIGHT_VISION, SkillType.STRUCTURE_SENSE);
        map.put(SkillType.FIRE_RESISTANCE, SkillType.NIGHT_VISION);
        map.put(SkillType.VOID_WALK, SkillType.VOID_SIPHON);
        map.put(SkillType.VOID_RIFT, SkillType.VOID_WALK);
        map.put(SkillType.HARVEST_AURA, SkillType.GREEN_THUMB);
        map.put(SkillType.COMPOST_MASTER, SkillType.HARVEST_AURA);
        map.put(SkillType.SEED_SATCHEL, SkillType.COMPOST_MASTER);
        map.put(SkillType.FARMERS_FORTUNE, SkillType.SEED_SATCHEL);
        map.put(SkillType.HARVEST_WAVE, SkillType.FARMERS_FORTUNE);
        map.put(SkillType.EFFICIENCY, SkillType.HASTE_AURA);
        map.put(SkillType.GEOLOGIST, SkillType.CURIOUS_MINER);
        map.put(SkillType.SURVEYOR, SkillType.GEOLOGIST);
        map.put(SkillType.CARTOGRAPHER, SkillType.SURVEYOR);
        map.put(SkillType.PATHFINDER, SkillType.CARTOGRAPHER);
        map.put(SkillType.WORLD_WALKER, SkillType.PATHFINDER);
        return map;
    }

    public static List<Node> nodes() { return NODES; }

    public static Node findNode(SkillType skill) {
        for (Node n : NODES) { if (n.skill == skill) return n; }
        return null;
    }

    public static SkillType parentOf(SkillType skill) { return PARENTS.get(skill); }

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
