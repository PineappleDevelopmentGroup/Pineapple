package sh.miles.pineapple.util.serialize.impl;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.util.serialize.GenericSerializer;
import sh.miles.pineapple.util.serialize.exception.FieldNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class ArmorTrimAdapter implements GenericSerializer<ArmorTrim> {

    public static final String MATERIAL_KEY = "trim_material";
    public static final String PATTERN_KEY = "trim_pattern";

    @NotNull
    @Override
    public Map<String, Object> serialize(final ArmorTrim target) {
        final Map<String, Object> map = new HashMap<>();
        map.put(MATERIAL_KEY, target.getMaterial().getKey().toString());
        map.put(PATTERN_KEY, target.getPattern().getKey().toString());
        return map;
    }

    @NotNull
    @Override
    public ArmorTrim deserialize(@NotNull final Map<String, Object> map) throws FieldNotFoundException {
        final TrimMaterial material = Registry.TRIM_MATERIAL.get(NamespacedKey.fromString((String) map.get(MATERIAL_KEY)));
        final TrimPattern pattern = Registry.TRIM_PATTERN.get(NamespacedKey.fromString((String) map.get(PATTERN_KEY)));
        return new ArmorTrim(material, pattern);
    }

    @NotNull
    @Override
    public Class<ArmorTrim> getTypeClass() {
        return ArmorTrim.class;
    }
}
