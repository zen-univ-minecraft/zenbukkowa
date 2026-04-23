package com.zenbukkowa.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PlayerPlacedBlockDao {
    private final SqliteDatabase db;

    public PlayerPlacedBlockDao(SqliteDatabase db) {
        this.db = db;
    }

    public void record(String world, int x, int y, int z, String playerUuid) {
        String sql = "INSERT OR REPLACE INTO player_placed_blocks (world, x, y, z, player_uuid, placed_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = db.connection().prepareStatement(sql)) {
            ps.setString(1, world);
            ps.setInt(2, x);
            ps.setInt(3, y);
            ps.setInt(4, z);
            ps.setString(5, playerUuid);
            ps.setLong(6, System.currentTimeMillis());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isPlayerPlaced(String world, int x, int y, int z) {
        String sql = "SELECT 1 FROM player_placed_blocks WHERE world = ? AND x = ? AND y = ? AND z = ?";
        try (PreparedStatement ps = db.connection().prepareStatement(sql)) {
            ps.setString(1, world);
            ps.setInt(2, x);
            ps.setInt(3, y);
            ps.setInt(4, z);
            var rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }

    public void delete(String world, int x, int y, int z) {
        String sql = "DELETE FROM player_placed_blocks WHERE world = ? AND x = ? AND y = ? AND z = ?";
        try (PreparedStatement ps = db.connection().prepareStatement(sql)) {
            ps.setString(1, world);
            ps.setInt(2, x);
            ps.setInt(3, y);
            ps.setInt(4, z);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteForPlayer(String playerUuid) {
        String sql = "DELETE FROM player_placed_blocks WHERE player_uuid = ?";
        try (PreparedStatement ps = db.connection().prepareStatement(sql)) {
            ps.setString(1, playerUuid);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAll() {
        String sql = "DELETE FROM player_placed_blocks";
        try (PreparedStatement ps = db.connection().prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
