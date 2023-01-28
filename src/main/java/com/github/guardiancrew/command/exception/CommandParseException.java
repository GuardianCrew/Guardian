package com.github.guardiancrew.command.exception;

public class CommandParseException extends RuntimeException {

    public CommandParseException() {
        super();
    }

    public CommandParseException(String message) {
        super(message);
    }

    public CommandParseException(Throwable cause) {
        super(cause);
    }

    public CommandParseException(String message, Throwable cause) {
        super(message, cause);
    }

}
