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

        List<PointCategory> sorted = Arrays.stream(PointCategory.values())
                .sorted(Comparator.comparingLong((PointCategory c) -> progress.lastUpdated(c)).reversed()
                        .thenComparing(Enum::ordinal))
                .toList();

        setLine(board, 15, ChatColor.GRAY + "-------------");
        setLine(board, 14, ChatColor.YELLOW + "Time: " + formatTime());
        setLine(board, 13, ChatColor.WHITE + "Total: " + format(progress.totalPoints()));

        int score = 12;
        for (PointCategory cat : sorted) {
            setLine(board, score, categoryColor(cat) + cat.name() + ": " + format(progress.points(cat)));
            score--;
        }

        setLine(board, 5, ChatColor.GRAY + "-------------");
        int r = skillService.radius(player.getUniqueId());
        int d = skillService.depth(player.getUniqueId());
        setLine(board, 4, ChatColor.YELLOW + "Area: " + r + "x" + d + "x" + r);
        int h = skillService.haste(player.getUniqueId());
        setLine(board, 3, ChatColor.YELLOW + "Haste: " + roman(h));
        setLine(board, 2, ChatColor.GRAY + (eventService.isRunning() ? "Event Running" : eventService.isFinished() ? "Finished" : "Waiting"));
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

    private ChatColor categoryColor(PointCategory cat) {
        return switch (cat) {
            case TERRA -> ChatColor.GREEN;
            case MINERAL -> ChatColor.AQUA;
            case ORGANIC -> ChatColor.DARK_GREEN;
            case AQUATIC -> ChatColor.BLUE;
            case VOID -> ChatColor.DARK_PURPLE;
            case CROP -> ChatColor.YELLOW;
            case DISCOVERY -> ChatColor.LIGHT_PURPLE;
        };
    }

    public void clearAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            removePlayer(player);
        }
    }
}
