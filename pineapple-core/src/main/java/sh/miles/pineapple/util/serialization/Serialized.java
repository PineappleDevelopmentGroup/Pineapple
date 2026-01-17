package sh.miles.pineapple.util.serialization;

import org.jspecify.annotations.NullMarked;
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapter;
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapterRegistry;
import sh.miles.pineapple.util.serialization.exception.SerializedAdaptationException;
import sh.miles.pineapple.util.serialization.exception.SerializedAdapterNotFoundException;

/**
 * Main Class for Serialized to access methods related to the API
 */
@NullMarked
public final class Serialized implements SerializedSerializeContext, SerializedDeserializeContext {

    public static final Serialized INSTANCE = new Serialized();

    private Serialized() {
    }

    @Override
    public <C> C deserialize(final SerializedElement element, final Class<C> type) throws SerializedAdapterNotFoundException, SerializedAdaptationException {
        final var adapter = SerializedAdapterRegistry.INSTANCE.getOrNull(type);
        if (adapter == null) {
            throw new SerializedAdapterNotFoundException(type);
        }

        return (C) adapter.deserialize(element, this);
    }

    @Override
    public SerializedElement serialize(final Object object) throws SerializedAdapterNotFoundException, SerializedAdaptationException {
        return serialize(object, object.getClass());
    }

    @Override
    public SerializedElement serialize(final Object object, final Class<?> type) throws SerializedAdapterNotFoundException, SerializedAdaptationException {
        final var adapter = SerializedAdapterRegistry.INSTANCE.getOrNull(type);
        if (adapter == null) {
            throw new SerializedAdapterNotFoundException(type);
        }

        return ((SerializedAdapter<Object>) adapter).serialize(object, this);
    }
}
