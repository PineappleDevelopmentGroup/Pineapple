package sh.miles.pineapple.config.adapter;

import sh.miles.pineapple.collection.WeightedRandom;
import sh.miles.pineapple.config.ConfigType;
import sh.miles.pineapple.config.ReloadableClass;
import sh.miles.pineapple.config.ReloadableObject;
import sh.miles.pineapple.config.adapter.base.ConfigSerializable;
import sh.miles.pineapple.config.adapter.base.TypeAdapter;
import sh.miles.pineapple.config.adapter.base.TypeAdapterString;

import java.io.File;
import java.util.Collection;
import java.util.Map;

/**
 * The ConfigurationManager handles all config creation, loading and TypeAdapters
 *
 * @since 1.0.0-SNAPSHOT
 */
public class ConfigurationManager {

    /**
     * Create an instance based config
     *
     * @param file   the file to save/load
     * @param target instance of config class
     * @param <T>    config class
     * @return ConfigReloadable instance
     * @since 1.0.0-SNAPSHOT
     */
    public <T> ReloadableObject<T> createReloadable(File file, T target) {
        return new ReloadableObject<>(file, target);
    }

    /**
     * @param file   the file to save/load
     * @param target config class
     * @param <T>    config class
     * @return ConfigReloadable instance
     * @since 1.0.0-SNAPSHOT
     */
    public <T> ReloadableClass<T> createStaticReloadable(File file, Class<T> target) {
        return new ReloadableClass<>(file, target);
    }

    /**
     * Gets a TypeAdapterString for the given class
     *
     * @param clazz runtime class
     * @param <S>   serialized type
     * @param <R>   runtime class
     * @return TypeAdapterString instance
     * @since 1.0.0-SNAPSHOT
     */
    public <S, R> TypeAdapterString<S, R> getStringAdapter(Class<R> clazz) {
        return getStringAdapter(new ConfigType<>(clazz));
    }

    /**
     * Gets a TypeAdapterString for the given type
     *
     * @param type config type
     * @param <S>  serialized type
     * @param <R>  runtime class
     * @return TypeAdapterString instance
     * @since 1.0.0-SNAPSHOT
     */
    @SuppressWarnings("unchecked")
    public <S, R> TypeAdapterString<S, R> getStringAdapter(ConfigType<R> type) {
        TypeAdapter<?, R> adapter = getAdapter(type);

        if (adapter instanceof TypeAdapterString<?, ?> stringAdapter) {
            return (TypeAdapterString<S, R>) stringAdapter;
        }

        return null;
    }

    /**
     * Gets the type adapter for the given class
     *
     * @param clazz runtime class
     * @param <S>   serialized type
     * @param <R>   runtime class
     * @return TypeAdapter instance
     * @since 1.0.0-SNAPSHOT
     */
    public <S, R> TypeAdapter<S, R> getAdapter(Class<R> clazz) {
        return getAdapter(new ConfigType<>(clazz));
    }

    /**
     * Gets the type adapter for given type
     *
     * @param type config type
     * @param <S>  serialized type
     * @param <R>  runtime class
     * @return TypeAdapter instance
     * @since 1.0.0-SNAPSHOT
     */
    @SuppressWarnings("unchecked")
    public <S, R> TypeAdapter<S, R> getAdapter(ConfigType<R> type) {
        TypeAdapter<?, ?> adapter = ConfigurationAdapterRegistry.INSTANCE.getOrNull(type);
        if (adapter == null) {
            adapter = createAdapter(type);
            ConfigurationAdapterRegistry.INSTANCE.register(adapter);
        }

        return (TypeAdapter<S, R>) adapter;
    }

    private TypeAdapter<?, ?> createAdapter(ConfigType<?> type) {
        if (ConfigSerializable.class.isAssignableFrom(type.getType())) {
            return new ConfigSerializableAdapter<>(type);
        }

        if (WeightedRandom.class.isAssignableFrom(type.getType())) {
            return new WeightedRandomAdapter<>(type);
        }

        if (Enum.class.isAssignableFrom(type.getType())) {
            return new EnumAdapter<>(type.getType());
        }

        if (Collection.class.isAssignableFrom(type.getType())) {
            return new CollectionAdapter<>(type);
        }

        if (Map.class.isAssignableFrom(type.getType())) {
            return new MapAdapter<>(type);
        }

        return new NativeAdapter<>(type.getType());
    }
}
