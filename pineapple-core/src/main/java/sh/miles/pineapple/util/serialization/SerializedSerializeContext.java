package sh.miles.pineapple.util.serialization;

import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.util.serialization.exception.SerializedAdaptationException;
import sh.miles.pineapple.util.serialization.exception.SerializedAdapterNotFoundException;

/**
 * Context for Serialized adapters to use that allows access to other adapters to be used within any adapter
 *
 * @since 1.0.0-SNAPSHOT
 */
public interface SerializedSerializeContext {

    /**
     * Serializes the given object down to a SerializedElement
     *
     * @param object the object to serialize
     * @return the non null SerializedElement
     * @throws SerializedAdapterNotFoundException if no adapter for the type is found
     * @throws SerializedAdaptationException      if an exception occurs during serialization
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    SerializedElement serialize(@NotNull final Object object) throws SerializedAdapterNotFoundException, SerializedAdaptationException;

    /**
     * Serialized the given object down to a SerializedElement
     *
     * @param object the object to serialize
     * @param type   the type of the object more specifically
     * @return the non null SerializedElement
     * @throws SerializedAdapterNotFoundException if no adapter for the type is found
     * @throws SerializedAdaptationException if an error occurs during serialization
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    SerializedElement serialize(@NotNull final Object object, Class<?> type) throws SerializedAdapterNotFoundException, SerializedAdaptationException;
}
