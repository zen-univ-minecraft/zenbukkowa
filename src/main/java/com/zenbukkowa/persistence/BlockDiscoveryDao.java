package com.zenbukkowa.persistence;

import org.bukkit.Material;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class BlockDiscoveryDao {
    private final SqliteDatabase db;

    public BlockDiscoveryDao(SqliteDatabase db) {
        this.db = db;
    }

    public boolean isDiscovered(UUID uuid, Material material) {
        String sql = "SELECT 1 FROM player_discoveries WHERE uuid = ? AND material = ?";
        try (PreparedStatement ps = db.connection().prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            ps.setString(2, material.name());
            var rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }

    public void recordDiscovery(UUID uuid, Material material) {
        String sql = "INSERT OR IGNORE INTO player_discoveries (uuid, material, discovered_at) VALUES (?, ?, ?)";
        try (PreparedStatement ps = db.connection().prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            ps.setString(2, material.name());
            ps.setLong(3, System.currentTimeMillis());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int countDiscoveries(UUID uuid) {
        String sql = "SELECT COUNT(*) FROM player_discoveries WHERE uuid = ?";
        try (PreparedStatement ps = db.connection().prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            var rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            return 0;
        }
    }

    public void deleteForPlayer(UUID uuid) {
        String sql = "DELETE FROM player_discoveries WHERE uuid = ?";
        try (PreparedStatement ps = db.connection().prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAll() {
        String sql = "DELETE FROM player_discoveries";
        try (PreparedStatement ps = db.connection().prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
