package com.github.guardiancrew.punishment;

import com.github.guardiancrew.Guardian;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Duration {

    public static final Duration FOREVER = new Duration(1, ChronoUnit.FOREVER);

    private final long amount;
    private final ChronoUnit unit;
    private Instant startStamp = null;
    private Instant expirationStamp = null;
    private Runnable runnable = null;
    private boolean expired = false;
    private BukkitTask task = null;

    public Duration(ChronoUnit unit, long amount) {
        this(amount, unit);
    }

    public Duration(long amount, ChronoUnit unit) {
        this.amount = amount;
        this.unit = unit;
    }

    public void start() {
        if (task != null) {
            task.cancel();
            task = null;
            expired = false;
        }
        this.startStamp = Instant.now();
        long amount = this.amount;
        ChronoUnit unit = this.unit;
        switch (unit) {
            case MONTHS:
                amount *= 30;
                unit = ChronoUnit.DAYS;
                break;
            case YEARS:
                amount *= 365;
                unit = ChronoUnit.DAYS;
                break;
        }
        if (this != FOREVER) {
            try {
                this.expirationStamp = startStamp.plus(amount, unit);
                long seconds = expirationStamp.getEpochSecond() - startStamp.getEpochSecond();
                task = Bukkit.getScheduler().runTaskLater(Guardian.getInstance(), this::hasExpired, (seconds * 20) + 1);
                return;
            } catch (ArithmeticException ignore) {}
        }
        expirationStamp = Instant.MAX;
    }

    public Instant getStartStamp() {
        return startStamp;
    }

    public Instant getExpirationStamp() {
        return expirationStamp;
    }

    public boolean hasExpired() {
        if (expired)
            return true;
        if (Instant.now().isAfter(expirationStamp)) {
            expired = true;
            runnable.run();
            return true;
        }
        return false;
    }

    public void onExpire(Runnable runnable) {
        this.runnable = runnable;
    }

}
