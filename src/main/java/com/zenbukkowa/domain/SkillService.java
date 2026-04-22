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
            try { return playerDao.loadSkills(u); }
            catch (SQLException e) { return new PlayerSkills(u); }
        });
    }

    public boolean canPurchase(UUID uuid, SkillType skill, int targetTier) {
        PlayerSkills skills = getSkills(uuid);
        int current = skills.tier(skill);
        if (targetTier != current + 1) return false;
        if (targetTier > skill.maxTier()) return false;
        return checkPrerequisites(skills, skill, targetTier);
    }

    private boolean checkPrerequisites(PlayerSkills skills, SkillType skill, int targetTier) {
        return switch (skill) {
            case AREA_DEPTH -> targetTier < 3 || skills.tier(SkillType.AREA_RADIUS) >= 3;
            case PILLAR_BREAK -> skills.tier(SkillType.AREA_DEPTH) >= 2;
            case EFFICIENCY -> skills.tier(SkillType.HASTE_AURA) >= 2;
            case GRAVITY_WELL -> skills.tier(SkillType.AREA_DEPTH) >= 3;
            case TERRA_BLESSING -> skills.tier(SkillType.PILLAR_BREAK) >= 1;
            case FORTUNE_TOUCH -> targetTier < 3 || skills.tier(SkillType.HASTE_AURA) >= 3;
            case VEIN_MINER -> skills.tier(SkillType.FORTUNE_TOUCH) >= 2;
            case MAGNET -> skills.tier(SkillType.VEIN_MINER) >= 1;
            case CRYSTAL_VISION -> skills.hasSkill(SkillType.MAGNET);
            case ROOT_RAZE -> skills.hasSkill(SkillType.LEAF_CONSUME);
            case SAPLING_REPLANT -> skills.hasSkill(SkillType.ROOT_RAZE);
            case BONEMEAL_AURA -> skills.hasSkill(SkillType.SAPLING_REPLANT);
            case NATURE_TOUCH -> skills.tier(SkillType.BONEMEAL_AURA) >= 2;
            case FROST_WALKER -> skills.hasSkill(SkillType.TIDE_BREAKER);
            case CONDUIT_AURA -> skills.hasSkill(SkillType.TIDE_BREAKER);
            case DEEP_DIVE -> skills.tier(SkillType.CONDUIT_AURA) >= 2;
            case VOID_SIPHON -> targetTier < 2 || skills.hasSkill(SkillType.STRUCTURE_SENSE);
            case NIGHT_VISION -> skills.hasSkill(SkillType.STRUCTURE_SENSE);
            case FIRE_RESISTANCE -> skills.hasSkill(SkillType.NIGHT_VISION);
            case VOID_WALK -> skills.tier(SkillType.VOID_SIPHON) >= 2;
            default -> true;
        };
    }

    public void purchase(UUID uuid, SkillType skill, int targetTier) {
        if (!canPurchase(uuid, skill, targetTier)) {
            throw new IllegalStateException("Cannot purchase " + skill + " tier " + targetTier);
        }
        PlayerSkills skills = getSkills(uuid);
        skills.setTier(skill, targetTier);
        try { playerDao.saveSkill(uuid, skill, targetTier); }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public int radius(UUID uuid) { return getSkills(uuid).tier(SkillType.AREA_RADIUS); }
    public int depth(UUID uuid) { return getSkills(uuid).tier(SkillType.AREA_DEPTH); }
    public int haste(UUID uuid) { return getSkills(uuid).tier(SkillType.HASTE_AURA); }
    public int efficiency(UUID uuid) { return getSkills(uuid).tier(SkillType.EFFICIENCY); }

    public void resetPlayer(UUID uuid) {
        try {
            playerDao.deleteSkills(uuid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        cache.remove(uuid);
    }

    public void resetAll() {
        try {
            playerDao.deleteAllSkills();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        cache.clear();
    }

    public void unload(UUID uuid) { cache.remove(uuid); }
}
