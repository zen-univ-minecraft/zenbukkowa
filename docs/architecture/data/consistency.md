# Data Consistency

## Invariants

1. `total_points` in `player_progress` equals the sum of the six category columns.
2. `player_skills.tier` is never negative.
3. `structure_claims.claimed_by` is NULL until claimed.
4. `break_log` is append-only.

## Enforcement

- `PointService` updates all six point columns in a single SQL transaction.
- `SkillService` validates tier bounds before write.
- `StructureService` uses `INSERT OR IGNORE` to prevent duplicate claims.

## Recovery

- On plugin enable, if `total_points` disagrees with sum, recalculate from `break_log`.
- This is a one-time repair and is logged.
