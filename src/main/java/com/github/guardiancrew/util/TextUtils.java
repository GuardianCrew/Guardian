package com.github.guardiancrew.util;

import org.bukkit.ChatColor;

public class TextUtils {

    public static String translate(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
