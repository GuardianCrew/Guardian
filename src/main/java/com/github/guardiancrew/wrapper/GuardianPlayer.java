package com.github.guardiancrew.wrapper;

import com.github.guardiancrew.punishment.Punishment;
import com.github.guardiancrew.staffmode.StaffModeTools;
import com.github.guardiancrew.util.TextUtils;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Date;

public class GuardianPlayer {

    private final Player bukkitPlayer;
    private boolean muted = false;

    GuardianPlayer(Player bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;
    }

    public Player getBukkitPlayer() {
        return bukkitPlayer;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public boolean isMuted() {
        return muted;
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

    public void staffMode(boolean activated) {
        if (activated) {
            getBukkitPlayer().saveData();
            getBukkitPlayer().getInventory().clear();
            getBukkitPlayer().sendMessage(TextUtils.translate("&3&lGuardian&3: &cThis message (maybe the prefix too?) can be edited in the config."));
            for (StaffModeTools tool : StaffModeTools.values()) {
                getBukkitPlayer().getInventory().addItem(tool.getItem());
            }
        } else {
            getBukkitPlayer().loadData();
        }
    }

    public void sendMessage(String message) {
        bukkitPlayer.sendMessage(message);
    }

}
