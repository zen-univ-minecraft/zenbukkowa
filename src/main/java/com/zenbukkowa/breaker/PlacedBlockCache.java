package com.zenbukkowa.breaker;

import com.zenbukkowa.persistence.PlayerPlacedBlockDao;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PlacedBlockCache {
    private final PlayerPlacedBlockDao dao;
    private final Set<String> cache = ConcurrentHashMap.newKeySet();
    private final Map<String, Boolean> negativeCache = new ConcurrentHashMap<>();

    public PlacedBlockCache(PlayerPlacedBlockDao dao) {
        this.dao = dao;
    }

    public void record(String world, int x, int y, int z, String playerUuid) {
        String key = key(world, x, y, z);
        cache.add(key);
        negativeCache.remove(key);
        dao.record(world, x, y, z, playerUuid);
    }

    public void delete(String world, int x, int y, int z) {
        String key = key(world, x, y, z);
        cache.remove(key);
        negativeCache.remove(key);
        dao.delete(world, x, y, z);
    }

    public boolean isPlayerPlaced(String world, int x, int y, int z) {
        String key = key(world, x, y, z);
        if (cache.contains(key)) return true;
        Boolean neg = negativeCache.get(key);
        if (neg != null) return false;
        boolean placed = dao.isPlayerPlaced(world, x, y, z);
        if (placed) {
            cache.add(key);
        } else {
            negativeCache.put(key, Boolean.TRUE);
        }
        return placed;
    }

    public void deleteForPlayer(String playerUuid) {
        cache.clear();
        negativeCache.clear();
        dao.deleteForPlayer(playerUuid);
    }

    public void deleteAll() {
        cache.clear();
        negativeCache.clear();
        dao.deleteAll();
    }

    private String key(String world, int x, int y, int z) {
        return world + ":" + x + ":" + y + ":" + z;
    }
}
