# Skill Descriptions

## Goal

Provide a player-readable description for every skill, grouped by category.

## Rules

1. Descriptions are shown as lore in the skill tree and as help topic text.
2. Effect descriptions state the mechanic, not just the number.
3. Prerequisite callouts are one-line and explicit.

---

## TERRA Skills

### AREA_RADIUS
- **Mechanic:** Expands the horizontal break area around the mined block.
- **Tier 1:** 1x1 (single block).
- **Tier 3:** 3x3.
- **Tier 5:** 5x5.
- **Tier 7:** 7x7.
- **Tier 9:** 9x9.
- **Tip:** Buy this first; it is the root of the TERRA branch.

### AREA_DEPTH
- **Mechanic:** Expands how many layers below the target block are broken.
- **Tier 1:** 1 layer.
- **Tier 3:** 3 layers.
- **Tier 5:** 5 layers.
- **Tier 7:** 7 layers.
- **Tier 9:** 9 layers.
- **Prerequisite:** AREA_RADIUS tier 3 for tier 3+.

### PILLAR_BREAK
- **Mechanic:** Also breaks blocks directly above the target block.
- **Tier 1:** 1 block above.
- **Tier 2:** 2 blocks above.
- **Tier 3:** 3 blocks above.
- **Prerequisite:** AREA_DEPTH tier 2.

### EFFICIENCY
- **Mechanic:** Grants Haste potion effect while mining.
- **Tier I:** Haste I.
- **Tier V:** Haste V.
- **Prerequisite:** HASTE_AURA tier 2.

### GRAVITY_WELL
- **Mechanic:** Sand and gravel in the break area fall instantly instead of dropping as items.
- **Prerequisite:** AREA_DEPTH tier 3.

### TERRA_BLESSING
- **Mechanic:** Multiplies all TERRA points earned by breaking blocks.
- **Tier 1:** +10%.
- **Tier 2:** +20%.
- **Tier 3:** +30%.
- **Prerequisite:** PILLAR_BREAK tier 1.

---

## MINERAL Skills

### HASTE_AURA
- **Mechanic:** Permanent Haste effect at all times.
- **Tier I:** Haste I.
- **Tier V:** Haste V.
- **Tip:** Buy this early; EFFICIENCY requires it.

### FORTUNE_TOUCH
- **Mechanic:** Chance to double points on any block break.
- **Tier I:** 10% chance.
- **Tier II:** 20% chance.
- **Tier III:** 30% chance.
- **Prerequisite:** HASTE_AURA tier 3 for tier 3.

### VEIN_MINER
- **Mechanic:** Extra points per ore block broken.
- **Tier 1:** +1 point.
- **Tier 2:** +2 points.
- **Tier 3:** +3 points.
- **Prerequisite:** FORTUNE_TOUCH tier 2.

### MAGNET
- **Mechanic:** Auto-collects dropped items within 5 blocks.
- **Prerequisite:** VEIN_MINER tier 1.

### CRYSTAL_VISION
- **Mechanic:** Highlights ores within a radius.
- **Tier 1:** 5 blocks.
- **Tier 2:** 10 blocks.
- **Tier 3:** 15 blocks.
- **Prerequisite:** MAGNET.

---

## ORGANIC Skills

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

---

## AQUATIC Skills

### SALVAGE
- **Mechanic:** Chance to not consume tool durability on a break.
- **Tier I:** 15% chance.
- **Tier II:** 30% chance.
- **Tier III:** 45% chance.

### TIDE_BREAKER
- **Mechanic:** Grants Water Breathing and Dolphin's Grace while underwater.

### FROST_WALKER
- **Mechanic:** Walk on water (Frost Walker effect).
- **Prerequisite:** TIDE_BREAKER.

### CONDUIT_AURA
- **Mechanic:** Grants Conduit Power while in water.
- **Tier 1:** Conduit I.
- **Tier 2:** Conduit II.
- **Tier 3:** Conduit III.
- **Prerequisite:** TIDE_BREAKER.

### DEEP_DIVE
- **Mechanic:** Multiplies AQUATIC points while underwater.
- **Tier 1:** +10%.
- **Tier 2:** +20%.
- **Tier 3:** +30%.
- **Prerequisite:** CONDUIT_AURA tier 2.

---

## VOID Skills

### VOID_SIPHON
- **Mechanic:** Bonus points for blocks at Y < 0 or in Nether/End.
- **Tier I:** +25%.
- **Tier II:** +50%.
- **Tier III:** +75%.
- **Prerequisite:** STRUCTURE_SENSE for tier 2+.

### STRUCTURE_SENSE
- **Mechanic:** Highlights nearby structure blocks with particles.

### NIGHT_VISION
- **Mechanic:** Permanent Night Vision.
- **Prerequisite:** STRUCTURE_SENSE.

### FIRE_RESISTANCE
- **Mechanic:** Permanent Fire Resistance.
- **Prerequisite:** NIGHT_VISION.

### VOID_WALK
- **Mechanic:** No fall damage, jump boost, and void death immunity at tier 3.
- **Tier 1:** No fall damage + Jump Boost I.
- **Tier 2:** Jump Boost II.
- **Tier 3:** Void death immunity + Jump Boost III.
- **Prerequisite:** VOID_SIPHON tier 2.

---

## CROP Skills

### GREEN_THUMB
- **Mechanic:** Bonus CROP points for every crop block broken.
- **Tier 1:** +1 point.
- **Tier 2:** +2 points.
- **Tier 3:** +3 points.
- **Tier 4:** +4 points.
- **Tier 5:** +5 points.
- **Tip:** Buy this to unlock the CROP branch.

### HARVEST_AURA
- **Mechanic:** Chance to preserve seeds when breaking mature crops.
- **Tier I:** 25% chance.
- **Tier II:** 50% chance.
- **Tier III:** 75% chance.
- **Prerequisite:** GREEN_THUMB.

### COMPOST_MASTER
- **Mechanic:** Auto-bonemeals nearby crop blocks.
- **Tier 1:** 2 block radius.
- **Tier 2:** 4 block radius.
- **Tier 3:** 6 block radius.
- **Prerequisite:** HARVEST_AURA.

### SEED_SATCHEL
- **Mechanic:** Automatically replants wheat, carrots, potatoes, or beetroot when the mature crop is broken.
- **Prerequisite:** COMPOST_MASTER.

### FARMERS_FORTUNE
- **Mechanic:** Multiplies all CROP points earned.
- **Tier 1:** +10%.
- **Tier 2:** +20%.
- **Tier 3:** +30%.
- **Prerequisite:** SEED_SATCHEL.
