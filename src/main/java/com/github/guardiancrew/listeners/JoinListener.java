package com.github.guardiancrew.listeners;

import com.github.guardiancrew.wrapper.GuardianAdapter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        GuardianAdapter.wrapPlayer(event.getPlayer());
    }
}
