package sh.miles.pineapple.util.serialize.base;

import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.util.serialize.exception.GenericSerializationException;

/**
 * A Generic Serializer used to connect multiple types of serialization systems
 *
 * @param <S> Stored Type, the type stored
 * @param <C> Code Type, the type output to the code
 * @since 1.0.0-SNAPSHOT
 */
public interface GenericSerializer<S, C> {

    /**
     * Serializes a complex type into the stored type
     *
     * @param complex the complex type
     * @param context the serialization context
     * @return the serialized type
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    S serialize(@NotNull final C complex, @NotNull final GenericSerializationContext context);

    /**
     * Deserializes a stored type into a complex type
     *
     * @param stored the stored type
     * @param context the serialization context
     * @return the complex code type
     * @throws GenericSerializationException if any exception occurs while deserialization occurs
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    C deserialize(@NotNull final S stored, @NotNull final GenericSerializationContext context) throws GenericSerializationException;

    /**
     * Gets the stored type
     *
     * @return the stored type
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    TypeToken<S> getStoredType();

    /**
     * Gets the complex type
     *
     * @return the complex type
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    TypeToken<C> getComplexType();
}
