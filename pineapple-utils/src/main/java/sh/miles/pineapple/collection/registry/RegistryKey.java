package sh.miles.pineapple.collection.registry;

/**
 * implemented by objects which can be added to Registries
 *
 * @since 1.0.0
 */
public interface RegistryKey<K> {
    /**
     * @return the key of this object
     * @since 1.0.0
     */
    K getKey();
}
