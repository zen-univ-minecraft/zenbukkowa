# Point Categories

## Goal

Divide block-breaking rewards into five categories that must be earned in balance to maximize destruction.

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

## Point Values

- Default: `1 point` per block broken.
- Ores in MINERAL: `5 points` per block.
- Ancient debris: `20 points` per block.
- Structure bonus blocks (prismarine, sea lantern, dark oak in mansions): `2 points` per block.

## Balance Rule

- Max area skills require spending `TERRA` + `MINERAL` + `ORGANIC` + `AQUATIC` + `VOID` points in roughly equal proportion.
- A player who only mines stone cannot afford `AREA_DEPTH` past tier 2.
