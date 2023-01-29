package com.github.guardiancrew.events;

import com.github.guardiancrew.punishment.Duration;
import com.github.guardiancrew.punishment.Punishment;
import com.github.guardiancrew.punishment.PunishmentType;
import com.github.guardiancrew.wrapper.GuardianPlayer;
import jdk.internal.event.Event;
import org.bukkit.event.Cancellable;

public class PlayerPunishEvent extends Event implements Cancellable {

    private boolean cancelled;
    GuardianPlayer player;
    GuardianPlayer punisher;
    PunishmentType punishmentType;
    Duration duration;
    String reason;
    boolean ip;

    public PlayerPunishEvent(GuardianPlayer player, GuardianPlayer punisher, PunishmentType punishmentType, Duration duration, String reason, boolean ip) {
        this.player = player;
        this.punisher = punisher;
        this.punishmentType = punishmentType;
        this.duration = duration;
        this.reason = reason;
        this.ip = ip;
    }

    public GuardianPlayer getPlayer() {
        return player;
    }

    public GuardianPlayer getPunisher() {
        return punisher;
    }

    public PunishmentType getPunishmentType() {
        return punishmentType;
    }

    public Duration getDuration() {
        return duration;
    }

    public String getReason() {
        return reason;
    }

    public boolean isIp() {
        return ip;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }
}
