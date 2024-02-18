package sh.miles.pineapple.json.adapter;

import org.bukkit.damage.DamageType;
import sh.miles.pineapple.json.adapter.base.BukkitRegistryJsonAdapter;

@SuppressWarnings("all") // "experimental"
public class DamageTypeAdapter implements BukkitRegistryJsonAdapter<DamageType> {
    @Override
    public Class<DamageType> getAdapterType() {
        return DamageType.class;
    }

    @Override
    public boolean isHierarchy() {
        return true;
    }
}
