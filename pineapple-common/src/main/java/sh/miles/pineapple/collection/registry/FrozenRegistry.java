package sh.miles.pineapple.collection.registry;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Represents a registry that can not be registered too after creation
 *
 * @param <T> the type of the object
 * @since 1.0.0-SNAPSHOT
 */
public class FrozenRegistry<T extends RegistryKey<K>, K> extends AbstractRegistry<T, K> {

    public FrozenRegistry(final Supplier<Map<K, T>> registrySupplier) {
        super(() -> Map.copyOf(registrySupplier.get()));
    }
}
