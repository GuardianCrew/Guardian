package com.github.guardiancrew.punishment;

public enum PunishmentType {

    WARN("warned"),
    MUTE("muted"),
    BAN("banned");

    private final String past;

    PunishmentType(String past) {
        this.past = past;
    }

    public String getPastTense() {
        return past;
    }

}
