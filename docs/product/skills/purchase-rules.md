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

- Base cost for tier N of a normal skill: `50 * N * N` points of that category.
- Example: `AREA_RADIUS` tier 1 costs `50 TERRA` points.
- Example: `AREA_RADIUS` tier 3 costs `450 TERRA` points.
- Example: `HASTE_AURA` tier V costs `1250 MINERAL` points.

## Tier Order

- Tiers must be purchased in ascending order.
- A player cannot skip from tier 1 to tier 3.

---

## Unlock Gating

### Cross-Category Roots

All base skills in non-TERRA categories require `AREA_RADIUS` tier 1 or higher:
- `HASTE_AURA` requires `AREA_RADIUS`.
- `LEAF_CONSUME` requires `AREA_RADIUS`.
- `TIDE_BREAKER` requires `AREA_RADIUS`.
- `SALVAGE` requires `AREA_RADIUS`.
- `VOID_SIPHON` requires `AREA_RADIUS`.
- `STRUCTURE_SENSE` requires `AREA_RADIUS`.
- `GREEN_THUMB` requires `AREA_RADIUS`.
- `CURIOUS_MINER` requires `AREA_RADIUS`.

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
- The cost is fixed per tier and is NOT computed by `50 * N * N`.
- Example: `ANGEL_WINGS` tier 1 costs `500 TERRA + 500 MINERAL + 500 VOID`.

### Prerequisites

- Mythic skills require high-tier normal skills across multiple domains.
- `ANGEL_WINGS` requires `AREA_RADIUS` tier 3, `HASTE_AURA` tier 3, and `VOID_SIPHON` tier 2.
- `TITAN_STRIKE` requires `AREA_RADIUS` tier 3, `NATURE_TOUCH` tier 2, and `FARMERS_FORTUNE` tier 2.

### Display

- Mythic skill lore lists every required category and cost.
- Example: `Cost: 500 TERRA + 500 MINERAL + 500 VOID`.
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
