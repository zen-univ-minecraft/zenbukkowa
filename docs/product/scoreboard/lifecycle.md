# Scoreboard Lifecycle

## Goal

Define when the scoreboard is created, updated, hidden, and destroyed.

## Creation

- On player join: `ScoreboardService.addPlayer()` creates a new sidebar scoreboard.
- If the player has disabled the scoreboard in settings, the main scoreboard is shown instead.

## Update Triggers

1. **Tick update:** every second for the time line.
2. **Page rotation:** every 10 seconds (200 ticks) the global page index increments.
3. **Point mutation:** `PointService` callback triggers `updatePlayer()` for the affected player.
4. **Skill purchase:** `MenuListener` calls `updateAll()` after a successful skill buy.
5. **Event start/end:** `EventService` broadcasts changes.

## Page Rotation

- `ScoreboardService` owns a `currentPage` field (0, 1, or 2).
- `tick()` increments the page every 10 seconds and calls `updateAll()`.
- Players do NOT control page rotation; it is global and automatic.

## Removal

- On player quit: scoreboard reference is dropped.
- On plugin disable: `clearAll()` restores the main scoreboard for all online players.
- On scoreboard toggle OFF: main scoreboard is restored immediately.
