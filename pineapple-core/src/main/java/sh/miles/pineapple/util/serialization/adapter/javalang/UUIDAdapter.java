package sh.miles.pineapple.util.serialization.adapter.javalang;

import org.jspecify.annotations.NullMarked;
import sh.miles.pineapple.util.serialization.SerializedDeserializeContext;
import sh.miles.pineapple.util.serialization.SerializedElement;
import sh.miles.pineapple.util.serialization.SerializedPrimitive;
import sh.miles.pineapple.util.serialization.SerializedSerializeContext;
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapter;
import sh.miles.pineapple.util.serialization.exception.SerializedAdaptationException;

import java.util.UUID;

@NullMarked
class UUIDAdapter implements SerializedAdapter<UUID> {

    @Override
    public SerializedElement serialize(final UUID obj, final SerializedSerializeContext context) throws SerializedAdaptationException {
        return SerializedElement.primitive(obj.toString());
    }

    @Override
    public UUID deserialize(final SerializedElement element, final SerializedDeserializeContext context) throws SerializedAdaptationException {
        final SerializedPrimitive primitive = element.getAsPrimitive();
        if (!primitive.isString()) {
            throw new SerializedAdaptationException(
                "Expected %s when deserialization to UUID found %s".formatted(String.class,
                    primitive.getTypeOfPrimitive()
                ));
        }
        return UUID.fromString(primitive.getAsString());
    }

    @Override
    public Class<?> getKey() {
        return UUID.class;
    }
}
