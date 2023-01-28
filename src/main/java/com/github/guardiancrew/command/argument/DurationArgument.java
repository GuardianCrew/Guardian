package com.github.guardiancrew.command.argument;

import com.github.guardiancrew.command.exception.CommandParseException;
import com.github.guardiancrew.command.util.CompletionUtils;
import com.github.guardiancrew.command.util.StringReader;
import com.github.guardiancrew.punishment.Duration;
import org.bukkit.command.CommandSender;

import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DurationArgument implements Argument<Duration> {

    private static final HashMap<String, ChronoUnit> STRING_TO_UNIT = new HashMap<>();
    private static final Pattern DURATION_PATTERN = Pattern.compile("(\\d+)(mo|[smhdwy])");

    static {
        STRING_TO_UNIT.put("s", ChronoUnit.SECONDS);
        STRING_TO_UNIT.put("m", ChronoUnit.MINUTES);
        STRING_TO_UNIT.put("h", ChronoUnit.HOURS);
        STRING_TO_UNIT.put("d", ChronoUnit.DAYS);
        STRING_TO_UNIT.put("w", ChronoUnit.WEEKS);
        STRING_TO_UNIT.put("mo", ChronoUnit.MONTHS);
        STRING_TO_UNIT.put("y", ChronoUnit.YEARS);
    }

    @Override
    public Duration parse(CommandSender executor, StringReader reader) {
        String string = reader.readUnquotedString();
        Matcher matcher = DURATION_PATTERN.matcher(string);
        if (!matcher.matches())
            throw new CommandParseException();
        try {
            long amount = Long.parseLong(matcher.group(1));
            ChronoUnit unit = STRING_TO_UNIT.get(matcher.group(2));
            return Duration.of(amount, unit);
        } catch (NumberFormatException e) {
            return Duration.forever();
        }
    }

    @Override
    public List<String> tabComplete(CommandSender executor, StringReader reader) {
        String number = reader.readUntil(() -> Character.isDigit(reader.peek()));
        if (number.isEmpty()) {
            if (reader.canRead())
                return Collections.emptyList();
            number = "#";
        }
        String unit = reader.getRemaining();
        List<String> completions = CompletionUtils.filterCompletions(unit, STRING_TO_UNIT.keySet());
        String finalNumber = number;
        return completions.stream()
                .map(string -> finalNumber + string)
                .collect(Collectors.toList());
    }

}
