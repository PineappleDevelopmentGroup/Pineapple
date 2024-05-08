package sh.miles.pineapple.util.serialization;

import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapter;
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapterRegistry;
import sh.miles.pineapple.util.serialization.exception.SerializedAdaptationException;
import sh.miles.pineapple.util.serialization.exception.SerializedAdapterNotFoundException;

/**
 * Main Class for Serialized to access methods related to the API
 */
public final class Serialized implements SerializedSerializeContext, SerializedDeserializeContext {

    public static final Serialized INSTANCE = new Serialized();

    private Serialized() {
    }

    @NotNull
    @Override
    public <C> C deserialize(@NotNull final SerializedElement element, final Class<C> type) throws SerializedAdapterNotFoundException, SerializedAdaptationException {
        final var adapter = SerializedAdapterRegistry.INSTANCE.getOrNull(type);
        if (adapter == null) {
            throw new SerializedAdapterNotFoundException(type);
        }

        return (C) adapter.deserialize(element, this);
    }

    @NotNull
    @Override
    public SerializedElement serialize(@NotNull final Object object) throws SerializedAdapterNotFoundException, SerializedAdaptationException {
        return serialize(object, object.getClass());
    }

    @NotNull
    @Override
    public SerializedElement serialize(@NotNull final Object object, final Class<?> type) throws SerializedAdapterNotFoundException, SerializedAdaptationException {
        final var adapter = SerializedAdapterRegistry.INSTANCE.getOrNull(type);
        if (adapter == null) {
            throw new SerializedAdapterNotFoundException(type);
        }

        return ((SerializedAdapter<Object>) adapter).serialize(object, this);
    }
}
