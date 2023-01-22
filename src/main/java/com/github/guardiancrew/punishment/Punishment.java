package com.github.guardiancrew.punishment;

import java.time.Instant;

public class Punishment {

    private final PunishmentType type;
    private Instant length;
    private boolean ip;

    public Punishment(PunishmentType type) {
        this.type = type;
    }

    public Punishment(PunishmentType type, Instant length) {
        this.type = type;
        this.length = length;
    }

    public Punishment(PunishmentType type, Instant length, boolean ip) {
        this.type = type;
        this.length = length;
        this.ip = ip;
    }

    public PunishmentType getType() {
        return type;
    }

    public Instant getLength() {
        return length;
    }

    public boolean isIp() {
        return ip;
    }
}
