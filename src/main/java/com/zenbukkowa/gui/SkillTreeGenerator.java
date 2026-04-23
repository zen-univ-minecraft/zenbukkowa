package com.zenbukkowa.gui;

import com.zenbukkowa.domain.SkillType;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class SkillTreeGenerator {

    static List<SkillTreeLayout.Node> generateNodes(Map<SkillType, SkillType> parents) {
        Map<SkillType, Integer> columns = resolveColumns();

        Map<SkillType, Integer> maxRow = new EnumMap<>(SkillType.class);
        Queue<SkillType> queue = new ArrayDeque<>();

        int rootRow = SkillTreeLayout.GRID_ROWS - 1;
        for (SkillType skill : SkillType.values()) {
            if (!parents.containsKey(skill)) {
                maxRow.put(skill, rootRow);
                queue.add(skill);
            }
        }

        while (!queue.isEmpty()) {
            SkillType parent = queue.poll();
            for (SkillType child : getChildren(parent, parents)) {
                int candidate = maxRow.get(parent) - 2;
                if (!maxRow.containsKey(child) || candidate < maxRow.get(child)) {
                    maxRow.put(child, candidate);
                    queue.add(child);
                }
            }
        }

        Map<Integer, List<SkillType>> byColumn = new HashMap<>();
        for (SkillType skill : SkillType.values()) {
            if (skill == SkillType.ANGEL_WINGS || skill == SkillType.TITAN_STRIKE) continue;
            byColumn.computeIfAbsent(columns.get(skill), k -> new ArrayList<>()).add(skill);
        }

        Map<SkillType, Integer> actualRow = new EnumMap<>(SkillType.class);
        for (Map.Entry<Integer, List<SkillType>> entry : byColumn.entrySet()) {
            List<SkillType> skills = entry.getValue();
            skills.sort((a, b) -> Integer.compare(maxRow.get(b), maxRow.get(a)));
            int prevRow = Integer.MAX_VALUE;
            for (SkillType skill : skills) {
                int desired = maxRow.get(skill);
                int allowed = Math.min(desired, prevRow - 2);
                actualRow.put(skill, allowed);
                prevRow = allowed;
            }
        }

        List<SkillTreeLayout.Node> nodes = new ArrayList<>();
        nodes.add(new SkillTreeLayout.Node(SkillType.TITAN_STRIKE, 0, 8));
        nodes.add(new SkillTreeLayout.Node(SkillType.ANGEL_WINGS, 0, 11));
        for (SkillType skill : SkillType.values()) {
            if (skill == SkillType.ANGEL_WINGS || skill == SkillType.TITAN_STRIKE) continue;
            nodes.add(new SkillTreeLayout.Node(skill, actualRow.get(skill), columns.get(skill)));
        }
        return nodes;
    }

    private static Map<SkillType, Integer> resolveColumns() {
        Map<SkillType, Integer> map = new EnumMap<>(SkillType.class);
        Map<com.zenbukkowa.domain.PointCategory, Integer> branchBase = Map.of(
                com.zenbukkowa.domain.PointCategory.CROP, 0,
                com.zenbukkowa.domain.PointCategory.ORGANIC, 3,
                com.zenbukkowa.domain.PointCategory.MINERAL, 6,
                com.zenbukkowa.domain.PointCategory.TERRA, 9,
                com.zenbukkowa.domain.PointCategory.AQUATIC, 12,
                com.zenbukkowa.domain.PointCategory.VOID, 15,
                com.zenbukkowa.domain.PointCategory.DISCOVERY, 18
        );
        for (SkillType skill : SkillType.values()) {
            map.put(skill, branchBase.getOrDefault(skill.category(), 9));
        }
        map.put(SkillType.SEED_SATCHEL, 1);
        map.put(SkillType.EFFICIENCY, 7);
        map.put(SkillType.FROST_WALKER, 13);
        map.put(SkillType.SALVAGE, 13);
        map.put(SkillType.STRUCTURE_SENSE, 16);
        return map;
    }

    private static List<SkillType> getChildren(SkillType parent, Map<SkillType, SkillType> parents) {
        List<SkillType> result = new ArrayList<>();
        for (Map.Entry<SkillType, SkillType> e : parents.entrySet()) {
            if (e.getValue() == parent) {
                result.add(e.getKey());
            }
        }
        return result;
    }
}
