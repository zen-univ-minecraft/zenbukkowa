# Local Server

## Goal

Run a local Paper 1.21.11 server with the plugin.

## Compose Service

- Image: `itzg/minecraft-server:java21`
- Type: `PAPER`
- Version: `1.21.11`
- Memory: `2G`
- Ports: `25565`, `25575` (RCON)
- Volumes:
  - `./tmp/mc-data:/data`
  - `./build/libs:/plugins:ro`

## First Boot

1. `docker compose up` downloads the server and starts it.
2. Plugin JAR is loaded from `build/libs`.
3. RCON is enabled for smoke tests.

## Ops

- Add ops via `OPS` environment variable.
- Change version via `MC_VERSION` environment variable.
