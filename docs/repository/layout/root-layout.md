# Root Layout

## Required Top-Level Files

- `README.md`
- `LICENSE`
- `build.gradle.kts`
- `settings.gradle.kts`
- `docker-compose.yml`
- `docker-compose.verify.yml`

## Required Top-Level Directories

- `docs/` (see docs layout contract)
- `src/main/java/`
- `src/main/resources/`
- `src/test/java/`
- `scripts/`

## Generated

- `build/libs/zenbukkowa-*.jar` (shadow JAR)
- `tmp/mc-data/` (Docker volume, gitignored)
