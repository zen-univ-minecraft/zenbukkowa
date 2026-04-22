package com.zenbukkowa.persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StructureDao {
    private final SqliteDatabase db;

    public StructureDao(SqliteDatabase db) {
        this.db = db;
    }

    public boolean isClaimed(String structureId) throws SQLException {
        String sql = "SELECT 1 FROM structure_claims WHERE structure_id = ? AND claimed_by IS NOT NULL";
        try (PreparedStatement ps = db.connection().prepareStatement(sql)) {
            ps.setString(1, structureId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void claim(String structureId, String type, String world, int x, int y, int z, String playerUuid) throws SQLException {
        String sql = "INSERT INTO structure_claims (structure_id, structure_type, world, x, y, z, claimed_by, claimed_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT(structure_id) DO UPDATE SET claimed_by=excluded.claimed_by, claimed_at=excluded.claimed_at";
        try (PreparedStatement ps = db.connection().prepareStatement(sql)) {
            ps.setString(1, structureId);
            ps.setString(2, type);
            ps.setString(3, world);
            ps.setInt(4, x);
            ps.setInt(5, y);
            ps.setInt(6, z);
            ps.setString(7, playerUuid);
            ps.setLong(8, System.currentTimeMillis());
            ps.executeUpdate();
        }
    }
}
