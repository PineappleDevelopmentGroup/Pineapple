package sh.miles.pineapple.util.serialization.adapter;

import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.collection.registry.RegistryKey;
import sh.miles.pineapple.util.serialization.SerializedDeserializeContext;
import sh.miles.pineapple.util.serialization.SerializedElement;
import sh.miles.pineapple.util.serialization.SerializedSerializeContext;
import sh.miles.pineapple.util.serialization.bridges.SerializedBridge;
import sh.miles.pineapple.util.serialization.exception.SerializedAdaptationException;

import java.lang.reflect.Type;

/**
 * An interface for the Serialized system which becomes adapter to different platforms when used alongside
 * {@link SerializedBridge}
 *
 * @param <C> Code Type
 * @since 1.0.0-SNAPSHOT
 */
public interface SerializedAdapter<C> extends RegistryKey<Class<?>> {

    /**
     * Serializes an element into a SerializedElement
     *
     * @param obj     the object to serialize
     * @param context the context of the serialization
     * @return the SerializedElement
     * @throws SerializedAdaptationException thrown if an error occurs during the serialization process
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    SerializedElement serialize(@NotNull final C obj, @NotNull final SerializedSerializeContext context) throws SerializedAdaptationException;

    /**
     * Deserializes an element into a runtime object
     *
     * @param element the element to deserialize
     * @param context the context of the deserialization
     * @return the deserialized object
     * @throws SerializedAdaptationException thrown if an error occurs during the deserialization process
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    C deserialize(@NotNull final SerializedElement element, @NotNull final SerializedDeserializeContext context) throws SerializedAdaptationException;
}
