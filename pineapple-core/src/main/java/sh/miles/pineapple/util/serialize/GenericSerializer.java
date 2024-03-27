package sh.miles.pineapple.util.serialize;

import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.util.serialize.exception.FieldNotFoundException;

import java.util.Map;

/**
 * Generic Serialization strategy which converts an object into a Map String Object.
 * <p>
 * This strategy is helpful for reducing repeated code when working with the deserialization of multiple systems. While
 * it may take more computing effort to convert using this strategy its convenience outweighs its cons.
 *
 * @param <T> the input type
 * @since 1.0.0-SNAPSHOT
 */
public interface GenericSerializer<T> {

    /**
     * Serializes a target into a String Object map
     *
     * @param target the target to serialize
     * @return the map
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    Map<String, Object> serialize(T target);

    /**
     * Deserializes the target into the type of this serializer
     *
     * @param map the map input
     * @return the type of this serializer
     * @throws FieldNotFoundException thrown if the map does not contain a required field
     */
    @NotNull
    T deserialize(@NotNull final Map<String, Object> map) throws FieldNotFoundException;

    /**
     * Gets the type class of this GenericSerializer
     *
     * @return the serializer
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    Class<T> getTypeClass();
}
