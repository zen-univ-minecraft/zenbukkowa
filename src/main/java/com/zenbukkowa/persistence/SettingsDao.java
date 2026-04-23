package com.zenbukkowa.persistence;

import java.sql.*;
import java.util.*;

public class SettingsDao {
    private final SqliteDatabase db;

    public SettingsDao(SqliteDatabase db) {
        this.db = db;
    }

    public void initialize() throws SQLException {
        try (Statement stmt = db.connection().createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS player_settings (
                    uuid TEXT PRIMARY KEY,
                    locale TEXT NOT NULL DEFAULT 'en'
                )
                """);
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS server_settings (
                    key TEXT PRIMARY KEY,
                    value TEXT NOT NULL
                )
                """);
        }
    }

    public void saveLocale(UUID uuid, String locale) {
        String sql = "INSERT INTO player_settings (uuid, locale) VALUES (?, ?) ON CONFLICT(uuid) DO UPDATE SET locale=excluded.locale";
        try (PreparedStatement ps = db.connection().prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            ps.setString(2, locale);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<UUID, String> loadAllLocales() {
        Map<UUID, String> map = new HashMap<>();
        try (Statement stmt = db.connection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT uuid, locale FROM player_settings")) {
            while (rs.next()) {
                map.put(UUID.fromString(rs.getString("uuid")), rs.getString("locale"));
            }
        } catch (SQLException ignored) {}
        return map;
    }

    public void saveSetting(String key, String value) {
        String sql = "INSERT INTO server_settings (key, value) VALUES (?, ?) ON CONFLICT(key) DO UPDATE SET value=excluded.value";
        try (PreparedStatement ps = db.connection().prepareStatement(sql)) {
            ps.setString(1, key);
            ps.setString(2, value);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String loadSetting(String key, String defaultValue) {
        String sql = "SELECT value FROM server_settings WHERE key = ?";
        try (PreparedStatement ps = db.connection().prepareStatement(sql)) {
            ps.setString(1, key);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("value");
            }
        } catch (SQLException ignored) {}
        return defaultValue;
    }
}
