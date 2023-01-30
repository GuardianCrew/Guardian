package com.github.guardiancrew.staffmode;

import com.github.guardiancrew.util.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum StaffModeTool {

    TELEPORT("&9Teleport", Material.ENDER_PEARL, false, 0, () -> {
        Bukkit.broadcastMessage("Teleport Item");
    }),
    FREEZE("&bFreeze", Material.PACKED_ICE, true, 1, () -> {
        Bukkit.broadcastMessage("Freeze Item");
    });

    private final String name;
    private final Material material;
    private final boolean glowing;
    private final int slot;
    private final Runnable runnable;

    StaffModeTool(String name, Material item, boolean glowing, int slot, Runnable runnable) {
        this.name = TextUtils.format(name);
        this.material = item;
        this.glowing = glowing;
        this.slot = slot;
        this.runnable = runnable;
    }

    public String getName() {
        return name;
    }

    public boolean isGlowing() {
        return glowing;
    }

    public ItemStack getItem() {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        if (glowing)
            meta.addEnchant(Enchantment.MENDING, 1, true);
        item.setItemMeta(meta);
        return item;
    }

    public int getSlot() {
        return slot;
    }

    public void run() {
        runnable.run();
    }
}
