# Block Discovery

## Goal

Reward players for encountering and breaking diverse naturally occurring block types for the first time.

## Rules

1. Each player has their own discovery log.
2. The first time a player breaks a naturally occurring block type, they receive a DISCOVERY point bonus.
3. Player-placed blocks do not trigger discovery bonuses.
4. Discovery bonuses are awarded in addition to normal category points.

## Eligible Blocks

- Any `Material` that can generate naturally in a Minecraft world is eligible.
- Examples: stone, dirt, oak_log, diamond_ore, prismarine, sculk, wheat.
- Excluded: player-placed variants, admin blocks, and unobtainable blocks.

## Bonus Amounts

| Rarity | Examples | Bonus |
|---|---|---|
| Common | stone, dirt, sand, gravel | 10 DISCOVERY points |
| Uncommon | logs, planks, nylium, end stone | 25 DISCOVERY points |
| Rare | ores (except ancient debris), amethyst | 50 DISCOVERY points |
| Epic | ancient debris, spawner, trial_spawner | 200 DISCOVERY points |

## Discovery Skills

The DISCOVERY skill tree branch improves discovery bonuses:

- `CURIOUS_MINER` tier 1: Unlock DISCOVERY points.
- `GEOLOGIST`: Increases common block discovery bonus by 5 points per tier.
- `SURVEYOR`: Increases uncommon block discovery bonus by 10 points per tier.
- `CARTOGRAPHER`: Increases rare block discovery bonus by 15 points per tier.
- `PATHFINDER`: Increases epic block discovery bonus by 50 points per tier.
- `WORLD_WALKER`: All discovery bonuses multiplied by 1.1x per tier.

## Feedback

- On first break of a new block type: chat message `Discovered {BlockName}! +{N} DISCOVERY points`.
- Discovery progress is shown in the Stats menu.

## Persistence

- Discovery state is stored per-player in SQLite (`player_discoveries` table).
- Reset clears all discovery progress and bonuses.
