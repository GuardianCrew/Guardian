package com.github.guardiancrew.command.commands;

import com.github.guardiancrew.command.Command;
import com.github.guardiancrew.command.CommandManager;
import com.github.guardiancrew.command.GuardianCommand;
import com.github.guardiancrew.command.argument.DurationArgument;
import com.github.guardiancrew.command.argument.PlayerArgument;
import com.github.guardiancrew.command.argument.ReasonArgument;
import com.github.guardiancrew.command.context.CommandContext;
import com.github.guardiancrew.punishment.Duration;
import com.github.guardiancrew.punishment.PunishmentType;
import com.github.guardiancrew.wrapper.GuardianAdapter;
import com.github.guardiancrew.wrapper.GuardianPlayer;

public class PunishCommands implements Command {

    static {
        CommandManager.registerCommand(GuardianCommand.builder("warn")
                .permission("guardian.punish.warn")
                .argument("target", new PlayerArgument())
                .argument("reason", new ReasonArgument(), true)
                .execute((executor, context) -> punish(PunishmentType.WARN, GuardianAdapter.wrapPlayer(executor), context, false))
                .build());

        CommandManager.registerCommand(GuardianCommand.builder("mute")
                .permission("guardian.punish.mute")
                .argument("target", new PlayerArgument())
                .argument("duration", new DurationArgument(), true)
                .argument("reason", new ReasonArgument(), true)
                .execute((executor, context) -> punish(PunishmentType.MUTE, GuardianAdapter.wrapPlayer(executor), context, false))
                .build());

        CommandManager.registerCommand(GuardianCommand.builder("kick")
                .permission("guardian.punish.kick")
                .argument("target", new PlayerArgument())
                .argument("reason", new ReasonArgument(), true)
                .execute((executor, context) -> {
                    GuardianPlayer target = context.getArgument("target", GuardianPlayer.class);
                    String reason = context.getOptionalArgument("reason", String.class).orElse("No reason specified.");
                    target.kick(GuardianAdapter.wrapPlayer(executor), reason);
                    return true;
                })
                .build());

        CommandManager.registerCommand(GuardianCommand.builder("ban")
                .permission("guardian.punish.ban")
                .argument("target", new PlayerArgument())
                .argument("duration", new DurationArgument(), true)
                .argument("reason", new ReasonArgument(), true)
                .execute((executor, context) -> punish(PunishmentType.BAN, GuardianAdapter.wrapPlayer(executor), context, false))
                .build());

    }

    private static boolean punish(PunishmentType type, GuardianPlayer punisher, CommandContext context, boolean ip) {
        GuardianPlayer target = context.getArgument("target", GuardianPlayer.class);
        Duration duration = type == PunishmentType.WARN ? Duration.forever() : context.getOptionalArgument("duration", Duration.class).orElse(Duration.forever());
        String reason = context.getOptionalArgument("reason", String.class).orElse("No reason specified");

        target.punish(type, punisher, duration, reason, ip);
        return true;
    }

}
