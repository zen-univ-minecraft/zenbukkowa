package com.zenbukkowa.plugin;

import org.bukkit.plugin.java.JavaPlugin;

public interface SchedulerBridge {
    void runTimer(JavaPlugin plugin, Runnable task, long delay, long period);
    void runLater(JavaPlugin plugin, Runnable task, long delay);
    void cancelAll();
}
