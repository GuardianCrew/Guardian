package com.github.guardiancrew.util;

import org.bukkit.command.CommandSender;

public final class Messenger {

    public static final String MESSAGE_PREFIX = "&3[&bGuardian&3]&r ";

    public Messenger() {
        throw new UnsupportedOperationException();
    }

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(TextUtils.format(message));
    }

    public static void sendPluginMessage(CommandSender sender, String message) {
        sender.sendMessage(TextUtils.format(MESSAGE_PREFIX + message));
    }

    public static void sendRawMessage(CommandSender sender, String message) {
        sender.sendMessage(message);
    }

}
