# zenbukkowa

PaperMC 1.21.11 event plugin: break blocks to earn multi-category points, spend points to unlock skills that expand your break area and speed, and compete for the highest cumulative score.

Documentation canon lives in [docs/README.md](docs/README.md).  
Contracts are docs-first: update docs before code.

## Quick Start

```bash
./gradlew shadowJar
docker compose up
```

Connect to `localhost:25565` with a Minecraft 1.21.11 client.

## Verification

```bash
docker compose -f docker-compose.verify.yml up --build
```
