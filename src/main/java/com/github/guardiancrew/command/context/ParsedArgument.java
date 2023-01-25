package com.github.guardiancrew.command.context;

public class ParsedArgument<T> {

    private final String input;
    private final T result;

    public ParsedArgument(String input, T result) {
        this.input = input;
        this.result = result;
    }

    public String getInput() {
        return input;
    }

    public T getResult() {
        return result;
    }

}
