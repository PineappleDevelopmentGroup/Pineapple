package sh.miles.pineapple.collection.registry;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * An Updatable registry that can have contents added arbitrarily.
 *
 * @param <T> the type
 * @since 1.0.0-SNAPSHOT
 */
public class WriteableRegistry<T extends RegistryKey<K>, K> extends AbstractRegistry<T, K> {

    public WriteableRegistry(final Supplier<Map<K, T>> registrySupplier) {
        super(registrySupplier);
    }

    public WriteableRegistry() {
        super(HashMap::new);
    }

    /**
     * Registers an object within the registry
     *
     * @param object the object to register
     * @return true if the value was successfully registered, otherwise false.
     * @since 1.0.0-SNAPSHOT
     */
    public boolean register(@NotNull final T object) {
        final T value = registry.get(object.getKey());
        if (value == null) {
            registry.put(object.getKey(), object);
            return true;
        }
        return false;
    }
}
