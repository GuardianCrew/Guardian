package com.github.guardiancrew.punishment;

import com.github.guardiancrew.deserialization.Deserializers;
import com.github.guardiancrew.util.FriendlyByteBuf;
import com.github.guardiancrew.util.Writable;
import com.github.guardiancrew.wrapper.GuardianAdapter;
import com.github.guardiancrew.wrapper.GuardianPlayer;
import org.jetbrains.annotations.Nullable;

public class Punishment implements Writable {

    private static final String PUNISH_MESSAGE = "&c%s &7has been &c%s &7for &c%s &7due to &c%s&7.";
    private static final String SELF_PUNISH_MESSAGE = "&c%s &7have been &c%s &7for &c%s &7due to &c%s&7.";
    private static final String PUNISH_MESSAGE_FOREVER = "&c%s &7has been &c%s &7due to &c%s&7.";
    private static final String SELF_PUNISH_MESSAGE_FOREVER = "&c%s &7have been &c%s &7due to &c%s&7.";

    private final PunishmentType type;
    private final GuardianPlayer punisher;
    private final GuardianPlayer target;
    private final String reason;
    private final Duration duration;
    private final boolean ip;

    public Punishment(PunishmentType type, GuardianPlayer punisher, GuardianPlayer target, String reason, @Nullable Duration duration, boolean ip) {
        this.type = type;
        this.punisher = punisher;
        this.target = target;
        this.reason = reason;
        this.duration = onExpire(duration == null ? Duration.forever() : duration);
        this.ip = ip;
    }

    public PunishmentType getType() {
        return type;
    }

    public GuardianPlayer getTarget() {
        return target;
    }

    public GuardianPlayer getPunisher() {
        return punisher;
    }

    public String getReason() {
        return reason;
    }

    public Duration getDuration() {
        return duration;
    }

    public boolean isIp() {
        return ip;
    }

    public boolean hasExpired() {
        return duration.hasExpired();
    }

    public void announce() {
        if (duration instanceof Forever) {
            target.sendPluginMessage(String.format(SELF_PUNISH_MESSAGE_FOREVER, "You", type.getPastTense(), reason));
            GuardianAdapter.getOnlinePlayers().forEach(player -> {
                if (player != target)
                    player.sendPluginMessage(String.format(PUNISH_MESSAGE_FOREVER, target.getBukkitPlayer().getDisplayName(), type.getPastTense(), reason));
            });
        } else {
            target.sendPluginMessage(String.format(SELF_PUNISH_MESSAGE, "You", type.getPastTense(), duration.toReadableString(), reason));
            GuardianAdapter.getOnlinePlayers().forEach(player -> {
                if (player != target)
                    player.sendPluginMessage(String.format(PUNISH_MESSAGE, target.getBukkitPlayer().getDisplayName(), type.getPastTense(), duration.toReadableString(), reason));
            });
        }
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeByte((byte) type.ordinal())
                .writeUUID(target.getUUID())
                .writeUUID(punisher.getUUID())
                .writeString(reason)
                .write(duration)
                .writeBoolean(ip);
    }

    private Duration onExpire(Duration duration) {
        duration.onExpire(() -> target.sendPluginMessage(String.format("&7You have been &cun%s&7.", type.getPastTense())));
        return duration;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Punishment{");
        sb.append("type=").append(type);
        sb.append(", punisher=").append(punisher);
        sb.append(", reason='").append(reason).append('\'');
        sb.append(", duration=").append(duration);
        sb.append(", ip=").append(ip);
        sb.append('}');
        return sb.toString();
    }

    public static Punishment deserialize(FriendlyByteBuf buf) {
        return builder(PunishmentType.values()[buf.readByte()], GuardianAdapter.wrapPlayer(buf.readUUID()))
                .punisher(GuardianAdapter.wrapPlayer(buf.readUUID()))
                .reason(buf.readString())
                .duration(Deserializers.deserialize(buf, Duration.class))
                .ip(buf.readBoolean())
                .build();
    }

    public static Builder builder(PunishmentType type, GuardianPlayer target) {
        return new Builder(type, target);
    }

    public static class Builder {

        private final PunishmentType type;
        private final GuardianPlayer target;
        private GuardianPlayer punisher;
        private String reason;
        @Nullable
        private Duration duration = null;
        private boolean ip = false;

        public Builder(PunishmentType type, GuardianPlayer target) {
            this.type = type;
            this.target = target;
        }

        public Builder punisher(GuardianPlayer punisher) {
            this.punisher = punisher;
            return this;
        }

        public Builder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public Builder duration(@Nullable Duration duration) {
            this.duration = duration;
            return this;
        }

        public Builder ip(boolean ip) {
            this.ip = ip;
            return this;
        }

        public Punishment build() {
            return new Punishment(type, punisher, target, reason, duration, ip);
        }

    }

}
