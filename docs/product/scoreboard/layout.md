# Scoreboard Layout

## Goal

Specify the exact sidebar lines visible to all players.

## Lines (top to bottom)

```
zenbukkowa
-------------
Time: 00:42:15
Total: 12,345
TERRA: 1,234
MINERAL: 2,345
ORGANIC: 3,456
AQUATIC: 4,567
VOID: 987
Area: 5x5x5
Haste: III
```

## Update Rules

1. `Time` updates every second from the event elapsed timer.
2. Point lines update immediately on every point grant or spend.
3. `Area` updates immediately on skill purchase.
4. `Haste` updates immediately on skill purchase.
5. All numbers use comma separators for readability.

## Event State

- Before event start: `Time: Waiting`.
- During event: elapsed time counting up from `00:00:00`.
- After event end: `Time: Finished` (frozen at final elapsed).
