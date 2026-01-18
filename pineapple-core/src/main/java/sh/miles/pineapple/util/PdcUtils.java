package sh.miles.pineapple.util;

import io.papermc.paper.persistence.PersistentDataContainerView;
import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public final class PdcUtils {

    private PdcUtils() {
    }

    public static NamespacedKey locationKey(String namespace, int x, int y, int z) {
        return new NamespacedKey(namespace, "x%d_y%d_z%d".formatted(x, y, z));
    }

    public static NamespacedKey keyToLegacy(Key key) {
        return new NamespacedKey(key.namespace(), key.value());
    }

    public static void setMarker(PersistentDataContainer container, Key key) {
        container.set(keyToLegacy(key), PersistentDataType.BYTE, (byte) 1);
    }

    public static boolean hasMarker(PersistentDataContainerView view, Key key) {
        return view.getOrDefault(keyToLegacy(key), PersistentDataType.BYTE, (byte) 0) == 1;
    }
}
