# Skill Tree

## Goal

Define the visual tree layout, 2D scroll behavior, and node coordinates for the skills menu.

## Rules

1. The tree grows bottom-to-top: roots at the bottom, advanced skills higher.
2. Each category branch occupies its own contiguous column pair; no skill bleeds into a neighbor's territory.
3. Parent-child skills are connected by `GREEN_STAINED_GLASS_PANE` items.
4. The viewport is a 54-slot inventory showing a **7×5** slice of a larger virtual grid.
5. Scroll arrows move the viewport one row or one column at a time.
6. Empty cells inside the tree's bounding box use `LIGHT_GRAY_STAINED_GLASS_PANE` to distinguish them from out-of-bounds cells, which use `GRAY_STAINED_GLASS_PANE`.

## Implementation Status

| Feature | Status |
|---|---|
| Runtime coordinate generation from PARENTS | ✅ Live |
| 2-column branch layout with separators | ✅ Live |
| L-shaped cross-column connections | ✅ Live |
| Bounding-box empty-cell differentiation | ✅ Live |
| Horizontal arrow row (slots 45–50) | ✅ Live |
| Green/Gray/LightGray filler materials | ✅ Live |

---

## Virtual Grid

- **Width:** 20 columns (0–19).
- **Height:** 13 rows (0–12).
- **Viewport:** 7 columns wide × 5 rows tall.
- **Max vertical scroll:** 8 (`GRID_ROWS - VIEWPORT_ROWS`).
- **Max horizontal scroll:** 13 (`GRID_COLS - VIEWPORT_COLS`).

---

## Node Coordinate Generation

Coordinates are **derived at runtime** from the relationship tree. `PARENTS` is the single source of truth.

### Branch Columns

Each branch has a main column and an overflow column inside the same branch boundary.

| Branch | Main | Overflow |
|---|---|---|
| CROP | 0 | 1 |
| ORGANIC | 3 | 4 |
| MINERAL | 6 | 7 |
| TERRA | 9 | 10 |
| AQUATIC | 12 | 13 |
| VOID | 15 | 16 |
| DISCOVERY | 18 | 19 |

### Column Overrides

Overflow skills are placed inside their own branch boundary, never in a separator.

| Skill | Column | Branch |
|---|---|---|
| SEED_SATCHEL | 1 | CROP overflow |
| EFFICIENCY | 7 | MINERAL overflow |
| FROST_WALKER | 13 | AQUATIC overflow |
| SALVAGE | 13 | AQUATIC overflow |
| STRUCTURE_SENSE | 16 | VOID overflow |

### Mythic Positions

Mythic skills sit at row 0 inside separator columns so they never overlap branch connections.

| Skill | Row | Col |
|---|---|---|
| TITAN_STRIKE | 0 | 8 |
| ANGEL_WINGS | 0 | 11 |

### Row Generation Algorithm

1. Group skills by resolved column (branch main or overflow).
2. Within each column, topologically sort by parent chain.
3. When a node has multiple children in the same column, the child with the longer descendant chain is placed lower (larger row number) to leave room.
4. Assign rows bottom-to-top, decrementing by 2 per node.
   - All roots (including TERRA trunk) base row = 12

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

## Bounding-Box Visualization

`SkillTreeViewport` computes the axis-aligned bounding box of all nodes and connections at class-load time.

- **Inside bounding box, no node or connection:** `LIGHT_GRAY_STAINED_GLASS_PANE` — tells the player "this is part of the tree, but nothing is here."
- **Outside bounding box:** `GRAY_STAINED_GLASS_PANE` — tells the player "you have scrolled past the tree."
- **Connection:** `GREEN_STAINED_GLASS_PANE` — standard tree edge.

---

## 2D Scroll Contract

### Controls

All navigation items are in row 5 (the bottom inventory row), fully outside the 5-row viewport.

| Slot | Item | Direction |
|---|---|---|
| 45 | Back arrow | → Root menu |
| 46 | Page indicator | `V X / Y . H A / B` |
| 47 | Left arrow | lower columns |
| 48 | Up arrow | higher rows |
| 49 | Down arrow | lower rows |
| 50 | Right arrow | higher columns |

### State

- Scroll offsets are stored per-player in `MenuService`.
- Default vertical offset is `8` (bottom of tree visible; rows 8–12).
- Default horizontal offset is `0` (leftmost columns visible; cols 0–6).
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
