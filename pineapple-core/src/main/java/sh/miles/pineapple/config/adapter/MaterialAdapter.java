package sh.miles.pineapple.config.adapter;

import org.bukkit.Material;
import sh.miles.pineapple.config.adapter.base.TypeAdapterString;

class MaterialAdapter implements TypeAdapterString<Material> {

    @Override
    public Class<Material> getRuntimeType() {
        return Material.class;
    }

    @Override
    public String toString(Material value) {
        return value.getKey().toString();
    }

    @Override
    public Material fromString(String value) {
        return Material.matchMaterial(value);
    }
}
