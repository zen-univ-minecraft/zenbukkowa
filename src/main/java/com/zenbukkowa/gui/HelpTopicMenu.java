package com.zenbukkowa.gui;

import com.zenbukkowa.domain.LocaleService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.Map;

public class HelpTopicMenu {

    private static final Map<String, TopicDef> TOPICS = Map.of(
            "rules", new TopicDef(List.of(
                    new ChildDef(Material.GOLD_INGOT, "help.rules.children.overview"),
                    new ChildDef(Material.IRON_PICKAXE, "help.rules.children.scoring"),
                    new ChildDef(Material.BEDROCK, "help.rules.children.protection")
            )),
            "points", new TopicDef(List.of(
                    new ChildDef(Material.DIAMOND_ORE, "help.points.children.ores"),
                    new ChildDef(Material.GRASS_BLOCK, "help.points.children.terra"),
                    new ChildDef(Material.PRISMARINE, "help.points.children.aquatic"),
                    new ChildDef(Material.COMPASS, "help.points.children.discovery")
            )),
            "skills", new TopicDef(List.of(
                    new ChildDef(Material.EXPERIENCE_BOTTLE, "help.skills.children.tiers"),
                    new ChildDef(Material.BOOK, "help.skills.children.prerequisites"),
                    new ChildDef(Material.EMERALD, "help.skills.children.costs")
            )),
            "breaking", new TopicDef(List.of(
                    new ChildDef(Material.DIAMOND_PICKAXE, "help.breaking.children.radius"),
                    new ChildDef(Material.CHEST, "help.breaking.children.protection")
            )),
            "structures", new TopicDef(List.of(
                    new ChildDef(Material.PRISMARINE_BRICKS, "help.structures.children.monument"),
                    new ChildDef(Material.DARK_OAK_LOG, "help.structures.children.mansion")
            )),
            "commands", new TopicDef(List.of(
                    new ChildDef(Material.REDSTONE_BLOCK, "help.commands.children.op"),
                    new ChildDef(Material.BOOK, "help.commands.children.player")
            ))
    );

    public static void open(Player player, MenuService menuService, LocaleService locale, String topicKey) {
        String titleKey = "menu.help_title_" + topicKey;
        String title = locale.get(player.getUniqueId(), titleKey);
        if (title.startsWith("menu.")) title = topicKey;
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.BLACK + title);

        TopicDef def = TOPICS.get(topicKey);
        if (def != null) {
            int slot = 10;
            for (ChildDef child : def.children()) {
                if (slot >= 44) break;
                String label = locale.get(player.getUniqueId(), child.baseKey() + ".label");
                inv.setItem(slot, MenuItems.create(child.material(), ChatColor.YELLOW + label));
                slot++;
                if (slot % 9 == 8) {
                    slot += 2;
                }
            }
        }

        inv.setItem(49, MenuItems.create(Material.ARROW, ChatColor.WHITE + locale.get(player.getUniqueId(), "menu.back")));
        MenuItems.fillEmpty(inv);
        player.openInventory(inv);
        menuService.setOpen(player, "help_" + topicKey);
    }

    public static void onClick(Player player, MenuService menuService, LocaleService locale, String topicKey, int slot) {
        if (slot == 49) {
            HelpMenu.open(player, menuService, locale);
            return;
        }
        TopicDef def = TOPICS.get(topicKey);
        if (def == null) return;
        int index = slotToChildIndex(slot);
        if (index < 0 || index >= def.children().size()) return;
        ChildDef child = def.children().get(index);
        String text = locale.get(player.getUniqueId(), child.baseKey() + ".text");
        if (text.startsWith("help.")) {
            text = locale.get(player.getUniqueId(), child.baseKey() + ".label");
        }
        for (String line : text.split("\n")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
        }
    }

    private static int slotToChildIndex(int slot) {
        if (slot < 10 || slot >= 44) return -1;
        int row = slot / 9;
        int col = slot % 9;
        if (col == 0 || col == 8) return -1;
        return (row - 1) * 7 + (col - 1);
    }

    private record TopicDef(List<ChildDef> children) {}
    private record ChildDef(Material material, String baseKey) {}
}
