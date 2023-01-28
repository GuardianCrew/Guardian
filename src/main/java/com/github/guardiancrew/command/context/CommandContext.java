package com.github.guardiancrew.command.context;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class CommandContext {

    private final String label;
    private final String input;
    private final Map<String, ParsedArgument<?>> arguments;

    public CommandContext(String label, String input, Map<String, ParsedArgument<?>> arguments) {
        this.label = label;
        this.input = input;
        this.arguments = arguments;
    }

    public String getLabel() {
        return label;
    }

    public String getInput() {
        return input;
    }

    public Collection<ParsedArgument<?>> getAllArguments() {
        return arguments.values().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public <R> R getArgument(String name, Class<R> returnType) {
        return getArgument(name, returnType, false);
    }

    @SuppressWarnings("unchecked")
    public <R> R getArgument(String name, Class<R> returnType, boolean optional) {
        ParsedArgument<?> argument = arguments.get(name);

        if (argument == null) {
            if (optional)
                return null;
            throw new IllegalArgumentException("No such argument '" + name + "' exists in this command");
        }

        Object result = argument.getResult();
        if (returnType.isAssignableFrom(result.getClass()))
            return (R) result;
        throw new IllegalArgumentException("Argument '" + name + "' is defined as " + result.getClass().getSimpleName() + ", not " + returnType);
    }

    public <R> Optional<R> getOptionalArgument(String name, Class<R> returnType) {
        return Optional.ofNullable(getArgument(name, returnType, true));
    }

}
