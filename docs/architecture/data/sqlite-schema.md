# SQLite Schema

## Goal

Document all tables, indexes, and reset behavior.

## Tables

### player_progress
- `uuid TEXT PRIMARY KEY`
- `terra_points INTEGER NOT NULL DEFAULT 0`
- `mineral_points INTEGER NOT NULL DEFAULT 0`
- `organic_points INTEGER NOT NULL DEFAULT 0`
- `aquatic_points INTEGER NOT NULL DEFAULT 0`
- `void_points INTEGER NOT NULL DEFAULT 0`
- `crop_points INTEGER NOT NULL DEFAULT 0`
- `total_points INTEGER NOT NULL DEFAULT 0`
- `blocks_broken INTEGER NOT NULL DEFAULT 0`

### player_skills
- `uuid TEXT NOT NULL`
- `skill_key TEXT NOT NULL`
- `tier INTEGER NOT NULL DEFAULT 0`
- Primary key: `(uuid, skill_key)`

### structure_claims
- `structure_id TEXT PRIMARY KEY`
- `structure_type TEXT NOT NULL`
- `world TEXT NOT NULL`
- `x INTEGER NOT NULL`
- `y INTEGER NOT NULL`
- `z INTEGER NOT NULL`
- `claimed_by TEXT`
- `claimed_at INTEGER`

### break_log
- `id INTEGER PRIMARY KEY AUTOINCREMENT`
- `uuid TEXT NOT NULL`
- `material TEXT NOT NULL`
- `category TEXT NOT NULL`
- `points INTEGER NOT NULL`
- `x INTEGER NOT NULL`
- `y INTEGER NOT NULL`
- `z INTEGER NOT NULL`
- `world TEXT NOT NULL`
- `timestamp INTEGER NOT NULL`

### player_settings
- `uuid TEXT PRIMARY KEY`
- `locale TEXT NOT NULL DEFAULT 'en'`

## Indexes

- `idx_break_log_uuid` on `break_log(uuid)`
- `idx_break_log_timestamp` on `break_log(timestamp)`

## Reset Behavior

- `DELETE FROM player_progress WHERE uuid = ?`
- `DELETE FROM player_skills WHERE uuid = ?`
- `DELETE FROM break_log WHERE uuid = ?`
- `DELETE FROM player_settings WHERE uuid = ?` (optional, if full wipe desired)
- `DELETE FROM player_progress` (all-players reset)
- `DELETE FROM player_skills` (all-players reset)
- `DELETE FROM break_log` (all-players reset)
- Structure claims are **never** deleted by reset.
