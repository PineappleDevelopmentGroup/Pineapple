package sh.miles.pineapple.collection.registry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Represents a basic registry object which contained {@link RegistryKey} objects
 *
 * @since 1.0.0
 */
public interface Registry<T extends RegistryKey<K>, K> {

    /**
     * Fetches an entry from the registry
     *
     * @param key the key to use to fetch the entry
     * @return an optional wrapping the nullable result
     * @since 1.0.0
     */
    Optional<T> get(@NotNull final K key);

    /**
     * Fetches an entry from the registry
     *
     * @param key the key to use to fetch the entry
     * @return the resulting object at that key, or null if it doesn't exist
     * @since 1.0.0
     */
    @Nullable
    T getOrNull(@NotNull final K key);

    /**
     * Retrieves a set of all keys from the registry
     *
     * @return a set of string keys
     * @since 1.0.0
     */
    Set<K> keys();

    /**
     * Creates a new WriteableRegistry with the given data
     *
     * @param data the data
     * @param <T>  the type
     * @param <K>  the key type
     * @return the newly created WriteableRegistry
     * @since 1.0.0
     */
    static <T extends RegistryKey<K>, K> WriteableRegistry<T, K> newWriteable(Supplier<Map<K, T>> data) {
        return new WriteableRegistry<>(data);
    }

    /**
     * Creates a new FrozenRegistry with the given data
     *
     * @param data the data
     * @param <T>  the type
     * @param <K>  the key type
     * @return the newly created FrozenRegistry
     * @since 1.0.0
     */
    static <T extends RegistryKey<K>, K> FrozenRegistry<T, K> newFrozen(Supplier<Map<K, T>> data) {
        return new FrozenRegistry<>(data);
    }
}
