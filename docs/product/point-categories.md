# Point Categories

## Goal

Divide block-breaking rewards into seven categories that must be earned in balance to maximize destruction.

## Categories

### TERRA

- Stone, cobblestone, mossy cobblestone
- Deepslate, cobbled deepslate, tuff, calcite, dripstone
- Dirt, coarse dirt, rooted dirt, mud
- Sand, red sand, gravel
- Clay, terracotta (all colors)
- Granite, diorite, andesite

### MINERAL

- Coal ore, iron ore, copper ore, gold ore
- Redstone ore, lapis ore, diamond ore, emerald ore
- Nether quartz ore, nether gold ore
- Ancient debris
- Amethyst blocks, budding amethyst, amethyst clusters
- Raw iron block, raw copper block, raw gold block

### ORGANIC

- Logs, stripped logs, wood, stripped wood (all variants)
- Planks (all variants)
- Leaves (all variants)
- Grass block, moss block, moss carpet
- Nylium (crimson, warped)
- Wart block, shroomlight
- Hay bale, melon, pumpkin, carved pumpkin
- Bamboo, sugar cane, cactus, vines

### AQUATIC

- Prismarine, prismarine bricks, dark prismarine
- Sea lantern
- Coral blocks, coral, coral fans (all variants)
- Sponge, wet sponge
- Kelp, seagrass, sea pickle
- Clay (also TERRA)

### VOID

- Obsidian, crying obsidian
- Netherrack, soul sand, soul soil
- Blackstone, basalt, smooth basalt
- End stone, end stone bricks
- Sculk, sculk vein, sculk sensor, sculk shrieker, sculk catalyst
- Magma block, glowstone

### CROP

- Wheat, carrots, potatoes, beetroots
- Melon stem, pumpkin stem, attached stems
- Cocoa, nether wart
- Sweet berry bush, cave vines, glow berries
- Torchflower, pitcher crop
- Sugar cane (also ORGANIC), cactus (also ORGANIC)

### DISCOVERY

- Awarded the first time a player breaks any naturally occurring block type.
- Each unique `Material` that can generate naturally in the world grants a one-time bonus.
- Bonus amount scales with block rarity: common blocks = 10 points, ores = 50 points, ancient debris = 200 points.
- The DISCOVERY branch in the skill tree expands the list of blocks that grant bonuses and increases bonus amounts.

## Point Values

- Default: `1 point` per block broken.
- Ores in MINERAL: `5 points` per block.
- Ancient debris: `20 points` per block.
- Structure bonus blocks (prismarine, sea lantern, dark oak in mansions): `2 points` per block.
- Discovery first-break bonuses: `10-200 points` depending on block rarity.

## Balance Rule

- Max area skills require spending points in roughly equal proportion across all seven categories.
- A player who only mines stone cannot afford `AREA_DEPTH` past tier 2.
