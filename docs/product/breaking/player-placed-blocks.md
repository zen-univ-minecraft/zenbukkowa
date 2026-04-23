# Player-Placed Block Tracking

## Goal

Prevent point farming by distinguishing natural blocks from player-placed or piston-moved blocks.

## Rules

1. Every block placed by a player is recorded per-coordinate in SQLite.
2. When a block is broken by any means, its coordinate is removed from tracking.
3. Blocks moved by pistons transfer tracking only if the source was tracked.
4. Natural blocks pushed by pistons are NOT tracked at their new location.

## Lifecycle

### Place

- `BlockPlaceEvent` records `(world, x, y, z, player_uuid)`.
- The record is also cached in memory.

### Break

- Any break (player, area break, explosion, etc.) removes the coordinate.
- Removal happens from both cache and database.

### Piston Extend

- For every block in the moved chain:
  - Delete the source coordinate.
  - If the source was player-placed, record the target with the same player UUID.
  - If the source was natural, do NOT record the target.

### Piston Retract (Sticky)

- Same rules as extend.

## Point Impact

- Player-placed blocks are broken normally but award zero points.
- Discovery bonuses are never awarded for player-placed blocks.
- The tracking table does not grow unbounded because entries are deleted on break.

## Persistence

- Table: `player_placed_blocks` keyed by `(world, x, y, z)`.
- Reset deletes all records for the target player(s).
