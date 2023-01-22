package com.github.guardiancrew.punishment;

import lombok.Getter;

import java.time.Instant;

public class Punishment {

    @Getter
    private final PunishmentType type;
    @Getter
    private Instant length;
    @Getter
    private boolean ip;

    Punishment(PunishmentType type) {
        this.type = type;
    }

    Punishment(PunishmentType type, Instant length) {
        this.type = type;
        this.length = length;
    }

    Punishment(PunishmentType type, Instant length, boolean ip) {
        this.type = type;
        this.length = length;
        this.ip = ip;
    }
}
