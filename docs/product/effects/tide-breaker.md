# Tide Breaker

## Goal

Grant water breathing and dolphin's grace while underwater.

## Trigger

- Player move event when Y or block changes.
- Global refresh task every 200 ticks (10 seconds).

## Effect

- PotionEffectType.WATER_BREATHING (level 0)
- PotionEffectType.DOLPHINS_GRACE (level 0)
- Duration: 400 ticks (20 seconds)

## Condition

- Player must have TIDE_BREAKER skill purchased.
- Player must be in water (`player.isInWater()`).

## Rules

1. Effects are ambient and invisible.
2. Effects are removed when the player leaves water.
3. No effect is applied if the player does not have the skill.
