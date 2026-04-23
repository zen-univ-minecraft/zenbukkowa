# Architecture

## Goal

Define module boundaries, persistence contracts, and runtime rules.

## Rules

1. Runtime composition, domain policies, and persistence boundaries must remain explicit.
2. Data consistency rules are documented before schema or DAO changes.
3. Player-scoped lifecycle loops are preferred over global tick assumptions.

## Child Index

- [runtime/README.md](runtime/README.md): package ownership and lifecycle contracts
- [data/README.md](data/README.md): SQLite schema and consistency expectations
- [gui/README.md](gui/README.md): menu system decomposition and state ownership
