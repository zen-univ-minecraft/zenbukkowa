package com.zenbukkowa.persistence;

import com.zenbukkowa.domain.PlayerProgress;
import com.zenbukkowa.domain.PlayerSkills;
import com.zenbukkowa.domain.PointCategory;
import com.zenbukkowa.domain.SkillType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class PlayerDao {
    private final SqliteDatabase db;

    public PlayerDao(SqliteDatabase db) {
        this.db = db;
    }

    public PlayerProgress loadProgress(UUID uuid) throws SQLException {
        PlayerProgress progress = new PlayerProgress(uuid);
        String sql = "SELECT terra_points, mineral_points, organic_points, aquatic_points, void_points, blocks_broken FROM player_progress WHERE uuid = ?";
        try (PreparedStatement ps = db.connection().prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    progress.setPoints(PointCategory.TERRA, rs.getLong("terra_points"));
                    progress.setPoints(PointCategory.MINERAL, rs.getLong("mineral_points"));
                    progress.setPoints(PointCategory.ORGANIC, rs.getLong("organic_points"));
                    progress.setPoints(PointCategory.AQUATIC, rs.getLong("aquatic_points"));
                    progress.setPoints(PointCategory.VOID, rs.getLong("void_points"));
                    progress.setBlocksBroken(rs.getLong("blocks_broken"));
                }
            }
        }
        return progress;
    }

    public void saveProgress(PlayerProgress progress) throws SQLException {
        String sql = """
            INSERT INTO player_progress (uuid, terra_points, mineral_points, organic_points, aquatic_points, void_points, total_points, blocks_broken)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            ON CONFLICT(uuid) DO UPDATE SET
                terra_points=excluded.terra_points,
                mineral_points=excluded.mineral_points,
                organic_points=excluded.organic_points,
                aquatic_points=excluded.aquatic_points,
                void_points=excluded.void_points,
                total_points=excluded.total_points,
                blocks_broken=excluded.blocks_broken
            """;
        try (PreparedStatement ps = db.connection().prepareStatement(sql)) {
            ps.setString(1, progress.uuid().toString());
            ps.setLong(2, progress.points(PointCategory.TERRA));
            ps.setLong(3, progress.points(PointCategory.MINERAL));
            ps.setLong(4, progress.points(PointCategory.ORGANIC));
            ps.setLong(5, progress.points(PointCategory.AQUATIC));
            ps.setLong(6, progress.points(PointCategory.VOID));
            ps.setLong(7, progress.totalPoints());
            ps.setLong(8, progress.blocksBroken());
            ps.executeUpdate();
        }
    }

    public PlayerSkills loadSkills(UUID uuid) throws SQLException {
        PlayerSkills skills = new PlayerSkills(uuid);
        String sql = "SELECT skill_key, tier FROM player_skills WHERE uuid = ?";
        try (PreparedStatement ps = db.connection().prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    try {
                        SkillType skill = SkillType.valueOf(rs.getString("skill_key"));
                        skills.setTier(skill, rs.getInt("tier"));
                    } catch (IllegalArgumentException ignored) {
                    }
                }
            }
        }
        return skills;
    }

    public void saveSkill(UUID uuid, SkillType skill, int tier) throws SQLException {
        String sql = "INSERT INTO player_skills (uuid, skill_key, tier) VALUES (?, ?, ?) ON CONFLICT(uuid, skill_key) DO UPDATE SET tier=excluded.tier";
        try (PreparedStatement ps = db.connection().prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            ps.setString(2, skill.name());
            ps.setInt(3, tier);
            ps.executeUpdate();
        }
    }

    public void deleteProgress(UUID uuid) throws SQLException {
        String sql = "DELETE FROM player_progress WHERE uuid = ?";
        try (PreparedStatement ps = db.connection().prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            ps.executeUpdate();
        }
    }

    public void deleteSkills(UUID uuid) throws SQLException {
        String sql = "DELETE FROM player_skills WHERE uuid = ?";
        try (PreparedStatement ps = db.connection().prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            ps.executeUpdate();
        }
    }

    public void deleteAllProgress() throws SQLException {
        try (Statement stmt = db.connection().createStatement()) {
            stmt.execute("DELETE FROM player_progress");
        }
    }

    public void deleteAllSkills() throws SQLException {
        try (Statement stmt = db.connection().createStatement()) {
            stmt.execute("DELETE FROM player_skills");
        }
    }
}
