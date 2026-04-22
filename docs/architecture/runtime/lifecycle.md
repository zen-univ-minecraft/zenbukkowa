# Runtime Lifecycle

## Goal

Document plugin enable/disable, player session boundaries, and cache invalidation.

## Enable Sequence

1. Load SQLite and run schema initialization.
2. Instantiate DAOs.
3. Instantiate domain services (PointService, SkillService, BreakService, EventService).
4. Instantiate presentation services (ScoreboardService, MenuService, HotbarMenuService).
5. Register Bukkit listeners and commands.
6. For every online player: install hotbar token, add to scoreboard, apply effects.

## Disable Sequence

1. Clear all scoreboards.
2. Cancel all scheduled tasks.
3. Close SQLite connection.

## Player Join

1. HotbarMenuService installs slot-8 token.
2. ScoreboardService adds player.
3. EffectService applies all active effects.

## Player Quit

1. PointService unloads cache entry.
2. SkillService unloads cache entry.

## Cache Invalidation on Reset

- When an operator resets a player:
  1. DAO deletes rows for that UUID.
  2. PointService and SkillService remove UUID from cache.
  3. If the player is online, re-apply effects and scoreboard.
- When an operator resets all players:
  1. DAO deletes all rows.
  2. PointService and SkillService clear entire cache.
  3. EventService resets timer state.
  4. Scoreboard and effects are refreshed for all online players.
