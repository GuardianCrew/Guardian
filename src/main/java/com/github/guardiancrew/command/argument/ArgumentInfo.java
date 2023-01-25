package com.github.guardiancrew.command.argument;

public class ArgumentInfo<T> {

    private final String name;
    private final Argument<T> argument;
    private final boolean optional;

    public ArgumentInfo(String name, Argument<T> argument, boolean optional) {
        this.name = name;
        this.argument = argument;
        this.optional = optional;
    }

    public String getName() {
        return name;
    }

    public Argument<T> getArgument() {
        return argument;
    }

    public boolean isOptional() {
        return optional;
    }

}
