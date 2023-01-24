package com.github.guardiancrew.wrapper;

import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GuardianAdapter {

    private GuardianAdapter() {
        throw new UnsupportedOperationException();
    }

    private static final Map<Player, GuardianPlayer> guardianPlayers = Collections.synchronizedMap(new HashMap<>());

    public static GuardianPlayer wrapPlayer(Player bukkitPlayer) {
        // we'll do more stuff here later
        return guardianPlayers.computeIfAbsent(bukkitPlayer, GuardianPlayer::new);
    }

    public static GuardianPlayer getPlayer(Player bukkitPlayer) {
        if (!guardianPlayers.containsKey(bukkitPlayer))
            wrapPlayer(bukkitPlayer);
        return guardianPlayers.get(bukkitPlayer);
    }
}
