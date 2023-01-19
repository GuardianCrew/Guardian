package com.github.guardian;

import org.bukkit.plugin.java.JavaPlugin;

public final class Guardian extends JavaPlugin {

    private static Guardian instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Guardian getInstance() {
        return instance;
    }

}
