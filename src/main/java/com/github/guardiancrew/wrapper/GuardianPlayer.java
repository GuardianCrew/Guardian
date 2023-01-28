package com.github.guardiancrew.wrapper;

import com.github.guardiancrew.punishment.Duration;
import com.github.guardiancrew.punishment.Punishment;
import com.github.guardiancrew.punishment.PunishmentType;
import com.github.guardiancrew.staffmode.StaffModeTools;
import com.github.guardiancrew.util.FriendlyByteBuf;
import com.github.guardiancrew.util.Messenger;
import com.github.guardiancrew.util.TextUtils;
import com.github.guardiancrew.util.Writable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class GuardianPlayer implements Writable {

    private final UUID uuid;
    private Player bukkitPlayer;
    private boolean staffMode = false;
    private final List<Punishment> punishments = new ArrayList<>();

    GuardianPlayer(UUID uuid) {
        this(uuid, Bukkit.getPlayer(uuid));
    }

    GuardianPlayer(Player bukkitPlayer) {
        this(bukkitPlayer.getUniqueId(), bukkitPlayer);
    }

    private GuardianPlayer(UUID uuid, Player bukkitPlayer) {
        this.uuid = uuid;
        this.bukkitPlayer = bukkitPlayer;
    }

    public void refresh() {
        bukkitPlayer = Bukkit.getPlayer(uuid);
    }

    public UUID getUUID() {
        return uuid;
    }

    public Player getBukkitPlayer() {
        return bukkitPlayer;
    }

    public void punish(PunishmentType type, GuardianPlayer punisher) {
        punish(type, punisher, Duration.forever());
    }

    public void punish(PunishmentType type, GuardianPlayer punisher, Duration duration) {
        punish(type, punisher, duration, null);
    }

    public void punish(PunishmentType type, GuardianPlayer punisher, Duration duration, String reason) {
        punish(type, punisher, duration, reason, false);
    }

    public void punish(PunishmentType type, GuardianPlayer punisher, Duration duration, String reason, boolean ip) {
        if (type != PunishmentType.WARN && hasPunishment(type)) {
            punisher.sendPluginMessage("&c" + bukkitPlayer.getDisplayName() + " &7is already &c" + type.getPastTense());
            return;
        }
        Punishment punishment = Punishment.builder(type, this)
                .punisher(punisher)
                .duration(duration)
                .reason(reason)
                .ip(ip)
                .build();
        duration.start();
        punishments.add(punishment);
        punishment.announce();
        if (type == PunishmentType.BAN) {
            bukkitPlayer.kickPlayer("You have been banned");
        }
    }

    public void kick(@Nullable GuardianPlayer executor, String reason) {
        bukkitPlayer.kickPlayer(TextUtils.format("&cYou &7have been kicked " + (executor == null ? "" : "by &c" + executor.bukkitPlayer.getDisplayName()) + " &7due to &c" + reason));
    }

    public void setStaffMode(boolean activated) {
        if (staffMode == activated)
            return;
        if (activated) {
            // TODO this is very dangerous, do it in another way
            bukkitPlayer.saveData();
            bukkitPlayer.getInventory().clear();
            sendMessage(TextUtils.format("&3&lGuardian&3: &cThis message (maybe the prefix too?) can be edited in the config."));
            for (StaffModeTools tool : StaffModeTools.values()) {
                bukkitPlayer.getInventory().addItem(tool.getItem());
            }
        } else {
            bukkitPlayer.loadData();
        }
        staffMode = activated;
    }

    public boolean inStaffMode() {
        return staffMode;
    }

    public List<Punishment> getPunishments() {
        return Collections.unmodifiableList(punishments);
    }

    public List<Punishment> getActivePunishments() {
        return punishments.stream()
                .filter(punishment -> !punishment.hasExpired())
                .collect(Collectors.toList());
    }

    public List<Punishment> getActivePunishments(PunishmentType type) {
        return punishments.stream()
                .filter(punishment -> !punishment.hasExpired() && punishment.getType() == type)
                .collect(Collectors.toList());
    }

    public boolean hasPunishment(PunishmentType type) {
        return getActivePunishments(type).size() > 0;
    }

    @ApiStatus.Internal
    public void addPunishment(Punishment punishment) {
        this.punishments.add(punishment);
    }

    public void sendMessage(String message) {
        Messenger.sendMessage(bukkitPlayer, message);
    }

    public void sendPluginMessage(String message) {
        Messenger.sendPluginMessage(bukkitPlayer, message);
    }

    public void sendRawMessage(String message) {
        Messenger.sendRawMessage(bukkitPlayer, message);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(uuid)
                .writeBoolean(staffMode)
                .writeVarInt(punishments.size());
        for (Punishment punishment : punishments)
            buf.write(punishment);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GuardianPlayer{");
        sb.append("uuid=").append(uuid);
        sb.append(", staffMode=").append(staffMode);
        sb.append('}');
        return sb.toString();
    }

    public static GuardianPlayer deserialize(FriendlyByteBuf buf) {
        UUID uuid = buf.readUUID();
        GuardianPlayer player = new GuardianPlayer(uuid);
        player.staffMode = buf.readBoolean();
        GuardianAdapter.guardianPlayers.putIfAbsent(uuid, player);
        return player;
    }

}
