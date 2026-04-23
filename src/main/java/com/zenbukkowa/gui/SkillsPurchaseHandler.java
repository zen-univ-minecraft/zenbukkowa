package com.zenbukkowa.gui;

import com.zenbukkowa.domain.*;
import com.zenbukkowa.scoreboard.ScoreboardService;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.stream.Collectors;

public class SkillsPurchaseHandler {
    private final MenuService menuService;
    private final SkillService skillService;
    private final PointService pointService;
    private final ScoreboardService scoreboardService;
    private final EffectService effectService;
    private final LocaleService locale;

    public SkillsPurchaseHandler(MenuService menuService, SkillService skillService,
                                 PointService pointService, ScoreboardService scoreboardService,
                                 EffectService effectService, LocaleService locale) {
        this.menuService = menuService;
        this.skillService = skillService;
        this.pointService = pointService;
        this.scoreboardService = scoreboardService;
        this.effectService = effectService;
        this.locale = locale;
    }

    public SkillService skillService() { return skillService; }
    public PointService pointService() { return pointService; }

    public void handleSkills(Player player, int slot) {
        if (slot == 49) { RootMenu.open(player, menuService, locale); return; }
        if (slot == 45) {
            int off = menuService.getScrollOffsetV(player);
            if (off > 0) { menuService.setScrollOffsetV(player, off - 1); SkillsMenu.open(player, menuService, skillService, pointService, locale); }
            return;
        }
        if (slot == 53) {
            int off = menuService.getScrollOffsetV(player);
            if (off < SkillTreeLayout.MAX_SCROLL_V) { menuService.setScrollOffsetV(player, off + 1); SkillsMenu.open(player, menuService, skillService, pointService, locale); }
            return;
        }
        if (slot == 47) {
            int off = menuService.getScrollOffsetH(player);
            if (off > 0) { menuService.setScrollOffsetH(player, off - 1); SkillsMenu.open(player, menuService, skillService, pointService, locale); }
            return;
        }
        if (slot == 51) {
            int off = menuService.getScrollOffsetH(player);
            if (off < SkillTreeLayout.MAX_SCROLL_H) { menuService.setScrollOffsetH(player, off + 1); SkillsMenu.open(player, menuService, skillService, pointService, locale); }
            return;
        }
        int offsetV = menuService.getScrollOffsetV(player);
        int offsetH = menuService.getScrollOffsetH(player);
        SkillType skill = SkillTreeViewport.skillAtSlot(slot, offsetV, offsetH);
        if (skill == null) return;
        int current = skillService.getSkills(player.getUniqueId()).tier(skill);
        if (current >= skill.maxTier()) {
            player.sendMessage(ChatColor.RED + locale.get(player.getUniqueId(), "menu.purchase_maxed"));
            return;
        }
        int targetTier = current + 1;
        if (!skillService.canPurchase(player.getUniqueId(), skill, targetTier)) {
            player.sendMessage(ChatColor.RED + locale.get(player.getUniqueId(), "menu.missing_prerequisite"));
            return;
        }
        try {
            if (skill.isMythic()) {
                Map<PointCategory, Integer> costs = skill.mythicCost(targetTier);
                pointService.spendPoints(player.getUniqueId(),
                        costs.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> (long) e.getValue().intValue())));
            } else {
                pointService.spendPoints(player.getUniqueId(), skill.category(), skill.cost(targetTier));
            }
            skillService.purchase(player.getUniqueId(), skill, targetTier);
            player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            player.sendMessage(ChatColor.GREEN + locale.get(player.getUniqueId(), "menu.purchase_success")
                    .replace("{skill}", skill.name().replace('_', ' '))
                    .replace("{tier}", String.valueOf(targetTier)));
            effectService.applyAll(player);
            scoreboardService.updateAll();
            SkillsMenu.open(player, menuService, skillService, pointService, locale);
        } catch (IllegalStateException e) {
            player.sendMessage(ChatColor.RED + e.getMessage());
        }
    }
}
