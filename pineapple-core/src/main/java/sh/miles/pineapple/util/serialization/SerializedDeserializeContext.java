package sh.miles.pineapple.util.serialization;

import org.jspecify.annotations.NullMarked;
import sh.miles.pineapple.util.serialization.exception.SerializedAdaptationException;
import sh.miles.pineapple.util.serialization.exception.SerializedAdapterNotFoundException;

/**
 * Context for Serialized adapters to use that allows access to other adapters to be used within any adapter
 *
 * @since 1.0.0-SNAPSHOT
 */
@NullMarked
public interface SerializedDeserializeContext {

    /**
     * Deserializes a given element into a runtime type
     *
     * @param element the element to deserialize
     * @param type    the type to deserialize into
     * @param <C>     the product
     * @return the deserialized product
     * @throws SerializedAdapterNotFoundException if no adapter for the type is found
     * @throws SerializedAdaptationException      if an error occurs during deserialization
     */
    <C> C deserialize(final SerializedElement element, final Class<C> type) throws SerializedAdapterNotFoundException, SerializedAdaptationException;
}
