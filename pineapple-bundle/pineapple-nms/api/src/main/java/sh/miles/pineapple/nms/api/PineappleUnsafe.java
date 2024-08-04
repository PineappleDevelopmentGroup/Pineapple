package sh.miles.pineapple.nms.api;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.collection.registry.FrozenRegistry;
import sh.miles.pineapple.collection.registry.RegistryKey;

/**
 * An Unstable Internal class which is unsafe and can change sporadically without notice
 */
@ApiStatus.Internal
public interface PineappleUnsafe {

    /**
     * Attempts to grab a registry class
     *
     * @param clazz the registry class to grab
     * @param <T>   the type of the registry class
     * @return the given registry
     * @throws IllegalStateException given the requested registry was not found
     */
    @ApiStatus.Internal
    @NotNull
    <T extends RegistryKey<NamespacedKey>> FrozenRegistry<T, NamespacedKey> getRegistry(Class<? super T> clazz) throws IllegalStateException;

}
