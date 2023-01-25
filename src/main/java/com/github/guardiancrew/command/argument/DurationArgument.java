package com.github.guardiancrew.command.argument;

import com.github.guardiancrew.command.exception.CommandParseException;
import com.github.guardiancrew.command.util.StringReader;
import com.github.guardiancrew.punishment.Duration;
import com.github.guardiancrew.wrapper.GuardianPlayer;

import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DurationArgument extends Argument<Duration> {

    private static final HashMap<String, ChronoUnit> STRING_TO_UNIT = new HashMap<>();
    private static final Pattern DURATION_PATTERN = Pattern.compile("(\\d+)(mo|[smhdy])");

    static {
        STRING_TO_UNIT.put("s", ChronoUnit.SECONDS);
        STRING_TO_UNIT.put("m", ChronoUnit.MINUTES);
        STRING_TO_UNIT.put("h", ChronoUnit.HOURS);
        STRING_TO_UNIT.put("d", ChronoUnit.DAYS);
        STRING_TO_UNIT.put("mo", ChronoUnit.MONTHS);
        STRING_TO_UNIT.put("y", ChronoUnit.YEARS);
    }

    @Override
    public Duration parse(StringReader reader, GuardianPlayer executor) {
        String string = reader.readUnquotedString();
        Matcher matcher = DURATION_PATTERN.matcher(string);
        if (!matcher.matches())
            throw new CommandParseException();
        long amount = Long.parseLong(matcher.group(1));
        ChronoUnit unit = STRING_TO_UNIT.get(matcher.group(2));
        return new Duration(amount, unit);
    }

}
