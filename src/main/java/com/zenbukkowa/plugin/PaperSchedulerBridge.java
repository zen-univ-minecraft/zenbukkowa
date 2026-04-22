package com.zenbukkowa.plugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class PaperSchedulerBridge implements SchedulerBridge {
    private final List<Integer> tasks = new ArrayList<>();

    @Override
    public void runTimer(JavaPlugin plugin, Runnable task, long delay, long period) {
        int id = Bukkit.getScheduler().runTaskTimer(plugin, task, delay, period).getTaskId();
        tasks.add(id);
    }

    @Override
    public void runLater(JavaPlugin plugin, Runnable task, long delay) {
        int id = Bukkit.getScheduler().runTaskLater(plugin, task, delay).getTaskId();
        tasks.add(id);
    }

    @Override
    public void cancelAll() {
        for (int id : tasks) {
            Bukkit.getScheduler().cancelTask(id);
        }
        tasks.clear();
    }
}
