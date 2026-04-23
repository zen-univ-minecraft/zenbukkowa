package com.zenbukkowa.domain;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

public class PlayerProgress {
    private final UUID uuid;
    private final Map<PointCategory, Long> points;
    private final Map<PointCategory, Long> lastUpdated;
    private long blocksBroken;
    private long discoveries;

    public PlayerProgress(UUID uuid) {
        this.uuid = uuid;
        this.points = new EnumMap<>(PointCategory.class);
        this.lastUpdated = new EnumMap<>(PointCategory.class);
        for (PointCategory c : PointCategory.values()) {
            this.points.put(c, 50L);
            this.lastUpdated.put(c, 0L);
        }
        this.blocksBroken = 0;
        this.discoveries = 0;
    }

    public UUID uuid() {
        return uuid;
    }

    public long points(PointCategory category) {
        return points.getOrDefault(category, 0L);
    }

    public void addPoints(PointCategory category, long amount) {
        points.put(category, points(category) + amount);
    }

    public long totalPoints() {
        return points.values().stream().mapToLong(Long::longValue).sum();
    }

    public long blocksBroken() {
        return blocksBroken;
    }

    public void incrementBlocksBroken(long amount) {
        this.blocksBroken += amount;
    }

    public void setBlocksBroken(long blocksBroken) {
        this.blocksBroken = blocksBroken;
    }

    public void setPoints(PointCategory category, long amount) {
        points.put(category, amount);
    }

    public long discoveries() {
        return discoveries;
    }

    public void setDiscoveries(long discoveries) {
        this.discoveries = discoveries;
    }

    public void incrementDiscoveries(long amount) {
        this.discoveries += amount;
    }

    public void touch(PointCategory category) {
        lastUpdated.put(category, System.currentTimeMillis());
    }

    public long lastUpdated(PointCategory category) {
        return lastUpdated.getOrDefault(category, 0L);
    }
}
