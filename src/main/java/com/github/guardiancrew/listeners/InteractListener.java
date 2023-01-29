package com.github.guardiancrew.listeners;

import com.github.guardiancrew.wrapper.GuardianAdapter;
import com.github.guardiancrew.wrapper.GuardianPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        GuardianPlayer player = GuardianAdapter.wrapPlayer(event.getPlayer());
        if (player.inStaffMode())
            event.setCancelled(true);
    }

}
