package sh.miles.pineapple.util.serialize.base;

import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.util.serialize.exception.GenericSerializerNotFoundException;

/**
 * Context for the Deserialization process that provides useful methods that can otherwise not be easily accessed
 *
 * @since 1.0.0-SNAPSHOT
 */
public interface GenericSerializationContext {

    /**
     * Serializes an object with this context
     *
     * @param complex     the complex object to serialize
     * @param complexType the complex type of the object
     * @param storedType  the stored type of the object
     * @param <S>         the stored type
     * @param <C>         the complex type
     * @return the stored object
     * @throws GenericSerializerNotFoundException if the provided complex type or stored type does not match
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    <S, C> S serialize(@NotNull final C complex, @NotNull final Class<C> complexType, @NotNull final Class<S> storedType) throws GenericSerializerNotFoundException;

    /**
     * Serializes an object with this context
     *
     * @param complex    the complex object to serialize
     * @param storedType the stored type
     * @param <S>        the stored type
     * @param <C>        the complex type
     * @return the stored object
     * @throws GenericSerializerNotFoundException if the provided complex type or stored type does not match
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    default <S, C> S serialize(@NotNull final C complex, @NotNull final Class<S> storedType) throws GenericSerializerNotFoundException {
        return serialize(complex, (Class<C>) complex.getClass(), storedType);
    }

    /**
     * Deserializes a stored object with this context
     *
     * @param stored      the stored value
     * @param storedType  the stored type
     * @param complexType the complex type to deserialize to
     * @param <S>         the stored type
     * @param <C>         the complex type
     * @return the complex object
     * @throws GenericSerializerNotFoundException if the provided stored or complex type could not be deserialized
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    <S, C> C deserialize(@NotNull final S stored, @NotNull final Class<S> storedType, @NotNull final Class<C> complexType) throws GenericSerializerNotFoundException;

    /**
     * Deserializes a stored object with this context
     *
     * @param stored      the stored
     * @param complexType the complex type
     * @param <S>         the stored type
     * @param <C>         the complex type
     * @return the complex object
     * @throws GenericSerializerNotFoundException if the provided complex type or stored type does not match
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    default <S, C> C deserialize(@NotNull final S stored, @NotNull final Class<C> complexType) {
        return deserialize(stored, (Class<S>) stored.getClass(), complexType);
    }
}
