package com.github.guardiancrew.command.argument;

import com.github.guardiancrew.command.exception.CommandParseException;
import com.github.guardiancrew.command.util.CompletionUtils;
import com.github.guardiancrew.command.util.StringReader;
import com.github.guardiancrew.wrapper.GuardianAdapter;
import com.github.guardiancrew.wrapper.GuardianPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerArgument implements Argument<GuardianPlayer> {

    @Override
    public GuardianPlayer parse(CommandSender executor, StringReader reader) {
        String playerName = reader.readPlayerName();
        Player bukkitPlayer = Bukkit.getPlayerExact(playerName);
        if (bukkitPlayer == null)
            throw new CommandParseException("Cannot find player '" + playerName + '\'');
        return GuardianAdapter.wrapPlayer(bukkitPlayer);
    }

    @Override
    public List<String> tabComplete(CommandSender executor, StringReader reader) {
        String playerName = reader.readPlayerName();
        List<String> playerNames = Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .collect(Collectors.toList());
        return CompletionUtils.filterCompletions(playerName, playerNames);
    }

}
