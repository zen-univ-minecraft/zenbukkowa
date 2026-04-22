# Settings Menu

## Goal

Player preferences and operator event controls.

## Layout

- 54 slots (6 rows).
- Row 2:
  - Slot 10: `Language` toggle (book) — switches EN / JA
  - Slot 12: `Scoreboard` toggle (ender eye) — ON / OFF
  - Slot 14: `Start Event` (emerald block) — visible only to operators
  - Slot 16: `End Event` (redstone block) — visible only to operators
  - Slot 25: `Event Controls` label (command block) — visible only to operators
- Row 6:
  - Slot 49: `Back` (arrow) -> Root menu

## Rules

1. Language toggle switches between English and Japanese immediately.
2. Scoreboard toggle enables/disables the sidebar.
3. Event controls only appear for players with `isOp()`.
4. Non-operators see only language and scoreboard options.
