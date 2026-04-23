# ORGANIC Skills

### LEAF_CONSUME
- **Mechanic:** Leaves are included in area breaks instead of being skipped.

### ROOT_RAZE
- **Mechanic:** Breaking a log also breaks logs directly below it (up to 20).
- **Prerequisite:** LEAF_CONSUME.

### SAPLING_REPLANT
- **Mechanic:** Automatically replants a sapling when a tree is fully broken.
- **Prerequisite:** ROOT_RAZE.

### BONEMEAL_AURA
- **Mechanic:** Auto-bonemeals crops in a radius.
- **Tier 1:** 3 blocks.
- **Tier 2:** 5 blocks.
- **Tier 3:** 7 blocks.
- **Prerequisite:** SAPLING_REPLANT.

### NATURE_TOUCH
- **Mechanic:** Multiplies all ORGANIC points earned.
- **Tier 1:** +10%.
- **Tier 2:** +20%.
- **Tier 3:** +30%.
- **Prerequisite:** BONEMEAL_AURA tier 2.

### WILD_GROWTH
- **Mechanic:** When breaking organic blocks, expands the break radius by bonus tiers.
- **Tier 1:** +1 bonus radius.
- **Tier 2:** +2 bonus radius.
- **Tier 3:** +3 bonus radius.
- **Prerequisite:** NATURE_TOUCH.
