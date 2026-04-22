package com.zenbukkowa.scoreboard;

import com.zenbukkowa.domain.EventService;
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
    private final EventService eventService;
    private final SchedulerBridge scheduler;
    private final JavaPlugin plugin;
    private final Map<UUID, Scoreboard> boards = new HashMap<>();
    private final Map<UUID, Boolean> enabled = new HashMap<>();

    public ScoreboardService(PointService pointService, SkillService skillService,
                             EventService eventService, SchedulerBridge scheduler, JavaPlugin plugin) {
        this.pointService = pointService;
        this.skillService = skillService;
        this.eventService = eventService;
        this.scheduler = scheduler;
        this.plugin = plugin;
        scheduler.runTimer(plugin, this::tick, 0, 20);
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
        boards.remove(player.getUniqueId());
    }

    public void tick() {
        updateAll();
    }

    public void updateAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updatePlayer(player);
        }
    }

    public void updatePlayer(Player player) {
        if (!isEnabled(player.getUniqueId())) {
            return;
        }
        Scoreboard board = boards.computeIfAbsent(player.getUniqueId(), u -> createBoard());
        var progress = pointService.getProgress(player.getUniqueId());

        setLine(board, 12, ChatColor.GRAY + "-------------");
        setLine(board, 11, ChatColor.YELLOW + "Time: " + formatTime());
        setLine(board, 10, ChatColor.WHITE + "Total: " + format(progress.totalPoints()));
        setLine(board, 9, ChatColor.GREEN + "TERRA: " + format(progress.points(PointCategory.TERRA)));
        setLine(board, 8, ChatColor.AQUA + "MINERAL: " + format(progress.points(PointCategory.MINERAL)));
        setLine(board, 7, ChatColor.DARK_GREEN + "ORGANIC: " + format(progress.points(PointCategory.ORGANIC)));
        setLine(board, 6, ChatColor.BLUE + "AQUATIC: " + format(progress.points(PointCategory.AQUATIC)));
        setLine(board, 5, ChatColor.DARK_PURPLE + "VOID: " + format(progress.points(PointCategory.VOID)));
        setLine(board, 4, ChatColor.GRAY + "-------------");
        int r = skillService.radius(player.getUniqueId());
        int d = skillService.depth(player.getUniqueId());
        setLine(board, 3, ChatColor.YELLOW + "Area: " + r + "x" + r + "x" + d);
        int h = skillService.haste(player.getUniqueId());
        setLine(board, 2, ChatColor.YELLOW + "Haste: " + roman(h));
        setLine(board, 1, ChatColor.GRAY + "-------------");

        player.setScoreboard(board);
    }

    private Scoreboard createBoard() {
        Scoreboard board = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();
        Objective obj = board.registerNewObjective("zenbukkowa", Criteria.DUMMY, ChatColor.GOLD + "zenbukkowa");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        return board;
    }

    private void apply(Player player) {
        if (isEnabled(player.getUniqueId())) {
            updatePlayer(player);
        } else {
            player.setScoreboard(Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard());
        }
    }

    private void setLine(Scoreboard board, int score, String text) {
        Team team = board.getTeam("line_" + score);
        if (team == null) {
            team = board.registerNewTeam("line_" + score);
            String entry = ChatColor.values()[score % 16].toString();
            team.addEntry(entry);
            board.getObjective("zenbukkowa").getScore(entry).setScore(score);
        }
        team.setPrefix(text);
    }

    private String formatTime() {
        if (!eventService.isRunning() && !eventService.isFinished()) {
            return "Waiting";
        }
        int seconds = eventService.elapsedSeconds();
        if (eventService.isFinished()) {
            return "Finished";
        }
        int h = seconds / 3600;
        int m = (seconds % 3600) / 60;
        int s = seconds % 60;
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
