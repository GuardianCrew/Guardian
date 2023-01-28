package com.github.guardiancrew.util;

import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.ChatColor;

import java.util.Locale;

public class TextUtils {

    private final static String[][] plurals = {

            {"fe", "ves"},// most -f words' plurals can end in -fs as well as -ves

            {"axe", "axes"},
            {"x", "xes"},

            {"ay", "ays"},
            {"ey", "eys"},
            {"iy", "iys"},
            {"oy", "oys"},
            {"uy", "uys"},
            {"kie", "kies"},
            {"zombie", "zombies"},
            {"y", "ies"},

            {"h", "hes"},

            {"man", "men"},

            {"us", "i"},

            {"hoe", "hoes"},
            {"toe", "toes"},
            {"o", "oes"},

            {"alias", "aliases"},
            {"gas", "gases"},

            {"child", "children"},

            {"sheep", "sheep"},

            // general ending
            {"", "s"},
    };

    public static Pair<String, Boolean> getEnglishPlural(String string) {
        if (string.isEmpty())
            return Pair.of("", false);
        for (String[] plural : plurals) {
            if (string.endsWith(plural[1]))
                return Pair.of(string.substring(0, string.length() - plural[1].length()) + plural[0], true);
            if (string.endsWith(plural[1].toUpperCase(Locale.ENGLISH)))
                return Pair.of(string.substring(0, string.length() - plural[1].length()) + plural[0].toLowerCase(Locale.ENGLISH), true);
        }
        return Pair.of(string, false);
    }

    public static String format(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

}
