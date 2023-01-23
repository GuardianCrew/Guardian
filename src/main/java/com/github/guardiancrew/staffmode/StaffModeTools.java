package com.github.guardiancrew.staffmode;

import com.github.guardiancrew.util.TextUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum StaffModeTools {

    TELEPORT("&9Teleport", Material.ENDER_PEARL, false),
    FREEZE("&bFreeze", Material.PACKED_ICE, true);

    private final String name;
    private final Material material;
    private final boolean glowing;

    StaffModeTools(String name, Material item, boolean glowing) {
        this.name = TextUtils.translate(name);
        this.material = item;
        this.glowing = glowing;
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
}
