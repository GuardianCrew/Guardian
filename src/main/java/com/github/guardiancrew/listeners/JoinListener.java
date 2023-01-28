package com.github.guardiancrew.listeners;

import com.github.guardiancrew.wrapper.GuardianAdapter;
import com.github.guardiancrew.wrapper.GuardianPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        GuardianPlayer player = GuardianAdapter.wrapPlayer(event.getPlayer());
        player.refresh();
    }

}
