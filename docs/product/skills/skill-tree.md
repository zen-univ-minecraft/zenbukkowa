# Skill Tree

## Goal

Define the visual tree layout, 2D scroll behavior, and node coordinates for the skills menu.

## Rules

1. The tree grows bottom-to-top: roots at the bottom, advanced skills higher.
2. TERRA is the central trunk; all other categories branch from it.
3. Parent-child skills are connected by `GREEN_STAINED_GLASS_PANE` items.
4. The viewport is a 54-slot inventory showing a 7x5 slice of a larger virtual grid.
5. Scroll arrows move the viewport one row or one column at a time.
6. Empty separator columns exist between branches for visual clarity.

---

## Virtual Grid

- **Width:** 20 columns (0-19).
- **Height:** 13 rows (0-12).
- **Viewport:** 9 columns wide x 5 rows tall.
- **Max vertical scroll:** 8 (`GRID_ROWS - VIEWPORT_ROWS`).
- **Max horizontal scroll:** 13 (`GRID_COLS - VIEWPORT_COLS`).

---

## Node Coordinate Generation

Coordinates are **derived at runtime** from the relationship tree. `PARENTS` is the single source of truth.

### Branch Columns

| Branch | Base Column |
|---|---|
| CROP | 0 |
| ORGANIC | 3 |
| MINERAL | 6 |
| TERRA | 9 |
| AQUATIC | 12 |
| VOID | 15 |
| DISCOVERY | 18 |

### Column Overrides

| Skill | Column |
|---|---|
| SEED_SATCHEL | 1 |
| FROST_WALKER | 13 |
| SALVAGE | 14 |
| EFFICIENCY | 10 |
| STRUCTURE_SENSE | 17 |

### Mythic Positions

| Skill | Row | Col |
|---|---|---|
| TITAN_STRIKE | 0 | 8 |
| ANGEL_WINGS | 0 | 10 |

### Row Generation Algorithm

1. Group skills by resolved column (branch base or override).
2. Within each column, topologically sort by parent chain.
3. When a node has multiple children in the same column, the child with the longer descendant chain is placed lower (larger row number) to leave room.
4. Assign rows bottom-to-top, decrementing by 2 per node.
   - TERRA trunk base row = 12
   - All other branches base row = 11

### Roots

All roots unlock independently with their own category points and sit at the bottom of their branch:
- `GREEN_THUMB` (CROP)
- `LEAF_CONSUME` (ORGANIC)
- `HASTE_AURA` (MINERAL)
- `AREA_RADIUS` (TERRA trunk, row 12)
- `TIDE_BREAKER` (AQUATIC)
- `SALVAGE` (AQUATIC)
- `VOID_SIPHON` (VOID)
- `STRUCTURE_SENSE` (VOID)
- `CURIOUS_MINER` (DISCOVERY)

---

## Connection Rendering

- A `GREEN_STAINED_GLASS_PANE` is placed in every grid cell between a parent and child node.
- Panes have no lore and a blank name (` `) so they are not interactive.
- Horizontal connections (cross-branch) use `GREEN_STAINED_GLASS_PANE` as well.
- When a connection cell overlaps with a node cell, the node takes precedence.
- **No connection path may pass through an unrelated node.** This is a hard layout invariant.

---

## 2D Scroll Contract

### Controls
- **Slot 43:** Scroll up (show higher rows). Arrow item — `W`.
- **Slot 51:** Scroll left (show lower columns). Arrow item — `A`.
- **Slot 52:** Scroll down (show lower rows). Arrow item — `S`.
- **Slot 53:** Scroll right (show higher columns). Arrow item — `D`.
- **Slot 45:** Back to Root menu.
- **Slot 46:** Page indicator (`V X / Y . H A / B`).

### State
- Scroll offsets are stored per-player in `MenuService`.
- Default vertical offset is `8` (bottom of tree visible; rows 8-12).
- Default horizontal offset is `0` (leftmost columns visible; cols 0-8).
- Up-arrow is disabled when vertical offset == 0.
- Down-arrow is disabled when vertical offset == max.
- Left-arrow is disabled when horizontal offset == 0.
- Right-arrow is disabled when horizontal offset == max.
- **Scroll state persists across inventory transitions and after close.**
- It is reset to default only when the Skills menu is opened from the Root menu.

### Interaction
1. Clicking a skill node attempts purchase of the next tier.
2. Clicking a connection pane does nothing.
3. Clicking scroll arrows updates the offset and re-renders.
