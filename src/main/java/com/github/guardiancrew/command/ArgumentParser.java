package com.github.guardiancrew.command;

import com.github.guardiancrew.command.argument.ArgumentInfo;
import com.github.guardiancrew.command.context.CommandContext;
import com.github.guardiancrew.command.context.ParsedArgument;
import com.github.guardiancrew.command.exception.CommandParseException;
import com.github.guardiancrew.command.util.StringReader;
import com.github.guardiancrew.wrapper.GuardianPlayer;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class ArgumentParser {

    private final LinkedHashMap<String, ArgumentInfo<?>> arguments;
    private final HashMap<String, GuardianCommand> subcommands;
    private final GuardianPlayer executor;
    @Nullable
    private String lastErrorMessage = null;

    public ArgumentParser(GuardianPlayer executor, LinkedHashMap<String, ArgumentInfo<?>> arguments, HashMap<String, GuardianCommand> subcommands) {
        this.arguments = arguments;
        this.subcommands = subcommands;
        this.executor = executor;
    }

    public ArgumentParser(GuardianPlayer executor, GuardianCommand command) {
        this.arguments = command.getArguments();
        this.subcommands = command.getSubcommands();
        this.executor = executor;
    }

    @Nullable
    public CommandContext parse(StringReader reader) {
        lastErrorMessage = null;
        LinkedHashMap<String, ParsedArgument<?>> parsedArguments = new LinkedHashMap<>(arguments.size());
        for (ArgumentInfo<?> argumentInfo : arguments.values()) {
            reader.skipWhitespace();
            if (!reader.canRead() && !argumentInfo.isOptional()) {
                lastErrorMessage = ChatColor.RED + "Not enough arguments";
                return null;
            }
            int start = reader.getCursor();
            try {
                Object parsedArgument = argumentInfo.getArgument().parse(reader, executor);
                int end = reader.getCursor();
                parsedArguments.put(argumentInfo.getName(), parsedArgument == null ? null : new ParsedArgument<>(reader.getString().substring(start, end), parsedArgument));
            } catch (CommandParseException e) {
                reader.setCursor(start);
                if (argumentInfo.isOptional()) {
                    parsedArguments.put(argumentInfo.getName(), null);
                } else if (executor != null && e.getMessage() != null) {
                    lastErrorMessage = ChatColor.RED + e.getMessage();
                    return null;
                }
            }
        }
        if (reader.canRead()) {
            reader.skipWhitespace();
            int cursor = reader.getCursor();
            GuardianCommand subcommand = subcommands.get(reader.readUntil(' '));
            if (subcommand == null) {
                reader.setCursor(cursor);
                lastErrorMessage = ChatColor.RED + "Unknown argument: '" + reader.getRemaining() + '\'';
                return null;
            }
            subcommand.execute(executor, reader);
            return null;
        }
        if (reader.canRead()) {
            lastErrorMessage = ChatColor.RED + "Too many arguments";
            return null;
        }
        return new CommandContext(reader.getString(), parsedArguments);
    }

    public @Nullable String getLastErrorMessage() {
        return lastErrorMessage;
    }

    public boolean hasError() {
        return lastErrorMessage != null;
    }

}
