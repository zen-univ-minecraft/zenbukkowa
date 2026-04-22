package com.zenbukkowa.plugin;

import com.zenbukkowa.breaker.AreaBreakListener;
import com.zenbukkowa.breaker.AreaCalculator;
import com.zenbukkowa.domain.BreakService;
import com.zenbukkowa.domain.EventService;
import com.zenbukkowa.domain.PointService;
import com.zenbukkowa.domain.SkillService;
import com.zenbukkowa.gui.HotbarMenuService;
import com.zenbukkowa.gui.MenuService;
import com.zenbukkowa.persistence.PlayerDao;
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
        AreaBreakListener areaBreakListener,
        ScoreboardListener scoreboardListener,
        StructureBonusListener structureBonusListener,
        PlayerDao playerDao,
        StructureDao structureDao,
        SqliteDatabase database,
        SchedulerBridge scheduler
) {
}
