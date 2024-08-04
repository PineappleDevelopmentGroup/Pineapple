package sh.miles.pineapple.config;

import sh.miles.pineapple.config.adapter.TypeAdapterHandler;
import sh.miles.pineapple.config.adapter.base.TypeAdapter;

import java.io.File;

public class ConfigManager {

    private final TypeAdapterHandler typeAdapterHandler;

    public ConfigManager() {
        this.typeAdapterHandler = new TypeAdapterHandler();
    }

    public ConfigWrapper create(File file, Class<?> clazz) {
        return new ConfigWrapper(file, clazz);
    }

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


}
