package com.zenbukkowa.persistence;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqliteDatabase {
    private final Path path;
    private Connection connection;

    public SqliteDatabase(Path path) {
        this.path = path;
    }

    public void initialize() throws SQLException {
        path.getParent().toFile().mkdirs();
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS player_progress (
                    uuid TEXT PRIMARY KEY,
                    terra_points INTEGER NOT NULL DEFAULT 0,
                    mineral_points INTEGER NOT NULL DEFAULT 0,
                    organic_points INTEGER NOT NULL DEFAULT 0,
                    aquatic_points INTEGER NOT NULL DEFAULT 0,
                    void_points INTEGER NOT NULL DEFAULT 0,
                    crop_points INTEGER NOT NULL DEFAULT 0,
                    discovery_points INTEGER NOT NULL DEFAULT 0,
                    total_points INTEGER NOT NULL DEFAULT 0,
                    blocks_broken INTEGER NOT NULL DEFAULT 0
                )
                """);
            migratePlayerProgress(stmt);
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS player_skills (
                    uuid TEXT NOT NULL,
                    skill_key TEXT NOT NULL,
                    tier INTEGER NOT NULL DEFAULT 0,
                    PRIMARY KEY (uuid, skill_key)
                )
                """);
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS structure_claims (
                    structure_id TEXT PRIMARY KEY,
                    structure_type TEXT NOT NULL,
                    world TEXT NOT NULL,
                    x INTEGER NOT NULL,
                    y INTEGER NOT NULL,
                    z INTEGER NOT NULL,
                    claimed_by TEXT,
                    claimed_at INTEGER
                )
                """);
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS break_log (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    uuid TEXT NOT NULL,
                    material TEXT NOT NULL,
                    category TEXT NOT NULL,
                    points INTEGER NOT NULL,
                    x INTEGER NOT NULL,
                    y INTEGER NOT NULL,
                    z INTEGER NOT NULL,
                    world TEXT NOT NULL,
                    timestamp INTEGER NOT NULL
                )
                """);
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS player_settings (
                    uuid TEXT PRIMARY KEY,
                    locale TEXT NOT NULL DEFAULT 'en'
                )
                """);
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS player_placed_blocks (
                    world TEXT NOT NULL,
                    x INTEGER NOT NULL,
                    y INTEGER NOT NULL,
                    z INTEGER NOT NULL,
                    player_uuid TEXT NOT NULL,
                    placed_at INTEGER NOT NULL,
                    PRIMARY KEY (world, x, y, z)
                )
                """);
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_placed_player ON player_placed_blocks(player_uuid)");
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS player_discoveries (
                    uuid TEXT NOT NULL,
                    material TEXT NOT NULL,
                    discovered_at INTEGER NOT NULL,
                    PRIMARY KEY (uuid, material)
                )
                """);
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_break_log_uuid ON break_log(uuid)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_break_log_timestamp ON break_log(timestamp)");
        }
    }

    private void migratePlayerProgress(Statement stmt) throws SQLException {
        try (ResultSet rs = stmt.executeQuery("PRAGMA table_info(player_progress)")) {
            boolean hasDiscovery = false;
            while (rs.next()) {
                if ("discovery_points".equals(rs.getString("name"))) {
                    hasDiscovery = true;
                    break;
                }
            }
            if (!hasDiscovery) {
                stmt.execute("ALTER TABLE player_progress ADD COLUMN discovery_points INTEGER NOT NULL DEFAULT 0");
            }
        }
    }

    public Connection connection() {
        return connection;
    }

    public void close() {
        try {
            if (connection != null) connection.close();
        } catch (SQLException ignored) {}
    }
}
