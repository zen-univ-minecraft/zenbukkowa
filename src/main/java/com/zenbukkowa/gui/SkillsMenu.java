package com.zenbukkowa.gui;

import com.zenbukkowa.domain.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class SkillsMenu {

    public static void open(Player player, MenuService menuService,
                            SkillService skillService, PointService pointService) {
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.BLACK + "Skills");
        PlayerSkills skills = skillService.getSkills(player.getUniqueId());
        PlayerProgress progress = pointService.getProgress(player.getUniqueId());

        int slot = 0;
        for (SkillType skill : SkillType.values()) {
            if (slot >= 45) break;
            int currentTier = skills.tier(skill);
            boolean maxed = currentTier >= skill.maxTier();
            boolean canBuy = !maxed && skillService.canPurchase(player.getUniqueId(), skill, currentTier + 1);
            int cost = maxed ? 0 : skill.cost(currentTier + 1);

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Category: " + skill.category());
            if (currentTier > 0) {
                lore.add(ChatColor.GREEN + "Current: tier " + currentTier + " (value " + skill.tierValue(currentTier) + ")");
            }
            if (!maxed) {
                lore.add(ChatColor.YELLOW + "Next: tier " + (currentTier + 1) + " (value " + skill.tierValue(currentTier + 1) + ")");
                lore.add(ChatColor.GOLD + "Cost: " + cost + " " + skill.category() + " Points");
                long balance = progress.points(skill.category());
                lore.add(ChatColor.GRAY + "You have: " + balance);
                if (!canBuy) {
                    if (balance < cost) {
                        lore.add(ChatColor.RED + "Insufficient points");
                    } else {
                        lore.add(ChatColor.RED + "Missing prerequisite");
                    }
                }
            } else {
                lore.add(ChatColor.DARK_GREEN + "Maxed");
            }

            Material mat = maxed ? Material.LIME_STAINED_GLASS_PANE : (canBuy ? Material.GREEN_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE);
            String name = ChatColor.WHITE + skill.name().replace('_', ' ') + (maxed ? "" : " (Tier " + currentTier + ")");
            inv.setItem(slot, MenuItems.named(mat, name, lore));
            slot++;
        }

        inv.setItem(49, MenuItems.create(Material.ARROW, ChatColor.WHITE + "Back"));
        fillEmpty(inv);
        player.openInventory(inv);
        menuService.setOpen(player, "skills");
    }

    private static void fillEmpty(Inventory inv) {
        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, MenuItems.filler(Material.GRAY_STAINED_GLASS_PANE));
            }
        }
    }
}
