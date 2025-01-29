package sh.miles.pineapple.config.adapter.base;

import sh.miles.pineapple.collection.registry.RegistryKey;
import sh.miles.pineapple.config.ConfigType;

/**
 * @param <S> Saved/Stored type
 * @param <R> Runtime/Code type
 */
public interface TypeAdapter<S, R> extends RegistryKey<ConfigType<?>> {

    Class<S> getSavedType();

    Class<R> getRuntimeType();

    S write(R value, S existing, boolean replace);

    R read(S saved);

    @Override
    default ConfigType<R> getKey() {
        return new ConfigType<>(getRuntimeType());
    }
}
