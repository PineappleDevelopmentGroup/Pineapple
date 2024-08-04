package sh.miles.pineapple.util.serialization.adapter;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.util.serialization.SerializedDeserializeContext;
import sh.miles.pineapple.util.serialization.SerializedElement;
import sh.miles.pineapple.util.serialization.SerializedSerializeContext;
import sh.miles.pineapple.util.serialization.exception.SerializedAdaptationException;

class NamespacedKeyAdapter implements SerializedAdapter<NamespacedKey> {

    @NotNull
    @Override
    public SerializedElement serialize(@NotNull final NamespacedKey obj, @NotNull final SerializedSerializeContext context) throws SerializedAdaptationException {
        return SerializedElement.primitive(obj.toString());
    }

    @NotNull
    @Override
    public NamespacedKey deserialize(@NotNull final SerializedElement element, @NotNull final SerializedDeserializeContext context) throws SerializedAdaptationException {
        if (element.isPrimitive()) {
            return NamespacedKey.fromString(element.getAsPrimitive().getAsString());
        }
        throw new SerializedAdaptationException("NamespacedKey's can only be adapted from primitive strings");
    }

    @Override
    public Class<?> getKey() {
        return NamespacedKey.class;
    }
}
