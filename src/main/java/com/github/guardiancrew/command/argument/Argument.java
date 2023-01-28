package com.github.guardiancrew.command.argument;

import com.github.guardiancrew.command.util.StringReader;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public interface Argument<T> {

    T parse(CommandSender executor, StringReader reader);

    default List<String> tabComplete(CommandSender executor, StringReader reader) {
        return Collections.emptyList();
    }

}
