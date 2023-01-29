package com.github.guardiancrew.command.commands;

import com.github.guardiancrew.command.CommandManager;
import com.github.guardiancrew.command.GuardianCommand;
import com.github.guardiancrew.wrapper.GuardianAdapter;
import com.github.guardiancrew.wrapper.GuardianPlayer;

public class StaffModeCommand {

    static {
        CommandManager.registerCommand(GuardianCommand.builder("staffmode")
                .permission("guardian.staffmode")
                .execute((executor, context) -> {
                    GuardianPlayer player = GuardianAdapter.wrapPlayer(executor);
                    player.setStaffMode(!player.inStaffMode());
                    return true;
                })
                .build());
    }

}
