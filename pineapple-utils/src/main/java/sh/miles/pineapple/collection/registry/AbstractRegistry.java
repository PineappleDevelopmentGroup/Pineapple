package sh.miles.pineapple.collection.registry;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

/**
 * A basic abstract implementation of Registry
 *
 * @param <T> the type
 * @since 1.0.0-SNAPSHOT
 */
public abstract class AbstractRegistry<T extends RegistryKey<K>, K> implements Registry<T, K> {

    protected final Map<K, T> registry;

    protected AbstractRegistry(Supplier<Map<K, T>> registrySupplier) {
        this.registry = registrySupplier.get();
    }

    @Override
    public Optional<T> get(@NotNull final K key) {
        return Optional.ofNullable(registry.get(key));
    }

    @Override
    public T getOrNull(@NotNull final K key) {
        return registry.get(key);
    }

    @NotNull
    @Override
    public T getOrDefault(@NotNull final K key, @NotNull final T defaultValue) {
        return registry.getOrDefault(key, Objects.requireNonNull(defaultValue));
    }

    @NotNull
    @Override
    public T getOrDefault(@NotNull final K key, @NotNull final Supplier<T> defaultValue) {
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
