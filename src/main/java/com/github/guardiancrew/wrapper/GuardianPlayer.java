package com.github.guardiancrew.wrapper;

import org.bukkit.entity.Player;

public class GuardianPlayer {

    private final Player bukkitPlayer;

    GuardianPlayer(Player bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;
    }

    public Player getBukkitPlayer() {
        return bukkitPlayer;
    }

}
