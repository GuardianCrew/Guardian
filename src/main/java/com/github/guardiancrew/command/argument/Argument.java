package com.github.guardiancrew.command.argument;

import com.github.guardiancrew.command.util.StringReader;
import com.github.guardiancrew.wrapper.GuardianPlayer;
import org.bukkit.util.StringUtil;

import java.util.Collections;
import java.util.List;

public abstract class Argument<T> {

    public abstract T parse(StringReader reader, GuardianPlayer executor);

    public List<String> tabComplete(StringReader reader, GuardianPlayer executor) {
        return Collections.emptyList();
    }

    public static List<String> sortCompletions(String input, List<String> completions) {
        completions.removeIf(string -> !StringUtil.startsWithIgnoreCase(string, input));
        completions.sort(String::compareToIgnoreCase);
        return completions;
    }

}
