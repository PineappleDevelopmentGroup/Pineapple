package sh.miles.pineapple.nms.impl.v1_21_R2;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.collection.registry.FrozenRegistry;
import sh.miles.pineapple.collection.registry.RegistryKey;
import sh.miles.pineapple.nms.api.PineappleUnsafe;
import sh.miles.pineapple.nms.impl.v1_21_R2.registry.PineappleNmsRegistry;

public class PineappleUnsafeImpl implements PineappleUnsafe {

    @SuppressWarnings("unchecked")
    @NotNull
    @Override
    public <T extends RegistryKey<NamespacedKey>> FrozenRegistry<T, NamespacedKey> getRegistry(final Class<? super T> clazz) {
        return (FrozenRegistry<T, NamespacedKey>) PineappleNmsRegistry.makeRegistry(clazz);
    }

}
