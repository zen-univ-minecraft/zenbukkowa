package com.zenbukkowa.gui;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MenuService {
    private final Map<UUID, String> openMenus = new HashMap<>();
    private final Map<UUID, Integer> skillScroll = new HashMap<>();

    public void setOpen(Player player, String menuId) {
        openMenus.put(player.getUniqueId(), menuId);
    }

    public void clear(Player player) {
        openMenus.remove(player.getUniqueId());
        skillScroll.remove(player.getUniqueId());
    }

    public String getOpen(Player player) {
        return openMenus.get(player.getUniqueId());
    }

    public boolean isInMenu(Player player) {
        return openMenus.containsKey(player.getUniqueId());
    }

    public void setScrollOffset(Player player, int offset) {
        skillScroll.put(player.getUniqueId(), offset);
    }

    public int getScrollOffset(Player player) {
        return skillScroll.getOrDefault(player.getUniqueId(), SkillTreeLayout.MAX_SCROLL);
    }
}
