package sh.miles.pineapple.config.adapter;

import org.bukkit.NamespacedKey;
import sh.miles.pineapple.config.adapter.base.TypeAdapterString;

class NamespacedKeyAdapter implements TypeAdapterString<NamespacedKey> {

    @Override
    public Class<NamespacedKey> getRuntimeType() {
        return NamespacedKey.class;
    }

    @Override
    public String toString(NamespacedKey value) {
        return value.toString();
    }

    @Override
    public NamespacedKey fromString(String value) {
        return NamespacedKey.fromString(value);
    }
}
