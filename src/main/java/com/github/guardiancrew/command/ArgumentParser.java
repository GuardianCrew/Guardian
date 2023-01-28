package com.github.guardiancrew.command;

import com.github.guardiancrew.command.argument.ArgumentInfo;
import com.github.guardiancrew.command.context.CommandContext;
import com.github.guardiancrew.command.context.ParsedArgument;
import com.github.guardiancrew.command.exception.CommandParseException;
import com.github.guardiancrew.command.util.StringReader;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class ArgumentParser {

    private final String label;
    private final LinkedHashMap<String, ArgumentInfo<?>> arguments;
    private final HashMap<String, GuardianCommand> subcommands;
    private final CommandSender executor;

    public ArgumentParser(CommandSender executor, String label, GuardianCommand command) {
        this(executor, label, command.getArguments(), command.getSubcommands());
    }

    public ArgumentParser(CommandSender executor, String label, LinkedHashMap<String, ArgumentInfo<?>> arguments, HashMap<String, GuardianCommand> subcommands) {
        this.label = label;
        this.arguments = arguments;
        this.subcommands = subcommands;
        this.executor = executor;
    }

    @Nullable
    public CommandContext parse(StringReader reader) throws CommandParseException {
        LinkedHashMap<String, ParsedArgument<?>> parsedArguments = new LinkedHashMap<>(arguments.size());
        for (ArgumentInfo<?> argumentInfo : arguments.values()) {
            reader.skipWhitespace();

            if (!reader.canRead() && !argumentInfo.isOptional())
                throw new CommandParseException("Not enough arguments");

            int start = reader.getCursor();
            try {
                Object parsedArgument = argumentInfo.getArgument().parse(executor, reader);
                int end = reader.getCursor();
                parsedArguments.put(argumentInfo.getName(), parsedArgument == null ? null : new ParsedArgument<>(reader.getString().substring(start, end), parsedArgument));
            } catch (CommandParseException e) {
                reader.setCursor(start);
                if (argumentInfo.isOptional()) {
                    parsedArguments.put(argumentInfo.getName(), null);
                } else {
                    throw e;
                }
            }
        }
        if (reader.canRead()) {
            reader.skipWhitespace();
            int cursor = reader.getCursor();
            String label = reader.readUntil(' ');
            GuardianCommand subcommand = subcommands.get(label);
            if (subcommand == null) {
                reader.setCursor(cursor);
                throw new CommandParseException("Unknown argument: '" + reader.getRemaining() + '\'');
            }
            subcommand.execute(executor, label, reader);
            return null;
        }

        if (reader.canRead())
            throw new CommandParseException("Too many arguments");

        return new CommandContext(label, reader.getString(), parsedArguments);
    }

}
