# Lifecycle

## Goal

Define plugin enable and disable ordering.

## Enable Sequence

1. Load `config.yml` and `skills.yml`.
2. Initialize `SqliteDatabase`.
3. Initialize DAOs.
4. Initialize domain services (inject DAOs and config).
5. Initialize GUI and scoreboard services.
6. Register event listeners.
7. Register commands.
8. Install hotbar menu item for all online players.
9. Start scoreboard refresh task.

## Disable Sequence

1. Cancel all scheduled tasks.
2. Close database connection.
3. Clear scoreboards.
