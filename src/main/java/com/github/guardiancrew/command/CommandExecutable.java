package com.github.guardiancrew.command;

import com.github.guardiancrew.command.context.CommandContext;
import com.github.guardiancrew.wrapper.GuardianPlayer;

@FunctionalInterface
public interface CommandExecutable {

    boolean execute(GuardianPlayer executor, CommandContext context);

}
