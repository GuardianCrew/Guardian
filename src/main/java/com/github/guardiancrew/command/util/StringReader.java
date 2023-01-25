package com.github.guardiancrew.command.util;

import com.github.guardiancrew.command.exception.CommandParseException;

import java.util.Locale;
import java.util.function.Supplier;

public class StringReader {

    private static final char SYNTAX_ESCAPE = '\\';
    private static final char SYNTAX_DOUBLE_QUOTE = '"';
    private static final char SYNTAX_SINGLE_QUOTE = '\'';

    private final String string;
    private int cursor;

    public StringReader(StringReader other) {
        this.string = other.string;
        this.cursor = other.cursor;
    }

    public StringReader(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    public int getCursor() {
        return cursor;
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    public int getRemainingLength() {
        return string.length() - cursor;
    }

    public int getTotalLength() {
        return string.length();
    }

    public String getRead() {
        return string.substring(0, cursor);
    }

    public String getRemaining() {
        return string.substring(cursor);
    }

    public boolean canRead() {
        return canRead(1);
    }

    public boolean canRead(int length) {
        return cursor + length <= string.length();
    }

    public char peek() {
        return peek(0);
    }

    public char peek(int offset) {
        return string.charAt(cursor + offset);
    }

    public char read() {
        return string.charAt(cursor++);
    }

    public void skip() {
        cursor++;
    }

    public void skipWhitespace() {
        readStringUntil(() -> Character.isWhitespace(peek()));
    }

    public int readInt() throws CommandParseException {
        int start = cursor;
        String number = readStringUntil(() -> isAllowedNumber(peek()));

        if (number.isEmpty())
            throw new CommandParseException();

        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            cursor = start;
            throw new CommandParseException();
        }
    }

    public long readLong() throws CommandParseException {
        int start = cursor;
        String number = readStringUntil(() -> isAllowedNumber(peek()));

        if (number.isEmpty())
            throw new CommandParseException();

        try {
            return Long.parseLong(number);
        } catch (NumberFormatException e) {
            cursor = start;
            throw new CommandParseException();
        }
    }

    public float readFloat() throws CommandParseException {
        int start = cursor;
        String number = readStringUntil(() -> isAllowedNumber(peek()));

        if (number.isEmpty())
            throw new CommandParseException();

        try {
            return Float.parseFloat(number);
        } catch (NumberFormatException e) {
            cursor = start;
            throw new CommandParseException();
        }
    }

    public double readDouble() throws CommandParseException {
        int start = cursor;
        String number = readStringUntil(() -> isAllowedNumber(peek()));

        if (number.isEmpty())
            throw new CommandParseException();

        try {
            return Double.parseDouble(number);
        } catch (NumberFormatException e) {
            cursor = start;
            throw new CommandParseException();
        }
    }

    public String readUnquotedString() {
        return readStringUntil(() -> isAllowedInUnquotedString(peek()));
    }

    public String readQuotedString() throws CommandParseException {
        if (!canRead())
            return "";
        char next = peek();
        if (!isQuotedStringStart(next))
            throw new CommandParseException();
        skip();
        return readStringUntil(next);
    }

    public String readString() throws CommandParseException {
        if (!canRead())
            return "";
        char next = peek();
        if (isQuotedStringStart(next)) {
            skip();
            return readStringUntil(next);
        }
        return readUnquotedString();
    }

    public String readPlayerName() {
        return readStringUntil(() -> isAllowedPlayerName(peek()));
    }

    public String readStringUntil(Supplier<Boolean> supplier) {
        int start = cursor;
        while (canRead() && supplier.get())
            skip();
        return string.substring(start, cursor);
    }

    public String readStringUntil(char terminator) throws CommandParseException {
        StringBuilder stringBuilder = new StringBuilder();
        boolean escaped = false;
        while (canRead()) {
            char c = read();
            if (escaped) {
                if (c == terminator || c == SYNTAX_ESCAPE) {
                    stringBuilder.append(c);
                    escaped = false;
                } else {
                    setCursor(cursor - 1);
                    throw new CommandParseException();
                }
            } else if (c == SYNTAX_ESCAPE) {
                escaped = true;
            } else if (c == terminator) {
                return stringBuilder.toString();
            } else {
                stringBuilder.append(c);
            }
        }

        throw new CommandParseException();
    }

    public String readUntil(char terminator) throws CommandParseException {
        StringBuilder stringBuilder = new StringBuilder();
        boolean escaped = false;
        while (canRead()) {
            char c = read();
            if (c == terminator)
                return stringBuilder.toString();
            stringBuilder.append(c);
        }

        return stringBuilder.toString();
    }

    public boolean readBoolean() throws CommandParseException {
        int start = cursor;
        String value = readUnquotedString().toLowerCase(Locale.ENGLISH);

        if (value.isEmpty())
            throw new CommandParseException();

        if (value.equals("true")) {
            return true;
        } else if (value.equals("false")) {
            return false;
        } else {
            cursor = start;
            throw new CommandParseException();
        }
    }

    public void expect(char c) throws CommandParseException {
        if (!canRead() || peek() != c)
            throw new CommandParseException();
        skip();
    }

    public static boolean isAllowedNumber(char c) {
        return Character.isDigit(c) || c == '.' || c == '-';
    }

    public static boolean isQuotedStringStart(char c) {
        return c == SYNTAX_SINGLE_QUOTE || c == SYNTAX_DOUBLE_QUOTE;
    }

    public static boolean isAllowedInUnquotedString(char c) {
        return Character.isLetterOrDigit(c) || c == '_' || c == '-' || c == '.' || c == '+';
    }

    public static boolean isAllowedPlayerName(char c) {
        return Character.isAlphabetic(c) || Character.isDigit(c) || c == '_';
    }

}
