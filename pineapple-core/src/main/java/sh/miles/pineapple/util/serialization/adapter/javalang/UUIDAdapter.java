package sh.miles.pineapple.util.serialization.adapter.javalang;

import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.util.serialization.SerializedDeserializeContext;
import sh.miles.pineapple.util.serialization.SerializedElement;
import sh.miles.pineapple.util.serialization.SerializedPrimitive;
import sh.miles.pineapple.util.serialization.SerializedSerializeContext;
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapter;
import sh.miles.pineapple.util.serialization.exception.SerializedAdaptationException;

import java.util.UUID;

class UUIDAdapter implements SerializedAdapter<UUID> {

    @NotNull
    @Override
    public SerializedElement serialize(@NotNull final UUID obj, @NotNull final SerializedSerializeContext context) throws SerializedAdaptationException {
        return SerializedElement.primitive(obj.toString());
    }

    @NotNull
    @Override
    public UUID deserialize(@NotNull final SerializedElement element, @NotNull final SerializedDeserializeContext context) throws SerializedAdaptationException {
        final SerializedPrimitive primitive = element.getAsPrimitive();
        if (!primitive.isString()) {
            throw new SerializedAdaptationException("Expected %s when deserialization to UUID found %s".formatted(String.class, primitive.getTypeOfPrimitive()));
        }
        return UUID.fromString(primitive.getAsString());
    }

    @Override
    public Class<?> getKey() {
        return UUID.class;
    }
}
