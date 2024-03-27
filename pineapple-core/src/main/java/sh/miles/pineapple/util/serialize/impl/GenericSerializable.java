package sh.miles.pineapple.util.serialize.impl;

import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.util.serialize.GenericSerializer;
import sh.miles.pineapple.util.serialize.GenericSerializerPlatform;
import sh.miles.pineapple.util.serialize.exception.GenericSerializerNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Storage unit for all GenericSerializers to be used as a proxy with other services
 *
 * @since 1.0.0-SNAPSHOT
 */
public final class GenericSerializable {

    public static final GenericSerializable INSTANCE = new GenericSerializable();

    private final Map<Class<?>, GenericSerializer<?>> serializers;
    private final List<GenericSerializerPlatform> platforms;

    private GenericSerializable() {
        this.serializers = new HashMap<>();
        this.platforms = new ArrayList<>();
    }

    /**
     * Deserializes the map into the type
     *
     * @param map  the map of data
     * @param type the type of data
     * @param <T>  the type
     * @return the type
     * @since 1.0.0-SNAPSHOT
     */
    public <T> T deserialize(@NotNull final Map<String, Object> map, @NotNull final Class<T> type) {
        checkArgument(map != null, "the given map must not be null");
        checkArgument(type != null, "the given type must not be null");
        final var serializer = (GenericSerializer<T>) serializers.get(type);
        if (serializer != null) {
            throw new GenericSerializerNotFoundException(type);
        }
        return serializer.deserialize(map);
    }

    /**
     * Serializes the object into a map of string object
     *
     * @param object the object
     * @param <T>    the type
     * @return the map string object map
     */
    public <T> Map<String, Object> serializer(@NotNull final T object) {
        checkArgument(object != null, "The given type must not be null");
        final var serializer = (GenericSerializer<T>) serializers.get(object);
        if (serializer != null) {
            throw new GenericSerializerNotFoundException(object.getClass());
        }
        return serializer.serialize(object);
    }

    /**
     * Registers a new GenericSerializer
     *
     * @param serializer the serializer to register
     * @since 1.0.0-SNAPSHOT
     */
    public void register(@NotNull final GenericSerializer<?> serializer) {
        checkArgument(serializer != null, "the given serializer must not be null");
        this.serializers.put(serializer.getTypeClass(), serializer);
        this.platforms.forEach((platform) -> platform.registerToPlatform(serializer));
    }

    /**
     * Registers the platform
     *
     * @param platform the platform
     * @since 1.0.0-SNAPSHOT
     */
    public void registerPlatform(@NotNull final GenericSerializerPlatform platform) {
        checkArgument(platform != null, "the platform you provide must not be null");
        this.platforms.add(platform);
        this.serializers.values().forEach(platform::registerToPlatform);
    }

}
