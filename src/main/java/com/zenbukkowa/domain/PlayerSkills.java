package com.zenbukkowa.domain;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

public class PlayerSkills {
    private final UUID uuid;
    private final Map<SkillType, Integer> tiers;

    public PlayerSkills(UUID uuid) {
        this.uuid = uuid;
        this.tiers = new EnumMap<>(SkillType.class);
    }

    public UUID uuid() {
        return uuid;
    }

    public int tier(SkillType skill) {
        return tiers.getOrDefault(skill, 0);
    }

    public void setTier(SkillType skill, int tier) {
        tiers.put(skill, Math.max(0, Math.min(tier, skill.maxTier())));
    }

    public boolean hasSkill(SkillType skill) {
        return tier(skill) > 0;
    }
}
