package com.zenbukkowa.gui;

import com.zenbukkowa.domain.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SkillTreeViewport {

    private static final Set<String> BOUNDS = computeBounds();

    public static void render(Inventory inv, Player player, int scrollV, int scrollH,
                              SkillService skillService, PointService pointService,
                              LocaleService locale) {
        PlayerSkills skills = skillService.getSkills(player.getUniqueId());
        PlayerProgress progress = pointService.getProgress(player.getUniqueId());

        for (int i = 0; i < 54; i++) {
            inv.setItem(i, null);
        }

        for (SkillTreeLayout.Connection conn : SkillTreeLayout.connections()) {
            int slot = toSlot(conn.row(), conn.col(), scrollV, scrollH);
            if (slot >= 0 && slot < 45) {
                inv.setItem(slot, MenuItems.filler(Material.GREEN_STAINED_GLASS_PANE));
            }
        }

        for (SkillTreeLayout.Node node : SkillTreeLayout.nodes()) {
            int slot = toSlot(node.row(), node.col(), scrollV, scrollH);
            if (slot < 0 || slot >= 45) continue;
            ItemStack item = buildSkillItem(player, node.skill(), skills, progress, skillService, locale);
            inv.setItem(slot, item);
        }

        boolean canUp = scrollV > 0;
        boolean canDown = scrollV < SkillTreeLayout.MAX_SCROLL_V;
        boolean canLeft = scrollH > 0;
        boolean canRight = scrollH < SkillTreeLayout.MAX_SCROLL_H;

        for (int slot = 0; slot < 45; slot++) {
            if (inv.getItem(slot) != null) continue;
            int viewRow = slot / 9;
            int viewCol = slot % 9;
            int row = viewRow + scrollV;
            int col = viewCol + scrollH;
            String key = row + "," + col;
            Material filler = BOUNDS.contains(key) ? Material.LIGHT_GRAY_STAINED_GLASS_PANE : Material.GRAY_STAINED_GLASS_PANE;
            inv.setItem(slot, MenuItems.filler(filler));
        }

        inv.setItem(45, MenuItems.create(Material.ARROW, ChatColor.WHITE + locale.get(player.getUniqueId(), "menu.back")));
        inv.setItem(46, MenuItems.create(Material.PAPER, ChatColor.GRAY + "V " + (scrollV + 1) + "/" + (SkillTreeLayout.MAX_SCROLL_V + 1)
                + " . H " + (scrollH + 1) + "/" + (SkillTreeLayout.MAX_SCROLL_H + 1)));
        inv.setItem(47, canLeft ? MenuItems.create(Material.ARROW, ChatColor.WHITE + locale.get(player.getUniqueId(), "menu.scroll_left"))
                : MenuItems.filler(Material.GRAY_STAINED_GLASS_PANE));
        inv.setItem(48, canUp ? MenuItems.create(Material.ARROW, ChatColor.WHITE + locale.get(player.getUniqueId(), "menu.scroll_up"))
                : MenuItems.filler(Material.GRAY_STAINED_GLASS_PANE));
        inv.setItem(49, canDown ? MenuItems.create(Material.ARROW, ChatColor.WHITE + locale.get(player.getUniqueId(), "menu.scroll_down"))
                : MenuItems.filler(Material.GRAY_STAINED_GLASS_PANE));
        inv.setItem(50, canRight ? MenuItems.create(Material.ARROW, ChatColor.WHITE + locale.get(player.getUniqueId(), "menu.scroll_right"))
                : MenuItems.filler(Material.GRAY_STAINED_GLASS_PANE));

        for (int i = 51; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null) inv.setItem(i, MenuItems.filler(Material.GRAY_STAINED_GLASS_PANE));
        }
    }

    public static SkillType skillAtSlot(int slot, int scrollV, int scrollH) {
        int viewRow = slot / 9;
        int viewCol = slot % 9;
        int row = viewRow + scrollV;
        int col = viewCol + scrollH;
        for (SkillTreeLayout.Node node : SkillTreeLayout.nodes()) {
            if (node.row() == row && node.col() == col) return node.skill();
        }
        return null;
    }

    private static int toSlot(int row, int col, int scrollV, int scrollH) {
        int viewRow = row - scrollV;
        int viewCol = col - scrollH;
        if (viewRow < 0 || viewRow >= SkillTreeLayout.VIEWPORT_ROWS) return -1;
        if (viewCol < 0 || viewCol >= SkillTreeLayout.VIEWPORT_COLS) return -1;
        return viewRow * 9 + viewCol;
    }

    private static Set<String> computeBounds() {
        Set<String> set = new HashSet<>();
        for (SkillTreeLayout.Node node : SkillTreeLayout.nodes()) {
            set.add(node.row() + "," + node.col());
        }
        for (SkillTreeLayout.Connection conn : SkillTreeLayout.connections()) {
            set.add(conn.row() + "," + conn.col());
        }
        return set;
    }

    private static ItemStack buildSkillItem(Player player, SkillType skill, PlayerSkills skills,
                                            PlayerProgress progress, SkillService skillService,
                                            LocaleService locale) {
        int currentTier = skills.tier(skill);
        boolean maxed = currentTier >= skill.maxTier();
        boolean canBuy = !maxed && skillService.canPurchase(player.getUniqueId(), skill, currentTier + 1);

        List<String> lore = new ArrayList<>();
        String desc = locale.get(player.getUniqueId(), "skill." + skill.name().toLowerCase() + ".description");
        if (!desc.startsWith("skill.")) lore.add(ChatColor.GRAY + desc);

        SkillType parent = SkillTreeLayout.parentOf(skill);
        if (parent != null) {
            String parentName = parent.name().replace('_', ' ');
            lore.add(ChatColor.DARK_AQUA + "Requires: " + parentName);
        }

        if (currentTier > 0) lore.add(ChatColor.GREEN + "Current: tier " + currentTier);
        if (!maxed) {
            lore.add(ChatColor.YELLOW + "Next: tier " + (currentTier + 1));
            Map<PointCategory, Integer> costs = skill.tierCost(currentTier + 1);
            lore.add(ChatColor.GOLD + "Cost:");
            for (Map.Entry<PointCategory, Integer> e : costs.entrySet()) {
                long balance = progress.points(e.getKey());
                lore.add(ChatColor.GRAY + "  " + e.getKey() + ": " + e.getValue()
                        + ChatColor.DARK_GRAY + " (have " + balance + ")");
            }
            boolean anyShort = costs.entrySet().stream().anyMatch(e -> progress.points(e.getKey()) < e.getValue());
            if (!canBuy) lore.add(anyShort ? ChatColor.RED + "Insufficient points" : ChatColor.RED + "Missing prerequisite");
        } else {
            lore.add(ChatColor.DARK_GREEN + "Maxed");
        }

        Material mat = maxed ? Material.EMERALD_BLOCK
                : (canBuy ? Material.GREEN_WOOL : Material.GRAY_WOOL);
        ChatColor color = skill.isMythic() ? ChatColor.LIGHT_PURPLE : categoryColor(skill.category());
        String name = color + skill.name().replace('_', ' ')
                + (maxed ? "" : " (Tier " + currentTier + ")");

        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RESET + name);
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    private static ChatColor categoryColor(PointCategory cat) {
        return switch (cat) {
            case TERRA -> ChatColor.GREEN;
            case MINERAL -> ChatColor.AQUA;
            case ORGANIC -> ChatColor.DARK_GREEN;
            case AQUATIC -> ChatColor.BLUE;
            case VOID -> ChatColor.DARK_PURPLE;
            case CROP -> ChatColor.YELLOW;
            case DISCOVERY -> ChatColor.LIGHT_PURPLE;
        };
    }
}
