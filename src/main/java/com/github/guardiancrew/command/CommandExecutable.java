package com.github.guardiancrew.command;

import com.github.guardiancrew.command.context.CommandContext;
import org.bukkit.command.CommandSender;

@FunctionalInterface
public interface CommandExecutable {

    boolean execute(CommandSender executor, CommandContext context);

}
