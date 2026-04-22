# Haste Aura

## Goal

Grant permanent haste while the player is online.

## Trigger

- Player join.
- Player respawn.
- HASTE_AURA skill purchase or upgrade.

## Effect

- PotionEffectType.HASTE
- Duration: 400 ticks (20 seconds), refreshed every 200 ticks (10 seconds).
- Amplifier: `tier - 1`
  - Tier I: Haste I
  - Tier V: Haste V

## Rules

1. Effect is ambient (no particles) and invisible (no icon).
2. If HASTE_AURA tier is 0, the effect is removed.
3. Refresh task runs globally for all online players.
