package com.github.guardiancrew.command;

import com.github.guardiancrew.util.Reflect;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;

public final class CommandManager {

    private static final String PREFIX = "guardian";
    private static CommandMap commandMap;

    private CommandManager() {
        throw new UnsupportedOperationException();
    }

    static {
        if (Reflect.methodExists(Server.class, "getCommandMap")) {
            commandMap = Bukkit.getServer().getCommandMap();
        } else {
            if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
                try {
                    Field field = SimplePluginManager.class.getDeclaredField("commandMap");
                    field.setAccessible(true);
                    commandMap = (CommandMap) field.get(Bukkit.getPluginManager());
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void registerCommand(GuardianCommand command) {
        commandMap.register(PREFIX, command.asBukkitCommand());
    }

}
