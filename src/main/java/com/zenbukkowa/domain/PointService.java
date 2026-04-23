package com.zenbukkowa.domain;

import com.zenbukkowa.persistence.BlockDiscoveryDao;
import com.zenbukkowa.persistence.PlayerDao;
import com.zenbukkowa.persistence.PlayerPlacedBlockDao;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PointService {
    private final PlayerDao playerDao;
    private final PlayerPlacedBlockDao playerPlacedBlockDao;
    private final BlockDiscoveryDao blockDiscoveryDao;
    private final Map<UUID, PlayerProgress> cache = new ConcurrentHashMap<>();
    private final Map<UUID, PlayerProgress> dirty = new ConcurrentHashMap<>();
    private Consumer<UUID> onChange;
    private double multiplier = 1.0;

    public PointService(PlayerDao playerDao, PlayerPlacedBlockDao playerPlacedBlockDao,
                        BlockDiscoveryDao blockDiscoveryDao) {
        this.playerDao = playerDao;
        this.playerPlacedBlockDao = playerPlacedBlockDao;
        this.blockDiscoveryDao = blockDiscoveryDao;
    }

    public void setOnChange(Consumer<UUID> onChange) {
        this.onChange = onChange;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public PlayerProgress getProgress(UUID uuid) {
        return cache.computeIfAbsent(uuid, u -> {
            try {
                return playerDao.loadProgress(u);
            } catch (SQLException e) {
                return new PlayerProgress(u);
            }
        });
    }

    public void addPoints(UUID uuid, PointCategory category, long amount, long blocks) {
        addPointsBatch(uuid, Map.of(category, amount), blocks);
    }

    public void addPoints(UUID uuid, PointCategory category, long amount, long blocks, boolean touch) {
        addPointsBatch(uuid, Map.of(category, amount), blocks, touch);
    }

    public void addPointsBatch(UUID uuid, Map<PointCategory, Long> amounts, long blocks) {
        addPointsBatch(uuid, amounts, blocks, true);
    }

    public void addPointsBatch(UUID uuid, Map<PointCategory, Long> amounts, long blocks, boolean touch) {
        PlayerProgress progress = getProgress(uuid);
        synchronized (progress) {
            for (Map.Entry<PointCategory, Long> e : amounts.entrySet()) {
                progress.addPoints(e.getKey(), (long) (e.getValue() * multiplier));
                if (touch) progress.touch(e.getKey());
            }
            progress.incrementBlocksBroken(blocks);
        }
        dirty.put(uuid, progress);
        notifyChange(uuid);
    }

    public void flush(UUID uuid) {
        PlayerProgress progress = dirty.remove(uuid);
        if (progress == null) return;
        synchronized (progress) {
            try {
                playerDao.saveProgress(progress);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void flushAll() {
        for (UUID uuid : List.copyOf(dirty.keySet())) {
            flush(uuid);
        }
    }

    public void spendPoints(UUID uuid, PointCategory category, long amount) {
        flush(uuid);
        PlayerProgress progress = getProgress(uuid);
        synchronized (progress) {
            if (progress.points(category) < amount) {
                throw new IllegalStateException("Insufficient " + category + " points");
            }
            progress.addPoints(category, -amount);
            progress.touch(category);
            try {
                playerDao.saveProgress(progress);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        notifyChange(uuid);
    }

    public void spendPoints(UUID uuid, Map<PointCategory, Long> costs) {
        flush(uuid);
        PlayerProgress progress = getProgress(uuid);
        synchronized (progress) {
            for (Map.Entry<PointCategory, Long> e : costs.entrySet()) {
                if (progress.points(e.getKey()) < e.getValue()) {
                    throw new IllegalStateException("Insufficient " + e.getKey() + " points");
                }
            }
            for (Map.Entry<PointCategory, Long> e : costs.entrySet()) {
                progress.addPoints(e.getKey(), -e.getValue());
                progress.touch(e.getKey());
            }
            try {
                playerDao.saveProgress(progress);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        notifyChange(uuid);
    }

    public List<Map.Entry<UUID, Long>> getLeaderboard(int limit) {
        flushAll();
        return cache.entrySet().stream()
                .sorted(Comparator.comparingLong((Map.Entry<UUID, PlayerProgress> e) -> e.getValue().totalPoints()).reversed()
                        .thenComparing(e -> e.getValue().points(PointCategory.MINERAL), Comparator.reverseOrder())
                        .thenComparing(e -> e.getValue().points(PointCategory.VOID), Comparator.reverseOrder()))
                .limit(limit)
                .map(e -> Map.entry(e.getKey(), e.getValue().totalPoints()))
                .collect(Collectors.toList());
    }

    public void resetPlayer(UUID uuid) {
        flush(uuid);
        try {
            playerDao.deleteProgress(uuid);
            playerPlacedBlockDao.deleteForPlayer(uuid.toString());
            blockDiscoveryDao.deleteForPlayer(uuid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        cache.remove(uuid);
        notifyChange(uuid);
    }

    public void resetAll() {
        flushAll();
        try {
            playerDao.deleteAllProgress();
            playerPlacedBlockDao.deleteAll();
            blockDiscoveryDao.deleteAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        cache.clear();
    }

    public void unload(UUID uuid) {
        flush(uuid);
        cache.remove(uuid);
    }

    private void notifyChange(UUID uuid) {
        if (onChange != null) {
            onChange.accept(uuid);
        }
    }
}
