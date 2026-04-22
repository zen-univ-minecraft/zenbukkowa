package com.zenbukkowa.domain;

import com.zenbukkowa.plugin.SchedulerBridge;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EventService {
    private final PointService pointService;
    private final SchedulerBridge scheduler;
    private final JavaPlugin plugin;
    private final int durationSeconds;
    private boolean running = false;
    private boolean finished = false;

    public EventService(PointService pointService, SchedulerBridge scheduler,
                        JavaPlugin plugin, int durationMinutes) {
        this.pointService = pointService;
        this.scheduler = scheduler;
        this.plugin = plugin;
        this.durationSeconds = durationMinutes * 60;
    }

    public void start() {
        if (running || finished) {
            return;
        }
        running = true;
        Bukkit.broadcastMessage(ChatColor.GREEN + "Event started! Duration: " + formatTime(durationSeconds));
        scheduler.runTimer(plugin, this::tick, 0, 20);
        scheduler.runLater(plugin, this::end, durationSeconds * 20L);
    }

    public void end() {
        if (!running) {
            return;
        }
        running = false;
        finished = true;
        announceWinner();
    }

    private void tick() {
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isFinished() {
        return finished;
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

    private String formatTime(int seconds) {
        int h = seconds / 3600;
        int m = (seconds % 3600) / 60;
        int s = seconds % 60;
        return String.format("%02d:%02d:%02d", h, m, s);
    }

    private String format(long n) {
        return String.format("%,d", n);
    }
}
