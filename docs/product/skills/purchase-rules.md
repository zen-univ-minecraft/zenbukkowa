# Purchase Rules

## Goal

Define how players buy skills with points.

## Currency Rule

- Each category has its own point balance.
- Skill purchases consume points from the skill's own category.
- Cross-category bundles do not exist.

## Cost Formula

- Base cost for tier N of a skill: `100 * N` points of that category.
- Example: AREA_RADIUS tier 3 costs `300 TERRA` points.
- Example: HASTE_AURA tier II costs `200 MINERAL` points.

## Tier Order

- Tiers must be purchased in ascending order.
- A player cannot skip from tier 1 to tier 5.

## Unlock Gating

- `AREA_DEPTH` tier 3+ requires `AREA_RADIUS` at least tier 3.
- `VOID_SIPHON` tier 2+ requires `STRUCTURE_SENSE` purchased.
- `FORTUNE_TOUCH` tier 3 requires `HASTE_AURA` tier III.

## Feedback

- On success: menu updates immediately, player hears level-up sound.
- On insufficient points: red "Insufficient {Category} Points" message.
- On unmet prerequisite: red "Requires {Skill} tier {N}" message.
- Purchases are final; no refunds.
