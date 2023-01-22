package com.github.guardiancrew;

import com.github.guardiancrew.util.Reflect;
import com.github.guardiancrew.util.Version;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Guardian extends JavaPlugin {

    private static Guardian instance;

    private static Version minecraftVersion;

    public static Guardian getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        updateMinecraftVersion();
    }

    @Override
    public void onEnable() {
        instance = this;

        registerListeners();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private static final Pattern bukkitVersionPattern = Pattern.compile("\\d+\\.\\d+(?:\\.\\d+)?");

    private static void updateMinecraftVersion() {
        String bukkitVersion = Bukkit.getBukkitVersion();
        Matcher matcher = bukkitVersionPattern.matcher(bukkitVersion);
        if (matcher.find())
            minecraftVersion = new Version(matcher.group());
    }

    public static Version getMinecraftVersion() {
        return minecraftVersion;
    }

    public static boolean isRunningMinecraft(int major, int minor) {
        return minecraftVersion.compareTo(major, minor) >= 0;
    }

    public static boolean isRunningMinecraft(int major, int minor, int patch) {
        return minecraftVersion.compareTo(major, minor, patch) >= 0;
    }

    public static boolean isRunningMinecraft(Version version) {
        return minecraftVersion.compareTo(version) >= 0;
    }

    private static void registerListeners() {
        Guardian instance = getInstance();
        Class<?>[] classes;
        try {
            classes = Reflect.getClasses("com.github.guardiancrew.listeners", Listener.class);
        } catch (ClassNotFoundException | URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
        if (classes == null) return;
        for (Class<?> c : classes) {
            try {
                Listener listener = (Listener) c
                        .getDeclaredConstructor()
                        .newInstance();
               instance.getServer().getPluginManager().registerEvents(listener, instance);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }
}
