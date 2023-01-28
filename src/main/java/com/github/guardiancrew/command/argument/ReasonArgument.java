package com.github.guardiancrew.command.argument;

import com.github.guardiancrew.command.util.StringReader;
import org.bukkit.command.CommandSender;

public class ReasonArgument implements Argument<String> {

    @Override
    public String parse(CommandSender executor, StringReader reader) {
        if (!reader.canRead())
            return null;

        String reason = reader.getRemaining();
        reader.setCursor(reader.getTotalLength());
        return reason;
    }

}
