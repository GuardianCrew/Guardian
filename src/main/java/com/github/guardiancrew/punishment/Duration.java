package com.github.guardiancrew.punishment;

import com.github.guardiancrew.Guardian;
import com.github.guardiancrew.util.FriendlyByteBuf;
import java.lang.Runnable;
import com.github.guardiancrew.util.TextUtils;
import com.github.guardiancrew.util.Writable;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class Duration implements Writable {

    private final long amount;
    private final ChronoUnit unit;
    protected Instant startStamp = null;
    protected Instant expirationStamp = null;
    private Runnable runnable = null;
    private boolean started = false;
    private boolean expired = false;
    private boolean ran = false;
    private BukkitTask task = null;

    public static Forever forever() {
        return new Forever();
    }

    public static Duration of(ChronoUnit unit, long amount) {
        return of(amount, unit);
    }

    public static Duration of(long amount, ChronoUnit unit) {
        if (unit == ChronoUnit.FOREVER)
            return forever();
        return new Duration(amount, unit);
    }

    protected Duration(long amount, ChronoUnit unit) {
        this.amount = amount;
        this.unit = unit;
    }

    public void start() {
        if (started)
            throw new IllegalStateException("The duration already started");
        started = true;
        this.startStamp = Instant.now();
        if (unit == ChronoUnit.FOREVER) {
            expirationStamp = Instant.MAX;
            return;
        }
        long amount = this.amount;
        ChronoUnit unit = this.unit;
        switch (unit) {
            case WEEKS:
                amount *= 7;
                unit = ChronoUnit.DAYS;
                break;
            case MONTHS:
                amount *= 30;
                unit = ChronoUnit.DAYS;
                break;
            case YEARS:
                amount *= 365;
                unit = ChronoUnit.DAYS;
                break;
        }
        try {
            this.expirationStamp = startStamp.plus(amount, unit);
            run();
            return;
        } catch (ArithmeticException ignore) {}
        expirationStamp = Instant.MAX;
    }

    private void run() {
        if (ran)
            return;
        long seconds = expirationStamp.getEpochSecond() - Instant.now().getEpochSecond();
        if (seconds < 0)
            checkExpiration();
        task = Bukkit.getScheduler().runTaskLater(Guardian.getInstance(), this::checkExpiration, (seconds * 20) + 2);
    }

    public void stop() {
        if (expired || !started)
            return;
        if (task != null)
            task.cancel();
        if (runnable != null)
            runnable.run();
    }

    public Instant getStartStamp() {
        return startStamp;
    }

    public Instant getExpirationStamp() {
        return expirationStamp;
    }

    public boolean hasExpired() {
        checkExpiration();
        return expired;
    }

    public void checkExpiration() {
        if (expired)
            return;
        if (expirationStamp.getEpochSecond() <= Instant.now().getEpochSecond()) {
            stop();
            ran = true;
            expired = true;
        }
    }

    /**
     * Runs when the duration expires <p>
     * NOTE: This does not persist after a restart
     * @param runnable The code to run
     */
    public void onExpire(Runnable runnable) {
        this.runnable = runnable;
    }

    public String toReadableString() {
        if (unit == ChronoUnit.FOREVER)
            return "forever";
        boolean single = amount == 1;
        String unitName = unit.name().toLowerCase(Locale.ENGLISH);
        return amount + " " +  (single ? TextUtils.getEnglishPlural(unitName).getLeft() : unitName);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Duration{");
        sb.append("amount=").append(amount);
        sb.append(", unit=").append(unit);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeLong(amount)
                .writeByte((byte) unit.ordinal())
                .writeInstant(startStamp)
                .writeInstant(expirationStamp)
                .writeBoolean(started)
                .writeBoolean(expired)
                .writeBoolean(ran);
    }

    public static Duration deserialize(FriendlyByteBuf buf) {
        Duration duration = Duration.of(buf.readLong(), ChronoUnit.values()[buf.readByte()]);
        duration.startStamp = buf.readInstant();
        duration.expirationStamp = buf.readInstant();
        duration.started = buf.readBoolean();
        duration.expired = buf.readBoolean();
        duration.ran = buf.readBoolean();
        duration.run();
        return duration;
    }

}
