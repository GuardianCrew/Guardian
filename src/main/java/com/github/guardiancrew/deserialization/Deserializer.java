package com.github.guardiancrew.deserialization;

import com.github.guardiancrew.util.FriendlyByteBuf;

public interface Deserializer<T> {

    T deserialize(FriendlyByteBuf buf);

}
