package sh.miles.pineapple.util.serialize.impl;

import com.google.common.reflect.TypeToken;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.util.serialize.GenericSerializer;
import sh.miles.pineapple.util.serialize.exception.FieldNotFoundException;

import java.util.Map;
import java.util.stream.Collectors;

class EnchantmentAdapter implements GenericSerializer<Map<Enchantment, Integer>> {

    @NotNull
    @Override
    public Map<String, Object> serialize(final Map<Enchantment, Integer> target) {
        return target.entrySet().stream().collect(Collectors.toMap((entry) -> entry.getKey().getKey().toString(), Map.Entry::getValue));
    }

    @NotNull
    @Override
    public Map<Enchantment, Integer> deserialize(@NotNull final Map<String, Object> map) throws FieldNotFoundException {
        return map.entrySet().stream().collect(Collectors.toMap((entry) -> Registry.ENCHANTMENT.get(NamespacedKey.fromString(entry.getKey())), (entry) -> Integer.parseInt((String) entry.getValue())));
    }

    @NotNull
    @Override
    public Class<Map<Enchantment, Integer>> getTypeClass() {
        return (Class<Map<Enchantment, Integer>>) new TypeToken<Map<Enchantment, Integer>>() {
        }.getRawType();
    }
}
