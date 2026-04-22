package com.zenbukkowa.scoreboard;

import com.zenbukkowa.domain.PointCategory;
import com.zenbukkowa.domain.PointService;
import com.zenbukkowa.domain.SkillService;
import com.zenbukkowa.plugin.SchedulerBridge;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;

import java.util.*;

public class ScoreboardService {
    private final PointService pointService;
    private final SkillService skillService;
    private final SchedulerBridge scheduler;
    private final JavaPlugin plugin;
    private final Map<UUID, Boolean> enabled = new HashMap<>();
    private final Scoreboard board;
    private final Objective objective;
    private int remainingSeconds = -1;

    public ScoreboardService(PointService pointService, SkillService skillService,
                             SchedulerBridge scheduler, JavaPlugin plugin) {
        this.pointService = pointService;
        this.skillService = skillService;
        this.scheduler = scheduler;
        this.plugin = plugin;
        this.board = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();
        this.objective = board.registerNewObjective("zenbukkowa", Criteria.DUMMY, ChatColor.GOLD + "zenbukkowa");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void startTimer(int durationSeconds) {
        this.remainingSeconds = durationSeconds;
        scheduler.runTimer(plugin, this::tick, 0, 20);
    }

    public void stopTimer() {
        this.remainingSeconds = -1;
        updateAll();
    }

    public void setEnabled(UUID uuid, boolean value) {
        enabled.put(uuid, value);
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            apply(player);
        }
    }

    public boolean isEnabled(UUID uuid) {
        return enabled.getOrDefault(uuid, true);
    }

    public void addPlayer(Player player) {
        apply(player);
    }

    public void removePlayer(Player player) {
        player.setScoreboard(Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard());
    }

    public void tick() {
        if (remainingSeconds > 0) {
            remainingSeconds--;
        }
        updateAll();
    }

    public void updateAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updatePlayer(player);
        }
    }

    private void updatePlayer(Player player) {
        if (!isEnabled(player.getUniqueId())) {
            return;
        }
        var progress = pointService.getProgress(player.getUniqueId());
        var skills = skillService.getSkills(player.getUniqueId());

        setLine(12, ChatColor.GRAY + "-------------");
        setLine(11, ChatColor.YELLOW + "Time: " + formatTime());
        setLine(10, ChatColor.WHITE + "Total: " + format(progress.totalPoints()));
        setLine(9, ChatColor.GREEN + "TERRA: " + format(progress.points(PointCategory.TERRA)));
        setLine(8, ChatColor.AQUA + "MINERAL: " + format(progress.points(PointCategory.MINERAL)));
        setLine(7, ChatColor.DARK_GREEN + "ORGANIC: " + format(progress.points(PointCategory.ORGANIC)));
        setLine(6, ChatColor.BLUE + "AQUATIC: " + format(progress.points(PointCategory.AQUATIC)));
        setLine(5, ChatColor.DARK_PURPLE + "VOID: " + format(progress.points(PointCategory.VOID)));
        setLine(4, ChatColor.GRAY + "-------------");
        int r = skillService.radius(player.getUniqueId());
        int d = skillService.depth(player.getUniqueId());
        setLine(3, ChatColor.YELLOW + "Area: " + r + "x" + r + "x" + d);
        int h = skillService.haste(player.getUniqueId());
        setLine(2, ChatColor.YELLOW + "Haste: " + roman(h));
        setLine(1, ChatColor.GRAY + "-------------");

        player.setScoreboard(board);
    }

    private void apply(Player player) {
        if (isEnabled(player.getUniqueId())) {
            updatePlayer(player);
        } else {
            player.setScoreboard(Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard());
        }
    }

    private void setLine(int score, String text) {
        Team team = board.getTeam("line_" + score);
        if (team == null) {
            team = board.registerNewTeam("line_" + score);
            String entry = ChatColor.values()[score % 16].toString();
            team.addEntry(entry);
            objective.getScore(entry).setScore(score);
        }
        team.setPrefix(text);
    }

    private String formatTime() {
        if (remainingSeconds < 0) {
            return "Waiting";
        }
        if (remainingSeconds == 0) {
            return "Finished";
        }
        int h = remainingSeconds / 3600;
        int m = (remainingSeconds % 3600) / 60;
        int s = remainingSeconds % 60;
        return String.format("%02d:%02d:%02d", h, m, s);
    }

    private String format(long n) {
        return String.format("%,d", n);
    }

    private String roman(int n) {
        return switch (n) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            default -> "0";
        };
    }

    public void clearAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            removePlayer(player);
        }
    }
}
