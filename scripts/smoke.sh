#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."

RCON_HOST="${RCON_HOST:-paper}"
RCON_PORT="${RCON_PORT:-25575}"
RCON_PASSWORD="${RCON_PASSWORD:-zenbukkowa-rcon}"

echo "=== waiting for RCON ==="
python3 -c "
import socket, sys, struct, time
for i in range(60):
    try:
        s = socket.create_connection(('${RCON_HOST}', ${RCON_PORT}), timeout=2)
        s.close()
        break
    except:
        time.sleep(2)
else:
    print('ERROR: RCON not reachable')
    sys.exit(1)
print('RCON reachable')
"

echo "=== checking plugin loaded via RCON ==="
python3 -c "
import socket, struct, sys

def send_rcon(sock, req_id, type_, payload):
    payload = payload.encode('utf-8')
    size = 10 + len(payload)
    sock.sendall(struct.pack('<iii', size, req_id, type_) + payload + b'\x00\x00')
    resp = sock.recv(4096)
    rsize, rid, rtype = struct.unpack('<iii', resp[:12])
    return resp[12:12+rsize-10].decode('utf-8', errors='replace')

try:
    s = socket.create_connection(('${RCON_HOST}', ${RCON_PORT}), timeout=5)
    send_rcon(s, 1, 3, '${RCON_PASSWORD}')
    out = send_rcon(s, 2, 2, 'plugins')
    s.close()
    if 'zenbukkowa' in out:
        print('plugin loaded: zenbukkowa')
    else:
        print('ERROR: zenbukkowa not in plugin list')
        print(out)
        sys.exit(1)
except Exception as e:
    print('ERROR:', e)
    sys.exit(1)
"

echo "=== smoke done ==="
