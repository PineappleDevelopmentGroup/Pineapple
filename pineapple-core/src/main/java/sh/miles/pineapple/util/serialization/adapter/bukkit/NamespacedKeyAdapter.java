package sh.miles.pineapple.util.serialization.adapter.bukkit;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.util.serialization.SerializedDeserializeContext;
import sh.miles.pineapple.util.serialization.SerializedElement;
import sh.miles.pineapple.util.serialization.SerializedSerializeContext;
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapter;
import sh.miles.pineapple.util.serialization.exception.SerializedAdaptationException;

class NamespacedKeyAdapter implements SerializedAdapter<NamespacedKey> {

    @Override
    public SerializedElement serialize( final NamespacedKey obj,  final SerializedSerializeContext context) throws SerializedAdaptationException {
        return SerializedElement.primitive(obj.toString());
    }

    @Override
    public NamespacedKey deserialize( final SerializedElement element,  final SerializedDeserializeContext context) throws SerializedAdaptationException {
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
