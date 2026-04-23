package com.zenbukkowa.gui;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MenuService {
    private final Map<UUID, String> openMenus = new HashMap<>();
    private final Map<UUID, Integer> skillScrollV = new HashMap<>();
    private final Map<UUID, Integer> skillScrollH = new HashMap<>();

    public void setOpen(Player player, String menuId) {
        openMenus.put(player.getUniqueId(), menuId);
    }

    public void clearOpen(Player player) {
        openMenus.remove(player.getUniqueId());
    }

    public String getOpen(Player player) {
        return openMenus.get(player.getUniqueId());
    }

    public boolean isInMenu(Player player) {
        return openMenus.containsKey(player.getUniqueId());
    }

    public void setScrollOffsetV(Player player, int offset) {
        skillScrollV.put(player.getUniqueId(), Math.max(0, Math.min(offset, SkillTreeLayout.MAX_SCROLL_V)));
    }

    public int getScrollOffsetV(Player player) {
        return skillScrollV.getOrDefault(player.getUniqueId(), SkillTreeLayout.MAX_SCROLL_V);
    }

    public void setScrollOffsetH(Player player, int offset) {
        skillScrollH.put(player.getUniqueId(), Math.max(0, Math.min(offset, SkillTreeLayout.MAX_SCROLL_H)));
    }

    public int getScrollOffsetH(Player player) {
        return skillScrollH.getOrDefault(player.getUniqueId(), 0);
    }

    public void resetScroll(Player player) {
        skillScrollV.remove(player.getUniqueId());
        skillScrollH.remove(player.getUniqueId());
    }
}
