package com.github.guardiancrew.deserialization;

import com.github.guardiancrew.util.FriendlyByteBuf;

import java.util.HashMap;
import java.util.Map;

public class Deserializers {

    private static final Map<Class<?>, Deserializer<?>> deserializers = new HashMap<>();

    private Deserializers() {
        throw new UnsupportedOperationException();
    }

    public static <T> void registerDeserializer(Class<T> c, Deserializer<T> deserializer) {
        if (deserializers.containsKey(c))
            throw new IllegalArgumentException("Deserializer for '" + c + "' already exists");
        deserializers.put(c, deserializer);
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialize(FriendlyByteBuf buf, Class<T> returnType) {
        Deserializer<T> deserializer = (Deserializer<T>) deserializers.get(returnType);
        return deserializer.deserialize(buf);
    }

}
