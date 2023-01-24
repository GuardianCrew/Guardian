package com.github.guardiancrew;

import com.github.guardiancrew.command.commands.MuteCommand;
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
        registerCommands();
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

    private void registerListeners() {
        Class<? extends Listener>[] classes;
        try {
            classes = Reflect.getClasses("com.github.guardiancrew.listeners", Listener.class);
        } catch (ClassNotFoundException | URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
        for (Class<? extends Listener> c : classes) {
            try {
                Listener listener = c
                        .getDeclaredConstructor()
                        .newInstance();
               getServer().getPluginManager().registerEvents(listener, this);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerCommands() {
        new MuteCommand();
//        try {
//            Reflect.getClasses("com.github.guardiancrew.command.commands", Object.class);
//        } catch (ClassNotFoundException | URISyntaxException | IOException e) {
//            throw new RuntimeException(e);
//        }
    }

}
