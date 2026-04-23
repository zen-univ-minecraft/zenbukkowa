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

    private static final Map<SkillType, SkillType> PARENTS = buildParents();
    private static final List<Node> NODES = SkillTreeGenerator.generateNodes(PARENTS);

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
