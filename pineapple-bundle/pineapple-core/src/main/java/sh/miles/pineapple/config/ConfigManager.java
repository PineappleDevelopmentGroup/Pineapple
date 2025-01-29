package sh.miles.pineapple.config;

import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.config.adapter.TypeAdapterHandler;
import sh.miles.pineapple.config.adapter.base.TypeAdapter;
import sh.miles.pineapple.config.type.Configuration;

import java.io.File;

public class ConfigManager {

    private final TypeAdapterHandler typeAdapterHandler;

    public ConfigManager() {
        this.typeAdapterHandler = new TypeAdapterHandler();
    }

    @Deprecated(forRemoval = true)
    public ConfigWrapper create(File file, Class<?> clazz) {
        return new ConfigWrapper(file, clazz);
    }

    @Deprecated(forRemoval = true)
    public ConfigWrapper createDefault(File file, Class<?> clazz) {
        return create(file, clazz).save(false).load();
    }

    public void registerTypeAdapter(TypeAdapter<?, ?> adapter) {
        this.typeAdapterHandler.register(adapter);
    }

    /**
     * Gets a type adapter from config type
     *
     * @param type The ConfigType
     * @param <S>  The Saved Type
     * @param <R>  The Runtime Type
     * @return TypeAdapter if found, otherwise null
     */
    @SuppressWarnings("unchecked")
    public <S, R> TypeAdapter<S, R> getTypeAdapter(ConfigType<R> type) {
        TypeAdapter<?, ?> adapter = this.typeAdapterHandler.getOrDefault(type, () -> {
            final var temp = this.typeAdapterHandler.create(type);
            this.typeAdapterHandler.register(temp);
            return temp;
        });
        return (TypeAdapter<S, R>) adapter;
    }

    /**
     * Create a static based configuration
     *
     * @param file The file for data to be saved to
     * @param clazz The Class to retrieve values from
     * @return Configuration data wrapper
     */
    public Configuration createConfiguration(@NotNull File file, @NotNull Class<?> clazz) {
        return new Configuration(file, clazz, null);
    }

    /**
     * Create an instanced based configuration
     *
     * @param file The file for data to be saved to
     * @param clazz The Class to retrieve values from
     * @param instance The instance to use to access the values
     * @return Configuration data wrapper
     */
    public Configuration createConfiguration(@NotNull File file, @NotNull Class<?> clazz, @NotNull Object instance) {
        return new Configuration(file, clazz, instance);
    }


}
