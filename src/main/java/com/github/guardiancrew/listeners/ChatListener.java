package com.github.guardiancrew.listeners;

import com.github.guardiancrew.wrapper.GuardianAdapter;
import com.github.guardiancrew.wrapper.GuardianPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        GuardianPlayer player = GuardianAdapter.wrapPlayer(event.getPlayer());
        if (player.isMuted())
            event.setCancelled(true);
    }
}
