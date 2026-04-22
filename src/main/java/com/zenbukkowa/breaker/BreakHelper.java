package com.zenbukkowa.breaker;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

public class BreakHelper {

    public static boolean isContainer(Material mat) {
        return mat == Material.CHEST || mat == Material.TRAPPED_CHEST
                || mat == Material.BARREL || mat == Material.SHULKER_BOX
                || mat.name().endsWith("_SHULKER_BOX")
                || mat == Material.HOPPER || mat == Material.DISPENSER
                || mat == Material.DROPPER;
    }

    public static boolean isLog(Material mat) {
        return mat.name().endsWith("_LOG") || mat.name().endsWith("_WOOD");
    }

    public static int remainingBreaks(ItemStack tool, int target) {
        if (tool == null || tool.getType().isAir() || !tool.getType().isItem()) {
            return target;
        }
        var meta = tool.getItemMeta();
        if (!(meta instanceof Damageable d)) {
            return target;
        }
        int maxDurability = tool.getType().getMaxDurability();
        if (maxDurability <= 0) {
            return target;
        }
        return Math.min(target, maxDurability - d.getDamage());
    }

    public static void damageTool(ItemStack tool) {
        if (tool == null || tool.getType().isAir()) {
            return;
        }
        var meta = tool.getItemMeta();
        if (!(meta instanceof Damageable d)) {
            return;
        }
        int maxDurability = tool.getType().getMaxDurability();
        if (maxDurability <= 0) {
            return;
        }
        d.setDamage(d.getDamage() + 1);
        tool.setItemMeta(d);
        if (d.getDamage() >= maxDurability) {
            tool.setAmount(0);
        }
    }
}
