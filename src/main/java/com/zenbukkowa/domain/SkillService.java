package com.zenbukkowa.domain;

import com.zenbukkowa.persistence.PlayerDao;

import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SkillService {
    private final PlayerDao playerDao;
    private final Map<UUID, PlayerSkills> cache = new ConcurrentHashMap<>();

    public SkillService(PlayerDao playerDao) {
        this.playerDao = playerDao;
    }

    public PlayerSkills getSkills(UUID uuid) {
        return cache.computeIfAbsent(uuid, u -> {
            try {
                return playerDao.loadSkills(u);
            } catch (SQLException e) {
                return new PlayerSkills(u);
            }
        });
    }

    public boolean canPurchase(UUID uuid, SkillType skill, int targetTier) {
        PlayerSkills skills = getSkills(uuid);
        int current = skills.tier(skill);
        if (targetTier != current + 1) {
            return false;
        }
        if (targetTier > skill.maxTier()) {
            return false;
        }
        return checkPrerequisites(skills, skill, targetTier);
    }

    private boolean checkPrerequisites(PlayerSkills skills, SkillType skill, int targetTier) {
        return switch (skill) {
            case AREA_DEPTH -> targetTier < 3 || skills.tier(SkillType.AREA_RADIUS) >= 3;
            case VOID_SIPHON -> targetTier < 2 || skills.hasSkill(SkillType.STRUCTURE_SENSE);
            case FORTUNE_TOUCH -> targetTier < 3 || skills.tier(SkillType.HASTE_AURA) >= 3;
            default -> true;
        };
    }

    public void purchase(UUID uuid, SkillType skill, int targetTier) {
        if (!canPurchase(uuid, skill, targetTier)) {
            throw new IllegalStateException("Cannot purchase " + skill + " tier " + targetTier);
        }
        PlayerSkills skills = getSkills(uuid);
        skills.setTier(skill, targetTier);
        try {
            playerDao.saveSkill(uuid, skill, targetTier);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int radius(UUID uuid) {
        return getSkills(uuid).tier(SkillType.AREA_RADIUS);
    }

    public int depth(UUID uuid) {
        return getSkills(uuid).tier(SkillType.AREA_DEPTH);
    }

    public int haste(UUID uuid) {
        return getSkills(uuid).tier(SkillType.HASTE_AURA);
    }

    public void unload(UUID uuid) {
        cache.remove(uuid);
    }
}
