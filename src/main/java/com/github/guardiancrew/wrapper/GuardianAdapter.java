package com.github.guardiancrew.wrapper;

import org.bukkit.entity.Player;

public class GuardianAdapter {

    private GuardianAdapter() {
        throw new UnsupportedOperationException();
    }

    public static GuardianPlayer wrapPlayer(Player bukkitPlayer) {
        // we'll do more stuff here later
        return new GuardianPlayer(bukkitPlayer);
    }

}
