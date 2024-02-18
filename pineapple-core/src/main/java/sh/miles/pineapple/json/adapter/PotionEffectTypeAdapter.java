package sh.miles.pineapple.json.adapter;

import org.bukkit.potion.PotionEffectType;
import sh.miles.pineapple.json.adapter.base.BukkitRegistryJsonAdapter;

public class PotionEffectTypeAdapter implements BukkitRegistryJsonAdapter<PotionEffectType> {
    @Override
    public Class<PotionEffectType> getAdapterType() {
        return PotionEffectType.class;
    }

    @Override
    public boolean isHierarchy() {
        return true;
    }
}
