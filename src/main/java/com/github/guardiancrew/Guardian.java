package com.github.guardiancrew;

import com.github.guardiancrew.util.Version;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.ServiceLoader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
        for (Class<?> c : new Reflections(getInstance().getClass().getPackageName() + ".listeners").getSubTypesOf(Listener.class)) {
            try {
                Listener listener = (Listener) c
                        .getDeclaredConstructor()
                        .newInstance();
                getInstance().getServer().getPluginManager().registerEvents(listener, getInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }
}
