# Purchase Rules

## Goal

Define how players buy skills with points, including costs, prerequisites, and reset policy.

## Rules

1. Each category has its own point balance.
2. Normal skill purchases consume points from the skill's own category.
3. Mythic skill purchases consume points from multiple categories at once.
4. Cross-category bundles do not exist for normal skills.
5. Purchases are final; no refunds.
6. Prerequisites must be checked **before** points are consumed.

---

## Cost Formula

- Base cost for tier N of a normal skill: `10 * N * N` points of that category.
- Example: `AREA_RADIUS` tier 1 costs `10 TERRA` points.
- Example: `AREA_RADIUS` tier 3 costs `90 TERRA` points.
- Example: `HASTE_AURA` tier V costs `250 MINERAL` points.
- Cross-category skills cost their primary category **plus** one secondary category at the same rate.
- Example: `EFFICIENCY` tier 2 costs `40 TERRA + 40 MINERAL`.

## Tier Order

- Tiers must be purchased in ascending order.
- A player cannot skip from tier 1 to tier 3.

---

## Unlock Gating

### Branch Roots

Each branch root unlocks independently with its own category points. No `AREA_RADIUS` requirement.
- `HASTE_AURA` unlocks with `MINERAL` points.
- `LEAF_CONSUME` unlocks with `ORGANIC` points.
- `TIDE_BREAKER` unlocks with `AQUATIC + TERRA` points.
- `SALVAGE` unlocks with `AQUATIC` points.
- `VOID_SIPHON` unlocks with `VOID` points.
- `STRUCTURE_SENSE` unlocks with `VOID` points.
- `GREEN_THUMB` unlocks with `CROP` points.
- `CURIOUS_MINER` unlocks with `DISCOVERY` points.

### TERRA
- `AREA_DEPTH` tier 3+ requires `AREA_RADIUS` tier 3+.
- `PILLAR_BREAK` requires `AREA_DEPTH` tier 2+.
- `GRAVITY_WELL` requires `AREA_DEPTH` tier 3+.
- `EFFICIENCY` requires `HASTE_AURA` tier 2+.
- `TERRA_BLESSING` requires `PILLAR_BREAK`.

### MINERAL
- `FORTUNE_TOUCH` tier 3 requires `HASTE_AURA` tier 3.
- `VEIN_MINER` requires `FORTUNE_TOUCH` tier 2.
- `MAGNET` requires `VEIN_MINER`.
- `CRYSTAL_VISION` requires `MAGNET`.
- `BLAST_MINING` requires `CRYSTAL_VISION`.

### ORGANIC
- `ROOT_RAZE` requires `LEAF_CONSUME`.
- `SAPLING_REPLANT` requires `ROOT_RAZE`.
- `BONEMEAL_AURA` requires `SAPLING_REPLANT`.
- `NATURE_TOUCH` requires `BONEMEAL_AURA` tier 2.
- `WILD_GROWTH` requires `NATURE_TOUCH`.

### AQUATIC
- `FROST_WALKER` requires `TIDE_BREAKER`.
- `CONDUIT_AURA` requires `TIDE_BREAKER`.
- `DEEP_DIVE` requires `CONDUIT_AURA` tier 2.
- `TSUNAMI` requires `DEEP_DIVE`.

### VOID
- `VOID_SIPHON` tier 2+ requires `STRUCTURE_SENSE`.
- `NIGHT_VISION` requires `STRUCTURE_SENSE`.
- `FIRE_RESISTANCE` requires `NIGHT_VISION`.
- `VOID_WALK` requires `VOID_SIPHON` tier 2.
- `VOID_RIFT` requires `VOID_WALK`.

### CROP
- `HARVEST_AURA` requires `GREEN_THUMB`.
- `COMPOST_MASTER` requires `HARVEST_AURA`.
- `SEED_SATCHEL` requires `COMPOST_MASTER`.
- `FARMERS_FORTUNE` requires `SEED_SATCHEL`.
- `HARVEST_WAVE` requires `FARMERS_FORTUNE`.

### DISCOVERY
- `GEOLOGIST` requires `CURIOUS_MINER`.
- `SURVEYOR` requires `GEOLOGIST`.
- `CARTOGRAPHER` requires `SURVEYOR`.
- `PATHFINDER` requires `CARTOGRAPHER`.
- `WORLD_WALKER` requires `PATHFINDER`.

---

## Mythic Skills

### Cost Structure

- Mythic skills consume points from **multiple categories simultaneously**.
- Mythic cost per category per tier: `100 * N * N`.
- Example: `ANGEL_WINGS` tier 1 costs `100 TERRA + 100 MINERAL + 100 VOID`.

### Prerequisites

- Mythic skills require high-tier normal skills across multiple domains.
- `ANGEL_WINGS` requires `AREA_RADIUS` tier 3, `HASTE_AURA` tier 3, and `VOID_SIPHON` tier 2.
- `TITAN_STRIKE` requires `AREA_RADIUS` tier 3, `NATURE_TOUCH` tier 2, and `FARMERS_FORTUNE` tier 2.
- Prerequisites still enforce branch-internal chains even though roots are independent.

### Display

- Mythic skill lore lists every required category and cost.
- Example: `Cost: 100 TERRA + 100 MINERAL + 100 VOID`.
- Prerequisite skills are listed by name.

---

## Prerequisite Display

- Every skill node in the tree must list its immediate prerequisite skills by human-readable name in the item lore.
- The prerequisite line is shown regardless of whether the prerequisite is met.
- Example: `Prerequisite: AREA_DEPTH tier 2`.

---

## Feedback

- On success: menu updates immediately, player hears level-up sound.
- On insufficient points: red "Insufficient {Category} Points" message.
- On unmet prerequisite: red "Missing prerequisite" message.
- **Points must never be consumed if the prerequisite is unmet.**

---

## Reset Policy

- Operators can reset individual player data or all player data.
- Reset clears points, skills, break logs, placed blocks, and discovery logs for the target player(s).
- Reset does not clear structure claims or server settings.
- There is no player-initiated self-reset.
