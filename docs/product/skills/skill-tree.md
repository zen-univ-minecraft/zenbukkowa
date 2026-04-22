# Skill Tree

## Goal

Define the visual tree layout, scroll behavior, and node coordinates for the skills menu.

## Rules

1. The tree grows bottom-to-top: roots at the bottom, advanced skills higher.
2. Each category forms a vertical branch.
3. Parent-child skills are connected by vertical `STICK` items.
4. The viewport is a 54-slot inventory showing 5 rows of a larger virtual grid.
5. Scroll arrows move the viewport up or down one row at a time.

---

## Virtual Grid

- **Width:** 9 columns.
- **Height:** 18 rows (rows 0–17).
- **Viewport:** 5 rows tall (rows visible at once).
- **Max scroll offset:** 13 (offset = first visible row index; shows rows 13–17 at default offset).

---

## Branch Column Map

| Category | Column(s) |
|---|---|
| TERRA | 0–1 |
| MINERAL | 2–3 |
| ORGANIC | 4 |
| AQUATIC | 5–6 |
| VOID | 7–8 |

---

## Node Coordinates (bottom-to-top)

### TERRA Branch (column 0)

| Skill | Row | Col | Parent |
|---|---|---|---|
| AREA_RADIUS | 16 | 0 | — |
| AREA_DEPTH | 13 | 0 | AREA_RADIUS |
| PILLAR_BREAK | 10 | 0 | AREA_DEPTH |
| GRAVITY_WELL | 7 | 0 | AREA_DEPTH |
| TERRA_BLESSING | 4 | 0 | PILLAR_BREAK |
| EFFICIENCY | 8 | 1 | HASTE_AURA (cross-branch) |

### MINERAL Branch (column 2)

| Skill | Row | Col | Parent |
|---|---|---|---|
| HASTE_AURA | 16 | 2 | — |
| FORTUNE_TOUCH | 13 | 2 | HASTE_AURA |
| VEIN_MINER | 10 | 2 | FORTUNE_TOUCH |
| MAGNET | 7 | 2 | VEIN_MINER |
| CRYSTAL_VISION | 4 | 2 | MAGNET |

### ORGANIC Branch (column 4)

| Skill | Row | Col | Parent |
|---|---|---|---|
| LEAF_CONSUME | 16 | 4 | — |
| ROOT_RAZE | 13 | 4 | LEAF_CONSUME |
| SAPLING_REPLANT | 10 | 4 | ROOT_RAZE |
| BONEMEAL_AURA | 7 | 4 | SAPLING_REPLANT |
| NATURE_TOUCH | 4 | 4 | BONEMEAL_AURA |

### AQUATIC Branch (column 5–6)

| Skill | Row | Col | Parent |
|---|---|---|---|
| SALVAGE | 16 | 5 | — |
| TIDE_BREAKER | 16 | 6 | — |
| CONDUIT_AURA | 13 | 5 | TIDE_BREAKER |
| FROST_WALKER | 13 | 6 | TIDE_BREAKER |
| DEEP_DIVE | 10 | 5 | CONDUIT_AURA |

### VOID Branch (column 7–8)

| Skill | Row | Col | Parent |
|---|---|---|---|
| VOID_SIPHON | 16 | 7 | — |
| STRUCTURE_SENSE | 16 | 8 | — |
| NIGHT_VISION | 13 | 8 | STRUCTURE_SENSE |
| FIRE_RESISTANCE | 10 | 8 | NIGHT_VISION |
| VOID_WALK | 7 | 7 | VOID_SIPHON |

---

## Connection Rendering

- A `STICK` item is placed in every grid cell between a parent and child node.
- Sticks have no lore and a gray name (` `) so they are not interactive.
- Horizontal connections (cross-branch) use `STICK` as well.

---

## Scroll Contract

### Controls
- **Slot 45:** Scroll up (show higher rows). Arrow item.
- **Slot 53:** Scroll down (show lower rows). Arrow item.
- **Slot 49:** Back to Root menu.
- **Slot 46:** Page indicator (`Page X / Y`).

### State
- Scroll offset is stored per-player in `MenuService`.
- Default offset is `13` (bottom of tree visible).
- Up-arrow is disabled when offset == 0.
- Down-arrow is disabled when offset == max.

### Interaction
1. Clicking a skill node attempts purchase of the next tier.
2. Clicking a stick does nothing.
3. Clicking scroll arrows updates the offset and re-renders.
