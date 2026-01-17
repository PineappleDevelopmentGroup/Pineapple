package sh.miles.pineapple.config;

import org.jspecify.annotations.NullMarked;
import sh.miles.pineapple.config.adapter.TypeAdapterHandler;
import sh.miles.pineapple.config.adapter.base.TypeAdapter;
import sh.miles.pineapple.config.type.Configuration;

import java.io.File;

@NullMarked
public class ConfigManager {

    private final TypeAdapterHandler typeAdapterHandler;

    public ConfigManager() {
        this.typeAdapterHandler = new TypeAdapterHandler();
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
            }
        );
        return (TypeAdapter<S, R>) adapter;
    }

    /**
     * Create a static based configuration
     *
     * @param file  The file for data to be saved to
     * @param clazz The Class to retrieve values from
     * @return Configuration data wrapper
     */
    public Configuration createConfiguration(File file, Class<?> clazz) {
        return new Configuration(file, clazz, null);
    }

    /**
     * Create an instanced based configuration
     *
     * @param file     The file for data to be saved to
     * @param clazz    The Class to retrieve values from
     * @param instance The instance to use to access the values
     * @return Configuration data wrapper
     */
    public Configuration createConfiguration(File file, Class<?> clazz, Object instance) {
        return new Configuration(file, clazz, instance);
    }


}
