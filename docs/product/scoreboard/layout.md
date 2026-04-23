# Scoreboard Layout

## Goal

Specify the exact sidebar lines visible to all players, rotating across pages every 10 seconds.

## Pages

The scoreboard cycles through 3 pages globally every 10 seconds (200 ticks).

### Page 1 (0–9s)

```
zenbukkowa
-------------
Time: 00:42:15
Total: 12,345
TERRA: 1,234
MINERAL: 2,345
ORGANIC: 3,456
Area: 5x5x5
Haste: III
-------------
```

### Page 2 (10–19s)

```
zenbukkowa
-------------
Time: 00:42:15
Total: 12,345
AQUATIC: 4,567
VOID: 987
CROP: 654
Area: 5x5x5
Haste: III
-------------
```

### Page 3 (20–29s)

```
zenbukkowa
-------------
Time: 00:42:15
Total: 12,345
Best: ORGANIC
Status: Running
Area: 5x5x5
Haste: III
-------------
```

## Stable Lines

These lines appear on every page:
- `Time` — event elapsed timer.
- `Total` — cumulative points across all categories.
- `Area` — current break radius and depth.
- `Haste` — current haste tier in Roman numerals.

## Rotating Lines

- **Page 1:** TERRA, MINERAL, ORGANIC point balances.
- **Page 2:** AQUATIC, VOID, CROP point balances.
- **Page 3:** Best category (highest balance) and event status.

## Update Rules

1. `Time` updates every second from the event elapsed timer.
2. Point lines update immediately on every point grant or spend.
3. `Area` updates immediately on skill purchase.
4. `Haste` updates immediately on skill purchase.
5. Page rotation happens globally; all players see the same page at the same time.
6. All numbers use comma separators for readability.

## Event State

- Before event start: `Time: Waiting`.
- During event: elapsed time counting up from `00:00:00`.
- After event end: `Time: Finished` (frozen at final elapsed).
