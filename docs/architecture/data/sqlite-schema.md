# SQLite Schema

## Tables

### player_progress

```sql
CREATE TABLE player_progress (
    uuid TEXT PRIMARY KEY,
    terra_points INTEGER NOT NULL DEFAULT 0,
    mineral_points INTEGER NOT NULL DEFAULT 0,
    organic_points INTEGER NOT NULL DEFAULT 0,
    aquatic_points INTEGER NOT NULL DEFAULT 0,
    void_points INTEGER NOT NULL DEFAULT 0,
    total_points INTEGER NOT NULL DEFAULT 0,
    blocks_broken INTEGER NOT NULL DEFAULT 0
);
```

### player_skills

```sql
CREATE TABLE player_skills (
    uuid TEXT NOT NULL,
    skill_key TEXT NOT NULL,
    tier INTEGER NOT NULL DEFAULT 0,
    PRIMARY KEY (uuid, skill_key)
);
```

### structure_claims

```sql
CREATE TABLE structure_claims (
    structure_id TEXT PRIMARY KEY,
    structure_type TEXT NOT NULL,
    world TEXT NOT NULL,
    x INTEGER NOT NULL,
    y INTEGER NOT NULL,
    z INTEGER NOT NULL,
    claimed_by TEXT,
    claimed_at INTEGER
);
```

### break_log

```sql
CREATE TABLE break_log (
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
);
```

## Indexes

```sql
CREATE INDEX idx_break_log_uuid ON break_log(uuid);
CREATE INDEX idx_break_log_timestamp ON break_log(timestamp);
```
