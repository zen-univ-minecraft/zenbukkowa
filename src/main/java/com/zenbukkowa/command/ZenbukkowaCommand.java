package com.zenbukkowa.command;

import com.zenbukkowa.domain.EventService;
import com.zenbukkowa.domain.PointService;
import com.zenbukkowa.domain.SkillService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ZenbukkowaCommand implements CommandExecutor {
    private final EventService eventService;
    private final PointService pointService;
    private final SkillService skillService;

    public ZenbukkowaCommand(EventService eventService, PointService pointService, SkillService skillService) {
        this.eventService = eventService;
        this.pointService = pointService;
        this.skillService = skillService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + "Usage: /zenbukkowa <start|end|status|points|reset|resetall>");
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "start" -> {
                if (!sender.isOp()) {
                    sender.sendMessage(ChatColor.RED + "Requires op");
                    return true;
                }
                eventService.start();
            }
            case "end" -> {
                if (!sender.isOp()) {
                    sender.sendMessage(ChatColor.RED + "Requires op");
                    return true;
                }
                eventService.end();
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
            case "reset" -> {
                if (!sender.isOp()) {
                    sender.sendMessage(ChatColor.RED + "Requires op");
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /zenbukkowa reset <player>");
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "Player not found");
                    return true;
                }
                pointService.resetPlayer(target.getUniqueId());
                skillService.resetPlayer(target.getUniqueId());
                sender.sendMessage(ChatColor.GREEN + "Reset player: " + target.getName());
            }
            case "resetall" -> {
                if (!sender.isOp()) {
                    sender.sendMessage(ChatColor.RED + "Requires op");
                    return true;
                }
                pointService.resetAll();
                skillService.resetAll();
                eventService.reset();
                sender.sendMessage(ChatColor.GREEN + "All player data reset");
            }
            default -> sender.sendMessage(ChatColor.RED + "Unknown subcommand");
        }
        return true;
    }
}
