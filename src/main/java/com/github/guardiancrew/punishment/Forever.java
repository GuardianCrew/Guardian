package com.github.guardiancrew.punishment;

import java.time.temporal.ChronoUnit;

public class Forever extends Duration {

    public Forever() {
        super(1, ChronoUnit.FOREVER);
    }

    @Override
    public boolean hasExpired() {
        return false;
    }

    @Override
    public String toReadableString() {
        return "forever";
    }

}
