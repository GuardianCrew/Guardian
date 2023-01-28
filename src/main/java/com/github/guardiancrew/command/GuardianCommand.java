package com.github.guardiancrew.command;

import com.github.guardiancrew.command.argument.Argument;
import com.github.guardiancrew.command.argument.ArgumentInfo;
import com.github.guardiancrew.command.context.CommandContext;
import com.github.guardiancrew.command.exception.CommandParseException;
import com.github.guardiancrew.command.util.CompletionUtils;
import com.github.guardiancrew.command.util.StringReader;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class GuardianCommand {

    private final String name;
    private final String description;
    private final List<String> aliases;
    private final LinkedHashMap<String, ArgumentInfo<?>> arguments;
    private final List<ArgumentInfo<?>> argumentList;
    private final HashMap<String, GuardianCommand> subcommands;
    private final String permission;
    private final String permissionMessage;
    private final CommandExecutable executable;

    public GuardianCommand(String name, String description, List<String> aliases, LinkedHashMap<String, ArgumentInfo<?>> arguments, HashMap<String, GuardianCommand> subcommands, String permission, String permissionMessage, CommandExecutable executable) {
        this.name = name;
        this.description = description;
        this.aliases = aliases;
        this.arguments = arguments;
        this.argumentList = new LinkedList<>(arguments.values());
        this.subcommands = subcommands;
        this.permission = permission;
        this.permissionMessage = permissionMessage;
        this.executable = executable;
      }

    public BukkitCommand asBukkitCommand() {
        BukkitCommand command = new BukkitCommand(name, description, "", aliases) {

            @Override
            public boolean execute(CommandSender sender, String commandLabel, String[] args) {
                StringReader reader = new StringReader(String.join(" ", args));
                return GuardianCommand.this.execute(sender, commandLabel, reader);
            }

            @Override
            public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
                int index = args.length - 1;
                String input = args[index];

                if (args.length > argumentList.size()) {
                    if (args.length > argumentList.size() + 1) {
                        GuardianCommand subcommand = subcommands.get(args[argumentList.size()]);
                        if (subcommand == null)
                            return Collections.emptyList();
                        return subcommand.asBukkitCommand().tabComplete(sender, alias, Arrays.copyOfRange(args, argumentList.size() + 1, args.length));
                    }
                    return CompletionUtils.filterCompletions(input, new ArrayList<>(subcommands.keySet()));
                }

                return GuardianCommand.this.tabComplete(sender, input, argumentList.get(index).getArgument());
            }

        };
        command.setPermission(permission);
        command.setPermissionMessage(permissionMessage);
        return command;
    }

    public boolean execute(CommandSender executor, String label, StringReader reader) {
        ArgumentParser argumentParser = new ArgumentParser(executor, label, this);
        try {
            CommandContext context = argumentParser.parse(reader);
            if (context == null)
                return true;
            executable.execute(executor, context);
        } catch (CommandParseException e) {
            executor.sendMessage(ChatColor.RED + e.getMessage());
        }
        return true;
    }

    public List<String> tabComplete(CommandSender executor, String input, Argument<?> argument) {
        return argument.tabComplete(executor, new StringReader(input));
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public LinkedHashMap<String, ArgumentInfo<?>> getArguments() {
        return arguments;
    }

    public HashMap<String, GuardianCommand> getSubcommands() {
        return subcommands;
    }

    public String getPermission() {
        return permission;
    }

    public String getPermissionMessage() {
        return permissionMessage;
    }

    public CommandExecutable getExecutable() {
        return executable;
    }

    public static Builder builder(@NotNull String name) {
        return new Builder(name);
    }

    public static class Builder {

        private final String name;
        private String description;
        private final List<String> aliases = new ArrayList<>();
        private final LinkedHashMap<String, ArgumentInfo<?>> arguments = new LinkedHashMap<>();
        private final HashMap<String, GuardianCommand> subcommands = new HashMap<>();
        private String permission;
        private String permissionMessage;
        private CommandExecutable executable;

        private Builder(@NotNull String name) {
            this.name = name;
            this.description = "";
            this.permission = "";
        }

        public Builder description(@NotNull String description) {
            this.description = description;
            return this;
        }

        public Builder aliases(@NotNull List<String> aliases) {
            this.aliases.clear();
            this.aliases.addAll(aliases);
            return this;
        }

        public Builder aliases(@NotNull String... aliases) {
            this.aliases.clear();
            this.aliases.addAll(Arrays.asList(aliases));
            return this;
        }

        public <T> Builder argument(String name, Argument<T> argument) {
            return argument(name, argument, false);
        }

        public <T> Builder argument(String name, Argument<T> argument, boolean optional) {
            arguments.putIfAbsent(name, new ArgumentInfo<>(name, argument, optional));
            return this;
        }

        public <T> Builder subcommand(GuardianCommand command) {
            subcommands.putIfAbsent(command.name, command);
            return this;
        }

        public Builder permission(@NotNull String permission) {
            this.permission = permission;
            return this;
        }

        public Builder permissionMessage(@NotNull String permissionMessage) {
            this.permissionMessage = permissionMessage;
            return this;
        }

        public Builder execute(@NotNull CommandExecutable executable) {
            this.executable = executable;
            return this;
        }

        public GuardianCommand build() {
            return new GuardianCommand(name, description, aliases, arguments, subcommands, permission, permissionMessage, executable);
        }

    }

}
