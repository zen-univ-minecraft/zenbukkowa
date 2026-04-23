package com.zenbukkowa.domain;

import com.zenbukkowa.persistence.PlayerDao;

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
    private final Map<UUID, PlayerProgress> cache = new ConcurrentHashMap<>();
    private Consumer<UUID> onChange;
    private double multiplier = 1.0;

    public PointService(PlayerDao playerDao) {
        this.playerDao = playerDao;
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
        PlayerProgress progress = getProgress(uuid);
        synchronized (progress) {
            progress.addPoints(category, (long) (amount * multiplier));
            progress.incrementBlocksBroken(blocks);
            try {
                playerDao.saveProgress(progress);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        notifyChange(uuid);
    }

    public void spendPoints(UUID uuid, PointCategory category, long amount) {
        PlayerProgress progress = getProgress(uuid);
        synchronized (progress) {
            if (progress.points(category) < amount) {
                throw new IllegalStateException("Insufficient " + category + " points");
            }
            progress.addPoints(category, -amount);
            try {
                playerDao.saveProgress(progress);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        notifyChange(uuid);
    }

    public void spendPoints(UUID uuid, Map<PointCategory, Long> costs) {
        PlayerProgress progress = getProgress(uuid);
        synchronized (progress) {
            for (Map.Entry<PointCategory, Long> e : costs.entrySet()) {
                if (progress.points(e.getKey()) < e.getValue()) {
                    throw new IllegalStateException("Insufficient " + e.getKey() + " points");
                }
            }
            for (Map.Entry<PointCategory, Long> e : costs.entrySet()) {
                progress.addPoints(e.getKey(), -e.getValue());
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
        return cache.entrySet().stream()
                .sorted(Comparator.comparingLong((Map.Entry<UUID, PlayerProgress> e) -> e.getValue().totalPoints()).reversed()
                        .thenComparing(e -> e.getValue().points(PointCategory.MINERAL), Comparator.reverseOrder())
                        .thenComparing(e -> e.getValue().points(PointCategory.VOID), Comparator.reverseOrder()))
                .limit(limit)
                .map(e -> Map.entry(e.getKey(), e.getValue().totalPoints()))
                .collect(Collectors.toList());
    }

    public void resetPlayer(UUID uuid) {
        try {
            playerDao.deleteProgress(uuid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        cache.remove(uuid);
        notifyChange(uuid);
    }

    public void resetAll() {
        try {
            playerDao.deleteAllProgress();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        cache.clear();
    }

    public void unload(UUID uuid) {
        cache.remove(uuid);
    }

    private void notifyChange(UUID uuid) {
        if (onChange != null) {
            onChange.accept(uuid);
        }
    }
}
