# Category Profiles

## Goal

Explain the thematic identity of each point category, which blocks map to it, and what kinds of skills it unlocks.

## Rules

1. One profile per category.
2. Each profile states the gameplay identity, typical biomes, and skill theme.
3. Cross-reference [point-categories.md](../point-categories.md) for the full block list.

---

## TERRA — Earth and Stone

### Identity
TERRA is the foundation category. Every miner starts here because stone and dirt are everywhere.

### Typical Blocks
Stone, cobblestone, deepslate, tuff, dirt, sand, gravel, granite, terracotta.

### Skill Theme
- **Area expansion:** AREA_RADIUS and AREA_DEPTH define how large a single break becomes.
- **Vertical utility:** PILLAR_BREAK and GRAVITY_WELL handle columns and falling blocks.
- **Point multiplier:** TERRA_BLESSING scales all TERRA income.

### Strategy Note
TERRA is the cheapest branch to enter (first tier costs 10 TERRA points). It no longer gates other branches.

---

## MINERAL — Ores and Crystals

### Identity
MINERAL rewards targeting high-value blocks. It is the economy category.

### Typical Blocks
Coal, iron, copper, gold, redstone, lapis, diamond, emerald, ancient debris, amethyst.

### Skill Theme
- **Speed:** HASTE_AURA and EFFICIENCY make mining faster.
- **Luck:** FORTUNE_TOUCH doubles points by chance.
- **Precision:** VEIN_MINER and CRYSTAL_VISION reward ore-focused paths.
- **Quality of life:** MAGNET collects drops automatically.
- **Chain reaction:** BLAST_MINING expands break radius when mining ores.

### Strategy Note
Ancient debris gives 20 points per block. Combining VEIN_MINER with FORTUNE_TOUCH turns Nether mining into a point fountain. BLAST_MINING makes ore veins collapse in larger bursts.

---

## ORGANIC — Flora and Wood

### Identity
ORGANIC rewards clearing forests, farms, and fungal zones. It is the sustainability category.

### Typical Blocks
All logs, planks, leaves, grass, moss, nylium, wart blocks, crops, bamboo.

### Skill Theme
- **Inclusion:** LEAF_CONSUME lets area breaks destroy leaves.
- **Automation:** ROOT_RAZE, SAPLING_REPLANT, and BONEMEAL_AURA reduce manual labour.
- **Multiplier:** NATURE_TOUCH scales ORGANIC income.
- **Area expansion:** WILD_GROWTH expands break radius when clearing organic blocks.

### Strategy Note
ORGANIC is strongest in forest biomes and warped/crimson forests. The auto-replant chain makes tree farms self-sustaining. WILD_GROWTH turns single log breaks into forest-clearing waves.

---

## AQUATIC — Ocean and Water

### Identity
AQUATIC rewards underwater exploration and prismarine structures. It is the mobility category.

### Typical Blocks
Prismarine, sea lantern, coral, sponge, kelp, clay.

### Skill Theme
- **Survival:** TIDE_BREAKER and CONDUIT_AURA let you work underwater indefinitely.
- **Mobility:** FROST_WALKER creates water-walking paths.
- **Durability:** SALVAGE preserves tools.
- **Multiplier:** DEEP_DIVE scales AQUATIC income while submerged.
- **Area expansion:** TSUNAMI expands break radius when breaking aquatic blocks underwater.

### Strategy Note
Ocean Monuments are the highest-density AQUATIC source. TIDE_BREAKER + CONDUIT_AURA makes monument clearing viable without air pockets. TSUNAMI turns underwater mining into demolition.

---

## VOID — Nether, End, and Deep Dark

### Identity
VOID rewards dangerous dimensions and deep layers. It is the risk category.

### Typical Blocks
Obsidian, netherrack, blackstone, basalt, end stone, sculk, magma, glowstone.

### Skill Theme
- **Exploration:** STRUCTURE_SENSE finds rare architecture.
- **Resilience:** NIGHT_VISION, FIRE_RESISTANCE, and VOID_WALK remove environmental threats.
- **Multiplier:** VOID_SIPHON scales VOID income in dimensions and deep caves.
- **Area expansion:** VOID_RIFT expands break radius when breaking void blocks in dimensions or deep caves.

### Strategy Note
VOID is the slowest category to build because the blocks are scarcer or harder to reach, but VOID_SIPHON turns Nether/End clearing into massive point gains. VOID_RIFT makes deep-slate strip mining devastating. Branch roots are independent — a player can start in VOID immediately.

---

## CROP — Cultivation and Harvest

### Identity
CROP rewards tending and harvesting farmland. It is the patience category.

### Typical Blocks
Wheat, carrots, potatoes, beetroots, melon stems, pumpkin stems, cocoa, nether wart, sweet berries.

### Skill Theme
- **Base income:** GREEN_THUMB increases points per crop block.
- **Preservation:** HARVEST_AURA reduces seed loss.
- **Automation:** COMPOST_MASTER and SEED_SATCHEL reduce replanting labour.
- **Multiplier:** FARMERS_FORTUNE scales all CROP income.
- **Area expansion:** HARVEST_WAVE expands break radius when breaking crop blocks.

### Strategy Note
CROP builds slowly but becomes self-sustaining once SEED_SATCHEL and COMPOST_MASTER are unlocked. Large farms near water generate steady income. HARVEST_WAVE turns a single wheat break into a field harvest.
