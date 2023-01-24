package com.github.guardiancrew.command.argument;

import com.github.guardiancrew.command.util.StringReader;
import com.github.guardiancrew.wrapper.GuardianPlayer;

public class ReasonArgument extends Argument<String> {

    @Override
    public String parse(StringReader reader, GuardianPlayer executor) {
        if (!reader.canRead())
            return null;

        String reason = reader.getRemaining();
        reader.setCursor(reader.getTotalLength());
        return reason;
    }

}
