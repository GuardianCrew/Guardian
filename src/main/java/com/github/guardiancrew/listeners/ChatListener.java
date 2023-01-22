package com.github.guardiancrew.listeners;

import com.github.guardiancrew.wrapper.GuardianAdapter;
import com.github.guardiancrew.wrapper.GuardianPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(PlayerChatEvent event) {
        GuardianPlayer player = GuardianAdapter.getPlayer(event.getPlayer());
        if (player.isMuted())
            event.setCancelled(true);
    }
}
