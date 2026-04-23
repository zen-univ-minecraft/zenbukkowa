package com.zenbukkowa.plugin;

import com.zenbukkowa.breaker.AreaBreakListener;
import com.zenbukkowa.breaker.AreaCalculator;
import com.zenbukkowa.breaker.BlockPlaceListener;
import com.zenbukkowa.breaker.PlacedBlockCache;
import com.zenbukkowa.breaker.PistonListener;
import com.zenbukkowa.breaker.BonemealTask;
import com.zenbukkowa.breaker.BreakPointCalculator;
import com.zenbukkowa.command.ZenbukkowaCommand;
import com.zenbukkowa.domain.*;
import com.zenbukkowa.gui.*;
import com.zenbukkowa.persistence.BlockDiscoveryDao;
import com.zenbukkowa.persistence.PlayerDao;
import com.zenbukkowa.persistence.PlayerPlacedBlockDao;
import com.zenbukkowa.persistence.SettingsDao;
import com.zenbukkowa.persistence.SqliteDatabase;
import com.zenbukkowa.persistence.StructureDao;
import com.zenbukkowa.scoreboard.ScoreboardListener;
import com.zenbukkowa.scoreboard.ScoreboardService;
import com.zenbukkowa.structure.StructureBonusListener;
import com.zenbukkowa.structure.StructureService;
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
            SettingsDao settingsDao = new SettingsDao(database);
            PlayerPlacedBlockDao playerPlacedBlockDao = new PlayerPlacedBlockDao(database);
            BlockDiscoveryDao blockDiscoveryDao = new BlockDiscoveryDao(database);
            settingsDao.initialize();

            PointService pointService = new PointService(playerDao, playerPlacedBlockDao, blockDiscoveryDao);
            double savedMultiplier = Double.parseDouble(settingsDao.loadSetting("point_multiplier", "1.0"));
            pointService.setMultiplier(savedMultiplier);

            SkillService skillService = new SkillService(playerDao);
            AreaCalculator areaCalculator = new AreaCalculator();
            BreakPointCalculator pointCalculator = new BreakPointCalculator(
                    config.getInt("points.base-per-block", 1),
                    config.getInt("points.ore-multiplier", 5),
                    config.getInt("points.ancient-debris-multiplier", 20));
            PlacedBlockCache placedBlockCache = new PlacedBlockCache(playerPlacedBlockDao);
            LocaleService localeService = new LocaleService(this, settingsDao, config.getString("locale.default", "en"));
            BlockDiscoveryService blockDiscoveryService = new BlockDiscoveryService(blockDiscoveryDao, pointService, localeService);
            BreakService breakService = new BreakService(
                    pointService, skillService, areaCalculator, pointCalculator,
                    placedBlockCache, blockDiscoveryService, this);
            SchedulerBridge scheduler = new PaperSchedulerBridge();
            EventService eventService = new EventService(pointService, this);
            ScoreboardService scoreboardService = new ScoreboardService(pointService, skillService, eventService, scheduler, this);
            pointService.setOnChange(uuid -> {
                org.bukkit.entity.Player p = org.bukkit.Bukkit.getPlayer(uuid);
                if (p != null) scoreboardService.updatePlayer(p);
            });
            scheduler.runTimer(this, pointService::flushAll, 100, 100);
            MenuService menuService = new MenuService();
            HotbarMenuService hotbarMenuService = new HotbarMenuService(this, menuService, localeService);
            StructureService structureService = new StructureService(structureDao, pointService);

            AreaBreakListener areaBreakListener = new AreaBreakListener(breakService);
            BlockPlaceListener blockPlaceListener = new BlockPlaceListener(placedBlockCache);
            PistonListener pistonListener = new PistonListener(placedBlockCache);
            BonemealTask bonemealTask = new BonemealTask(skillService, this);
            bonemealTask.start();
            ScoreboardListener scoreboardListener = new ScoreboardListener(scoreboardService);
            StructureBonusListener structureBonusListener = new StructureBonusListener(structureService);
            EffectService effectService = new EffectService(skillService, eventService);
            EffectListener effectListener = new EffectListener(effectService, scheduler, this);
            SkillsPurchaseHandler skillsPurchaseHandler = new SkillsPurchaseHandler(
                    menuService, skillService, pointService, scoreboardService, effectService, localeService);
            MenuListener menuListener = new MenuListener(menuService, hotbarMenuService, skillsPurchaseHandler,
                    scoreboardService, effectService, eventService, localeService, settingsDao);
            HotbarMenuListener hotbarMenuListener = new HotbarMenuListener(hotbarMenuService);

            services = new Services(
                    pointService, skillService, breakService, eventService,
                    scoreboardService, menuService, hotbarMenuService, structureService,
                    localeService, settingsDao,
                    areaBreakListener, scoreboardListener, structureBonusListener,
                    playerDao, structureDao, database, scheduler);

            VoidWalkListener voidWalkListener = new VoidWalkListener(skillService);
            registerListeners(areaBreakListener, blockPlaceListener, pistonListener, scoreboardListener, structureBonusListener,
                    menuListener, hotbarMenuListener, effectListener, voidWalkListener);
            registerCommand("zenbukkowa", new ZenbukkowaCommand(eventService, pointService, skillService));

            for (var online : getServer().getOnlinePlayers()) {
                hotbarMenuService.install(online);
                scoreboardService.addPlayer(online);
                effectService.applyAll(online);
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
