package com.zenbukkowa.gui;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MenuService {
    private final Map<UUID, String> openMenus = new HashMap<>();

    public void setOpen(Player player, String menuId) {
        openMenus.put(player.getUniqueId(), menuId);
    }

    public void clear(Player player) {
        openMenus.remove(player.getUniqueId());
    }

    public String getOpen(Player player) {
        return openMenus.get(player.getUniqueId());
    }

    public boolean isInMenu(Player player) {
        return openMenus.containsKey(player.getUniqueId());
    }
}
