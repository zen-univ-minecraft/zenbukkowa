#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."

RCON_HOST="${RCON_HOST:-paper}"
RCON_PORT="${RCON_PORT:-25575}"
RCON_PASSWORD="${RCON_PASSWORD:-zenbukkowa-rcon}"

echo "=== waiting for RCON ==="
for i in $(seq 1 60); do
    if echo "version" | nc -q 1 "$RCON_HOST" "$RCON_PORT" >/dev/null 2>&1; then
        break
    fi
    sleep 2
done

echo "=== checking plugin loaded ==="
PLUGINS=$(echo "plugins" | nc -q 1 "$RCON_HOST" "$RCON_PORT" | grep -i zenbukkowa || true)
if [ -z "$PLUGINS" ]; then
    echo "ERROR: zenbukkowa not loaded"
    exit 1
fi
echo "plugin loaded: $PLUGINS"

echo "=== checking zenbukkowa status ==="
echo "zenbukkowa status" | nc -q 1 "$RCON_HOST" "$RCON_PORT" || true

echo "=== smoke done ==="
