# Bonemeal Aura

## Goal

Automatically apply bonemeal to growable blocks near players who own BONEMEAL_AURA or COMPOST_MASTER.

## Trigger

- Periodic task runs every 5 seconds (100 ticks) for all online players.

## Effect

- For each player with `BONEMEAL_AURA` or `COMPOST_MASTER`, scan a cubic radius around the player.
- Radius = `tier` blocks horizontally, ±2 vertically.
- Every crop block inside the radius receives one bonemeal application.

## Crop Blocks

- Wheat, carrots, potatoes, beetroots, cocoa, melon stems, pumpkin stems
- Nether wart, torchflower crop, pitcher crop

## Rules

1. Bonemeal is applied via `Block#applyBoneMeal(BlockFace.UP)`.
2. Both skills stack independently; the larger radius is used.
3. No particles or sounds are emitted beyond normal bonemeal effects.
4. The task runs on the main server thread.
