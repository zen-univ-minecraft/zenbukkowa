package com.zenbukkowa.plugin;

import com.zenbukkowa.breaker.AreaBreakListener;
import com.zenbukkowa.domain.*;
import com.zenbukkowa.gui.HotbarMenuService;
import com.zenbukkowa.gui.MenuService;
import com.zenbukkowa.persistence.PlayerDao;
import com.zenbukkowa.persistence.SettingsDao;
import com.zenbukkowa.persistence.SqliteDatabase;
import com.zenbukkowa.persistence.StructureDao;
import com.zenbukkowa.scoreboard.ScoreboardListener;
import com.zenbukkowa.scoreboard.ScoreboardService;
import com.zenbukkowa.structure.StructureBonusListener;
import com.zenbukkowa.structure.StructureService;

public record Services(
        PointService points,
        SkillService skills,
        BreakService breaks,
        EventService events,
        ScoreboardService scoreboard,
        MenuService menus,
        HotbarMenuService hotbar,
        StructureService structures,
        LocaleService locale,
        SettingsDao settingsDao,
        AreaBreakListener areaBreakListener,
        ScoreboardListener scoreboardListener,
        StructureBonusListener structureBonusListener,
        PlayerDao playerDao,
        StructureDao structureDao,
        SqliteDatabase database,
        SchedulerBridge scheduler
) {
}
