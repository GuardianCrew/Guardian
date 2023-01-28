package com.github.guardiancrew.command.util;

import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class CompletionUtils {

    private CompletionUtils() {
        throw new UnsupportedOperationException();
    }

    public static List<String> filterCompletions(String input, Collection<String> completions) {
        List<String> newCompletions = new ArrayList<>(completions);
        newCompletions.removeIf(string -> !StringUtil.startsWithIgnoreCase(string, input));
        return newCompletions;
    }

}
