# Structure Bonuses

## Goal

Grant one-time large point bonuses for destroying significant structures.

## Eligible Structures

### Ocean Monument

- Trigger: breaking any prismarine, sea lantern, sponge, or gold block within 50 blocks of a monument center.
- Condition: `>= 80%` of eligible blocks in that radius are destroyed.
- Reward: `5000` points split equally (`1000` per category).
- Once per monument per server.

### Woodland Mansion

- Trigger: breaking any dark oak log, plank, stair, or carpet within the mansion bounding box.
- Condition: `>= 80%` of eligible blocks in the bounding box are destroyed.
- Reward: `5000` points split equally (`1000` per category).
- Once per mansion per server.

## Detection

- On first eligible block break near a known structure, scan the region.
- If condition met, broadcast server-wide message with player name and structure type.
- Mark structure as `claimed` in database to prevent duplicate bonuses.

## Feedback

- Player receives title: `Structure Destroyed!`
- Subtitle: `+5000 Points`
- Particles and sound at structure center.
