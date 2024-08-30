package sh.miles.pineapple.nms.api.registry;

import org.bukkit.NamespacedKey;
import sh.miles.pineapple.collection.registry.Registry;
import sh.miles.pineapple.collection.registry.RegistryKey;
import sh.miles.pineapple.nms.loader.NMSLoader;
import sh.miles.pineapple.nms.api.menu.MenuType;

/**
 * A class full of different types of Registries that can used
 *
 * @since 1.0.0-SNAPSHOT
 */
public final class PineappleRegistry {

    public static final Registry<MenuType<?>, NamespacedKey> MENU = make(MenuType.class);

    private PineappleRegistry() {
        throw new UnsupportedOperationException("You can not instantiate a utility class");
    }

    private static <T extends RegistryKey<NamespacedKey>> Registry<T, NamespacedKey> make(Class<? super T> interfaceClass) {
        return NMSLoader.INSTANCE.getPineapple().getUnsafe().getRegistry(interfaceClass);
    }

}
