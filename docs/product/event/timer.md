# Event Timer

## Goal

Track elapsed time since event start on the scoreboard.

## Behavior

- Before `/zenbukkowa start`: scoreboard shows `Time: Waiting`.
- After `/zenbukkowa start`: scoreboard shows elapsed time counting up from `00:00:00`.
- After `/zenbukkowa end`: scoreboard freezes at final elapsed time and shows `Time: Finished`.

## Commands

- `/zenbukkowa start` (requires op): records start timestamp, begins elapsed timer, broadcasts `Event started!`.
- `/zenbukkowa end` (requires op): records end, broadcasts winner, freezes timer.
- `/zenbukkowa status`: returns `Waiting`, `Running`, or `Finished`.

## Clock Rules

1. Timer resolution: 1 second.
2. Timer updates on the scoreboard every second via scheduled task.
3. No automatic stop; event runs until `/zenbukkowa end` is issued.
4. Elapsed time is derived from `System.currentTimeMillis()` delta from start timestamp.

## Scoreboard Integration

- `ScoreboardService` computes elapsed time from `EventService.startTimestamp`.
- `EventService` does not manage its own display task; it only stores start/end state.
