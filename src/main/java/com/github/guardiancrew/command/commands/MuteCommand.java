package com.github.guardiancrew.command.commands;

import com.github.guardiancrew.command.CommandManager;
import com.github.guardiancrew.command.GuardianCommand;
import com.github.guardiancrew.command.argument.DurationArgument;
import com.github.guardiancrew.command.argument.PlayerArgument;
import com.github.guardiancrew.command.argument.ReasonArgument;
import com.github.guardiancrew.punishment.Duration;
import com.github.guardiancrew.wrapper.GuardianPlayer;

public class MuteCommand {

    static {

        CommandManager.registerCommand(GuardianCommand.builder("mute")
                .permission("guardian.mute")
                .argument("target", new PlayerArgument())
                .argument("duration", new DurationArgument(), true)
                .argument("reason", new ReasonArgument(), true)
                .execute((executor, context) -> {

                    GuardianPlayer player = context.getArgument("target", GuardianPlayer.class);
                    Duration duration = context.getOptionalArgument("duration", Duration.class).orElse(Duration.FOREVER);
                    String reason = context.getOptionalArgument("reason", String.class).orElse("No reason specified.");

                    player.sendMessage("You have been muted for " + reason);
                    duration.onExpire(() -> player.sendMessage("You have been unmuted"));
                    duration.start();

                    return true;
                })
                .build());

    }

}
