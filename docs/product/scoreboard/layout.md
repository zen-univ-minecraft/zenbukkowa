# Scoreboard Layout

## Goal

Specify the exact sidebar lines visible to all players, with all 7 categories shown and dynamically sorted by recency.

## Layout

```
zenbukkowa
-------------
Time: 00:42:15
Total: 12,345
[most recent category]
[2nd most recent]
[3rd most recent]
[4th most recent]
[5th most recent]
[6th most recent]
[7th most recent]
-------------
Area: 5x5x5
Haste: III
Event Running
-------------
```

## Recency Ordering

Each player has their own category ordering based on when each category was last mutated (points added or spent). The order updates immediately on any point change. Ties break by `PointCategory` enum ordinal.

## Stable Lines

These lines appear in fixed positions:
- `Time` — event elapsed timer.
- `Total` — cumulative points across all categories.
- `Area` — current break radius and depth.
- `Haste` — current haste tier in Roman numerals.
- `Event Running` / `Waiting` / `Finished` — event status.

## Dynamic Lines

- All 7 category balances are shown, sorted by `lastUpdated` timestamp descending.
- Categories the player has never earned or spent appear at the bottom (timestamp = 0).

## Update Rules

1. `Time` updates every second from the event elapsed timer.
2. Point lines update immediately on every point grant or spend, re-sorting the category block.
3. `Area` updates immediately on skill purchase.
4. `Haste` updates immediately on skill purchase.
5. All numbers use comma separators for readability.

## Event State

- Before event start: `Time: Waiting`.
- During event: elapsed time counting up from `00:00:00`.
- After event end: `Time: Finished` (frozen at final elapsed).
