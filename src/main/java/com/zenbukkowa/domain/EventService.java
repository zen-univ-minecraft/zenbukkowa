package com.zenbukkowa.domain;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EventService {
    private final PointService pointService;
    private final JavaPlugin plugin;
    private Long startTimestamp = null;
    private Long endTimestamp = null;

    public EventService(PointService pointService, JavaPlugin plugin) {
        this.pointService = pointService;
        this.plugin = plugin;
    }

    public void start() {
        if (startTimestamp != null) {
            return;
        }
        startTimestamp = System.currentTimeMillis();
        Bukkit.broadcastMessage(ChatColor.GREEN + "Event started!");
    }

    public void end() {
        if (startTimestamp == null || endTimestamp != null) {
            return;
        }
        endTimestamp = System.currentTimeMillis();
        announceWinner();
    }

    public boolean isRunning() {
        return startTimestamp != null && endTimestamp == null;
    }

    public boolean isFinished() {
        return endTimestamp != null;
    }

    public void reset() {
        startTimestamp = null;
        endTimestamp = null;
    }

    public int elapsedSeconds() {
        if (startTimestamp == null) {
            return 0;
        }
        long end = endTimestamp != null ? endTimestamp : System.currentTimeMillis();
        return (int) ((end - startTimestamp) / 1000);
    }

    private void announceWinner() {
        List<Map.Entry<UUID, Long>> board = pointService.getLeaderboard(3);
        if (board.isEmpty()) {
            Bukkit.broadcastMessage(ChatColor.YELLOW + "Event finished! No scores recorded.");
            return;
        }
        Map.Entry<UUID, Long> winner = board.get(0);
        Player winnerPlayer = Bukkit.getPlayer(winner.getKey());
        String winnerName = winnerPlayer != null ? winnerPlayer.getName() : "Unknown";

        Bukkit.broadcastMessage(ChatColor.GOLD + "========== EVENT FINISHED ==========");
        Bukkit.broadcastMessage(ChatColor.GOLD + "Winner: " + winnerName + " with " + format(winner.getValue()) + " points!");
        for (int i = 0; i < board.size(); i++) {
            Map.Entry<UUID, Long> entry = board.get(i);
            Player p = Bukkit.getPlayer(entry.getKey());
            String name = p != null ? p.getName() : "?";
            Bukkit.broadcastMessage(ChatColor.YELLOW + "#" + (i + 1) + " " + name + " - " + format(entry.getValue()));
        }
        Bukkit.broadcastMessage(ChatColor.GOLD + "====================================");

        if (winnerPlayer != null && winnerPlayer.isOnline()) {
            winnerPlayer.sendTitle(ChatColor.GOLD + "Winner: " + winnerName,
                    ChatColor.YELLOW + format(winner.getValue()) + " Points", 10, 100, 20);
        }
    }

    private String format(long n) {
        return String.format("%,d", n);
    }
}
