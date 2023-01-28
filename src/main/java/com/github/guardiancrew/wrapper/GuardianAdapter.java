package com.github.guardiancrew.wrapper;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class GuardianAdapter {

    private GuardianAdapter() {
        throw new UnsupportedOperationException();
    }

    static final Map<UUID, GuardianPlayer> guardianPlayers = Collections.synchronizedMap(new HashMap<>());

    @Nullable
    public static GuardianPlayer wrapPlayer(CommandSender commandSender) {
        return commandSender instanceof Player ? wrapPlayer((Player) commandSender) : null;
    }

    public static GuardianPlayer wrapPlayer(Player bukkitPlayer) {
        return guardianPlayers.computeIfAbsent(bukkitPlayer.getUniqueId(), uuid -> new GuardianPlayer(bukkitPlayer));
    }

    public static GuardianPlayer wrapPlayer(UUID uuid) {
        return guardianPlayers.computeIfAbsent(uuid, GuardianPlayer::new);
    }

    public static List<GuardianPlayer> getOnlinePlayers() {
        return Bukkit.getOnlinePlayers().stream()
                .map(GuardianAdapter::wrapPlayer)
                .collect(Collectors.toList());
    }

    public static Collection<GuardianPlayer> getAllPlayers() {
        return guardianPlayers.values();
    }

}
