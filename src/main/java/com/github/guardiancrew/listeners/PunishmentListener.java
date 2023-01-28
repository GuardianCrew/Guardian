package com.github.guardiancrew.listeners;

import com.github.guardiancrew.punishment.PunishmentType;
import com.github.guardiancrew.util.TextUtils;
import com.github.guardiancrew.wrapper.GuardianAdapter;
import com.github.guardiancrew.wrapper.GuardianPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent.Result;

@SuppressWarnings("deprecation")
public class PunishmentListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(PlayerChatEvent event) {
        GuardianPlayer player = GuardianAdapter.wrapPlayer(event.getPlayer());
        if (player.hasPunishment(PunishmentType.MUTE)) {
            event.setCancelled(true);
            player.sendPluginMessage("&7You are currently &cmuted&7.");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerPreLoginEvent event) {
        GuardianPlayer player = GuardianAdapter.wrapPlayer(event.getUniqueId());
        if (player.hasPunishment(PunishmentType.BAN))
            event.disallow(Result.KICK_BANNED, TextUtils.format("&7You are &cbanned&7."));
    }

}
