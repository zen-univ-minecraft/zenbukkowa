# Skill Tree

## Goal

Define the visual tree layout, scroll behavior, and node coordinates for the skills menu.

## Rules

1. The tree grows bottom-to-top: roots at the bottom, advanced skills higher.
2. TERRA is the central trunk; all other categories branch from it.
3. Parent-child skills are connected by `GREEN_STAINED_GLASS_PANE` items.
4. The viewport is a 54-slot inventory showing 5 rows of a larger virtual grid.
5. Scroll arrows move the viewport up or down one row at a time.

---

## Virtual Grid

- **Width:** 9 columns.
- **Height:** 12 rows (rows 0–11).
- **Viewport:** 5 rows tall (rows visible at once).
- **Max scroll offset:** 7 (offset = first visible row; shows rows 7–11 at default offset).

---

## Branch Layout

- **TERRA trunk:** column 4, rows 7–11.
- **MINERAL branch:** left side, column 2, rows 5–10.
- **ORGANIC branch:** left side, column 1, rows 3–8.
- **AQUATIC branch:** right side, column 6, rows 4–9.
- **VOID branch:** right side, column 7, rows 2–7.

---

## Node Coordinates (bottom-to-top)

### TERRA Trunk (column 4)

| Skill | Row | Col | Parent |
|---|---|---|---|
| AREA_RADIUS | 11 | 4 | — |
| AREA_DEPTH | 9 | 4 | AREA_RADIUS |
| PILLAR_BREAK | 7 | 4 | AREA_DEPTH |
| TERRA_BLESSING | 5 | 4 | PILLAR_BREAK |

### MINERAL Branch (column 2)

| Skill | Row | Col | Parent |
|---|---|---|---|
| HASTE_AURA | 10 | 2 | — (requires AREA_RADIUS) |
| FORTUNE_TOUCH | 8 | 2 | HASTE_AURA |
| VEIN_MINER | 6 | 2 | FORTUNE_TOUCH |
| MAGNET | 4 | 2 | VEIN_MINER |
| CRYSTAL_VISION | 2 | 2 | MAGNET |

### ORGANIC Branch (column 1)

| Skill | Row | Col | Parent |
|---|---|---|---|
| LEAF_CONSUME | 8 | 1 | — (requires AREA_RADIUS) |
| ROOT_RAZE | 6 | 1 | LEAF_CONSUME |
| SAPLING_REPLANT | 4 | 1 | ROOT_RAZE |
| NATURE_TOUCH | 2 | 1 | SAPLING_REPLANT |

### AQUATIC Branch (column 6)

| Skill | Row | Col | Parent |
|---|---|---|---|
| TIDE_BREAKER | 9 | 6 | — (requires AREA_RADIUS) |
| SALVAGE | 7 | 6 | — (requires AREA_RADIUS) |
| CONDUIT_AURA | 5 | 6 | TIDE_BREAKER |
| FROST_WALKER | 3 | 6 | TIDE_BREAKER |
| DEEP_DIVE | 1 | 6 | CONDUIT_AURA |

### VOID Branch (column 7)

| Skill | Row | Col | Parent |
|---|---|---|---|
| VOID_SIPHON | 7 | 7 | — (requires AREA_RADIUS) |
| STRUCTURE_SENSE | 5 | 7 | — (requires AREA_RADIUS) |
| NIGHT_VISION | 3 | 7 | STRUCTURE_SENSE |
| FIRE_RESISTANCE | 1 | 7 | NIGHT_VISION |

### Cross-Branch

| Skill | Row | Col | Parent |
|---|---|---|---|
| EFFICIENCY | 8 | 3 | HASTE_AURA |
| GRAVITY_WELL | 6 | 4 | AREA_DEPTH |
| BONEMEAL_AURA | 2 | 1 | SAPLING_REPLANT |
| VOID_WALK | 3 | 5 | VOID_SIPHON |

---

## Connection Rendering

- A `GREEN_STAINED_GLASS_PANE` is placed in every grid cell between a parent and child node.
- Panes have no lore and a gray name (` `) so they are not interactive.
- Horizontal connections (cross-branch) use `GREEN_STAINED_GLASS_PANE` as well.

---

## Scroll Contract

### Controls
- **Slot 45:** Scroll up (show higher rows). Arrow item.
- **Slot 53:** Scroll down (show lower rows). Arrow item.
- **Slot 49:** Back to Root menu.
- **Slot 46:** Page indicator (`Page X / Y`).

### State
- Scroll offset is stored per-player in `MenuService`.
- Default offset is `7` (bottom of tree visible).
- Up-arrow is disabled when offset == 0.
- Down-arrow is disabled when offset == max.

### Interaction
1. Clicking a skill node attempts purchase of the next tier.
2. Clicking a connection pane does nothing.
3. Clicking scroll arrows updates the offset and re-renders.
