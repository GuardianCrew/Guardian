package com.github.guardiancrew.wrapper;

import com.github.guardiancrew.punishment.Punishment;
import lombok.Getter;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Date;

public class GuardianPlayer {

    private final Player bukkitPlayer;
    @Getter
    private boolean muted = false;

    GuardianPlayer(Player bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;
    }

    public Player getBukkitPlayer() {
        return bukkitPlayer;
    }

    public void punish(Punishment punishment) {
        switch (punishment.getType()) {
            case KICK:
                getBukkitPlayer().kickPlayer("Kick Message: The message will be in the config.");
                break;
            case MUTE:
                muted = true;
                getBukkitPlayer().sendMessage("Mute Message: The message will be in the config.");
                break;
            case BAN:
                if (punishment.isIp())
                    Bukkit.banIP(bukkitPlayer.getAddress().toString());
                else
                    Bukkit.getBanList(BanList.Type.NAME).addBan(getBukkitPlayer().getName(), "Ban Message: The message will be in the config.", Date.from(punishment.getLength()), "Source Thing");
                break;
        }
    }
}
