package com.zenbukkowa.plugin;

import com.zenbukkowa.breaker.AreaBreakListener;
import com.zenbukkowa.breaker.AreaCalculator;
import com.zenbukkowa.command.ZenbukkowaCommand;
import com.zenbukkowa.domain.BreakService;
import com.zenbukkowa.domain.EventService;
import com.zenbukkowa.domain.PointService;
import com.zenbukkowa.domain.SkillService;
import com.zenbukkowa.gui.HotbarMenuListener;
import com.zenbukkowa.gui.HotbarMenuService;
import com.zenbukkowa.gui.MenuListener;
import com.zenbukkowa.gui.MenuService;
import com.zenbukkowa.persistence.PlayerDao;
import com.zenbukkowa.persistence.SqliteDatabase;
import com.zenbukkowa.persistence.StructureDao;
import com.zenbukkowa.scoreboard.ScoreboardListener;
import com.zenbukkowa.scoreboard.ScoreboardService;
import com.zenbukkowa.structure.StructureBonusListener;
import com.zenbukkowa.structure.StructureService;
import com.zenbukkowa.domain.EffectService;
import com.zenbukkowa.domain.EffectListener;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;

public final class ZenbukkowaPlugin extends JavaPlugin {
    private Services services;

    @Override
    public void onEnable() {
        try {
            saveDefaultConfig();
            saveResource("skills.yml", false);
            FileConfiguration config = getConfig();
            String dbPath = config.getString("database.file", "plugins/zenbukkowa/zenbukkowa.db");
            Path fullDbPath = new File(getDataFolder().getParentFile().getParentFile(), dbPath).toPath();
            SqliteDatabase database = new SqliteDatabase(fullDbPath);
            database.initialize();

            PlayerDao playerDao = new PlayerDao(database);
            StructureDao structureDao = new StructureDao(database);
            PointService pointService = new PointService(playerDao);
            SkillService skillService = new SkillService(playerDao);
            AreaCalculator areaCalculator = new AreaCalculator();
            BreakService breakService = new BreakService(
                    pointService, skillService, areaCalculator,
                    config.getInt("points.base-per-block", 1),
                    config.getInt("points.ore-multiplier", 5),
                    config.getInt("points.ancient-debris-multiplier", 20));
            SchedulerBridge scheduler = new PaperSchedulerBridge();
            EventService eventService = new EventService(pointService, scheduler, this,
                    config.getInt("event.duration-minutes", 120));
            ScoreboardService scoreboardService = new ScoreboardService(pointService, skillService, scheduler, this);
            MenuService menuService = new MenuService();
            HotbarMenuService hotbarMenuService = new HotbarMenuService(this, menuService);
            StructureService structureService = new StructureService(structureDao, pointService);

            AreaBreakListener areaBreakListener = new AreaBreakListener(breakService);
            ScoreboardListener scoreboardListener = new ScoreboardListener(scoreboardService);
            StructureBonusListener structureBonusListener = new StructureBonusListener(structureService);
            EffectService effectService = new EffectService(skillService);
            EffectListener effectListener = new EffectListener(effectService, scheduler, this);
            MenuListener menuListener = new MenuListener(menuService, skillService, pointService, scoreboardService, effectService);
            HotbarMenuListener hotbarMenuListener = new HotbarMenuListener(hotbarMenuService);

            services = new Services(
                    pointService, skillService, breakService, eventService,
                    scoreboardService, menuService, hotbarMenuService, structureService,
                    areaBreakListener, scoreboardListener, structureBonusListener,
                    playerDao, structureDao, database, scheduler);

            registerListeners(areaBreakListener, scoreboardListener, structureBonusListener, menuListener, hotbarMenuListener, effectListener);
            registerCommand("zenbukkowa", new ZenbukkowaCommand(eventService, pointService));

            for (var online : getServer().getOnlinePlayers()) {
                hotbarMenuService.install(online);
                scoreboardService.addPlayer(online);
            }

            getLogger().info("zenbukkowa enabled");
        } catch (Exception ex) {
            getLogger().severe("Failed to enable: " + ex.getMessage());
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        if (services != null) {
            services.scoreboard().clearAll();
            services.scheduler().cancelAll();
            services.database().close();
        }
        getLogger().info("zenbukkowa disabled");
    }

    private void registerListeners(org.bukkit.event.Listener... listeners) {
        for (var listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    private void registerCommand(String name, CommandExecutor executor) {
        Objects.requireNonNull(getCommand(name), "Missing command in plugin.yml: " + name).setExecutor(executor);
    }

    public Services services() {
        return services;
    }
}
