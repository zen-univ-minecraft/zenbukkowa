# Scoreboard Layout

## Goal

Specify the exact sidebar lines visible to all players.

## Lines (top to bottom)

```
zenbukkowa
-------------
Time: 01:23:45
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

1. `Time` updates every second.
2. Point lines update on every point grant.
3. `Area` updates on skill purchase.
4. `Haste` updates on skill purchase.
5. All numbers use comma separators for readability.

## Event State

- Before event start: `Time: Waiting`.
- During event: countdown from configured duration.
- After event end: `Time: Finished`.
