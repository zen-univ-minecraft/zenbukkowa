package com.zenbukkowa.gui;

import com.zenbukkowa.domain.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SkillTreeViewport {

    public static void render(Inventory inv, Player player, int scrollOffset,
                              SkillService skillService, PointService pointService,
                              LocaleService locale) {
        PlayerSkills skills = skillService.getSkills(player.getUniqueId());
        PlayerProgress progress = pointService.getProgress(player.getUniqueId());

        // clear viewport area
        for (int i = 0; i < 45; i++) {
            inv.setItem(i, null);
        }

        // connections
        for (SkillTreeLayout.Connection conn : SkillTreeLayout.connections()) {
            int slot = toSlot(conn.row(), conn.col(), scrollOffset);
            if (slot >= 0 && slot < 45) {
                inv.setItem(slot, MenuItems.filler(Material.STICK));
            }
        }

        // skill nodes
        for (SkillTreeLayout.Node node : SkillTreeLayout.nodes()) {
            int slot = toSlot(node.row(), node.col(), scrollOffset);
            if (slot < 0 || slot >= 45) continue;
            ItemStack item = buildSkillItem(player, node.skill(), skills, progress, skillService, locale);
            inv.setItem(slot, item);
        }

        // controls
        boolean canUp = scrollOffset > 0;
        boolean canDown = scrollOffset < SkillTreeLayout.MAX_SCROLL;
        inv.setItem(45, canUp ? MenuItems.create(Material.ARROW, ChatColor.WHITE + locale.get(player.getUniqueId(), "menu.scroll_up"))
                : MenuItems.filler(Material.GRAY_STAINED_GLASS_PANE));
        inv.setItem(46, MenuItems.create(Material.PAPER, ChatColor.GRAY + "Page " + (scrollOffset + 1) + "/" + (SkillTreeLayout.MAX_SCROLL + 1)));
        inv.setItem(49, MenuItems.create(Material.ARROW, ChatColor.WHITE + locale.get(player.getUniqueId(), "menu.back")));
        inv.setItem(53, canDown ? MenuItems.create(Material.ARROW, ChatColor.WHITE + locale.get(player.getUniqueId(), "menu.scroll_down"))
                : MenuItems.filler(Material.GRAY_STAINED_GLASS_PANE));

        // fill remaining nulls with gray panes
        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null) inv.setItem(i, MenuItems.filler(Material.GRAY_STAINED_GLASS_PANE));
        }
    }

    public static SkillType skillAtSlot(int slot, int scrollOffset) {
        int row = slot / 9 + scrollOffset;
        int col = slot % 9;
        for (SkillTreeLayout.Node node : SkillTreeLayout.nodes()) {
            if (node.row() == row && node.col() == col) return node.skill();
        }
        return null;
    }

    private static int toSlot(int row, int col, int scrollOffset) {
        int viewRow = row - scrollOffset;
        if (viewRow < 0 || viewRow >= SkillTreeLayout.VIEWPORT_ROWS) return -1;
        return viewRow * 9 + col;
    }

    private static ItemStack buildSkillItem(Player player, SkillType skill, PlayerSkills skills,
                                            PlayerProgress progress, SkillService skillService,
                                            LocaleService locale) {
        int currentTier = skills.tier(skill);
        boolean maxed = currentTier >= skill.maxTier();
        boolean canBuy = !maxed && skillService.canPurchase(player.getUniqueId(), skill, currentTier + 1);
        int cost = maxed ? 0 : skill.cost(currentTier + 1);

        List<String> lore = new ArrayList<>();
        String desc = locale.get(player.getUniqueId(), "skill." + skill.name().toLowerCase() + ".description");
        if (!desc.startsWith("skill.")) lore.add(ChatColor.GRAY + desc);

        if (currentTier > 0) lore.add(ChatColor.GREEN + "Current: tier " + currentTier);
        if (!maxed) {
            lore.add(ChatColor.YELLOW + "Next: tier " + (currentTier + 1));
            lore.add(ChatColor.GOLD + "Cost: " + cost + " " + skill.category() + " Points");
            long balance = progress.points(skill.category());
            lore.add(ChatColor.GRAY + "You have: " + balance);
            if (!canBuy) lore.add(balance < cost ? ChatColor.RED + "Insufficient points" : ChatColor.RED + "Missing prerequisite");
        } else {
            lore.add(ChatColor.DARK_GREEN + "Maxed");
        }

        Material mat = maxed ? Material.LIME_STAINED_GLASS_PANE
                : (canBuy ? Material.GREEN_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE);
        String name = categoryColor(skill.category()) + skill.name().replace('_', ' ')
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
        };
    }
}
