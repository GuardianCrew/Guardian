package com.github.guardiancrew.deserialization;

import com.github.guardiancrew.punishment.Duration;
import com.github.guardiancrew.punishment.Punishment;
import com.github.guardiancrew.wrapper.GuardianPlayer;

public class DefaultDeserializers {

    static {

        Deserializers.registerDeserializer(GuardianPlayer.class, GuardianPlayer::deserialize);
        Deserializers.registerDeserializer(Punishment.class, Punishment::deserialize);
        Deserializers.registerDeserializer(Duration.class, Duration::deserialize);

    }

}
