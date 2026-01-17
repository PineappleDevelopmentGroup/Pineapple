package sh.miles.pineapple.util.serialization.adapter.bukkit;

import sh.miles.pineapple.chat.PineappleChat;
import sh.miles.pineapple.chat.PineappleComponent;
import sh.miles.pineapple.util.serialization.SerializedDeserializeContext;
import sh.miles.pineapple.util.serialization.SerializedElement;
import sh.miles.pineapple.util.serialization.SerializedSerializeContext;
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapter;
import sh.miles.pineapple.util.serialization.exception.SerializedAdaptationException;

class PineappleComponentAdapter implements SerializedAdapter<PineappleComponent> {

    @Override
    public SerializedElement serialize(final PineappleComponent obj, final SerializedSerializeContext context) throws SerializedAdaptationException {
        return SerializedElement.primitive(obj.getSource());
    }

    @Override
    public PineappleComponent deserialize(final SerializedElement element, final SerializedDeserializeContext context) throws SerializedAdaptationException {
        if (element.isPrimitive()) {
            return PineappleChat.component(element.getAsPrimitive().getAsString());
        }

        throw new SerializedAdaptationException("PineappleComponent's can only be deserialized from a primitive");
    }

    @Override
    public Class<?> getKey() {
        return PineappleComponent.class;
    }
}
