package com.github.guardiancrew.punishment;

import java.time.Instant;

public class Punishment {

    private final PunishmentType type;
    private Instant length;
    private boolean ip;
    private Instant date;

    public Punishment(PunishmentType type) {
        this.type = type;
        date = Instant.now();
    }

    public Punishment(PunishmentType type, Instant length) {
        this.type = type;
        this.length = length;
        date = Instant.now();
    }

    public Punishment(PunishmentType type, Instant length, boolean ip) {
        this.type = type;
        this.length = length;
        this.ip = ip;
        date = Instant.now();
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

    public Instant getDate() {
        return date;
    }
}
