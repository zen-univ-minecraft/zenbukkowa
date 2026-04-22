# Quickstart

## Requirements

- Docker with Docker Compose
- Java 21 (for local Gradle builds outside containers)

## Local Server Boot

1. Build the plugin:
   ```bash
   ./gradlew shadowJar
   ```
2. Start the Paper server:
   ```bash
   docker compose up
   ```
3. Connect to `localhost:25565` with a Minecraft 1.21.11 client.

## First Admin Setup

1. Join the server.
2. Run `op <your-username>` from the server console.
3. Run `/zenbukkowa start` to begin the event timer.

## Stop

```bash
docker compose down
```
