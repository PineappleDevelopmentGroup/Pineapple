package sh.miles.pineapple.collection.registry;

import org.jspecify.annotations.NullMarked;
import sh.miles.pineapple.function.Option;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

/**
 * A basic abstract implementation of Registry
 *
 * @param <T> the type
 * @since 1.0.0-SNAPSHOT
 */
@NullMarked
public abstract class AbstractRegistry<T extends RegistryKey<K>, K> implements Registry<T, K> {

    protected final Map<K, T> registry;

    protected AbstractRegistry(Supplier<Map<K, T>> registrySupplier) {
        this.registry = registrySupplier.get();
    }

    @Override
    public Option<T> get(final K key) {
        final T value = registry.get(key);
        if (value == null) {
            return Option.none();
        } else {
            return Option.some(value);
        }
    }

    @Override
    public T getOrNull(final K key) {
        return registry.get(key);
    }

    @Override
    public T getOrDefault(final K key, final T defaultValue) {
        return registry.getOrDefault(key, Objects.requireNonNull(defaultValue));
    }


    @Override
    public T getOrDefault(final K key, final Supplier<T> defaultValue) {
        var temp = registry.get(key);
        if (temp == null) {
            return defaultValue.get();
        }
        return temp;
    }

    @Override
    public Set<K> keys() {
        return registry.keySet();
    }
}
