package com.zenbukkowa.command;

import com.zenbukkowa.domain.EventService;
import com.zenbukkowa.domain.PointService;
import com.zenbukkowa.scoreboard.ScoreboardService;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ZenbukkowaCommand implements CommandExecutor {
    private final EventService eventService;
    private final PointService pointService;
    private final ScoreboardService scoreboardService;

    public ZenbukkowaCommand(EventService eventService, PointService pointService, ScoreboardService scoreboardService) {
        this.eventService = eventService;
        this.pointService = pointService;
        this.scoreboardService = scoreboardService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + "Usage: /zenbukkowa <start|end|status>");
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "start" -> {
                if (!sender.isOp()) {
                    sender.sendMessage(ChatColor.RED + "Requires op");
                    return true;
                }
                eventService.start();
                scoreboardService.startTimer(eventService.remainingSeconds());
            }
            case "end" -> {
                if (!sender.isOp()) {
                    sender.sendMessage(ChatColor.RED + "Requires op");
                    return true;
                }
                eventService.end();
                scoreboardService.stopTimer();
            }
            case "status" -> {
                String state = eventService.isRunning() ? "Running" : (eventService.isFinished() ? "Finished" : "Waiting");
                sender.sendMessage(ChatColor.YELLOW + "Event state: " + state);
            }
            case "points" -> {
                if (!(sender instanceof Player player)) {
                    sender.sendMessage(ChatColor.RED + "Player only");
                    return true;
                }
                var progress = pointService.getProgress(player.getUniqueId());
                sender.sendMessage(ChatColor.GOLD + "Total: " + progress.totalPoints());
            }
            default -> sender.sendMessage(ChatColor.RED + "Unknown subcommand");
        }
        return true;
    }
}
