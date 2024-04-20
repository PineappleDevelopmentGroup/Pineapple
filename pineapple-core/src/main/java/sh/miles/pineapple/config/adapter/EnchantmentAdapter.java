package sh.miles.pineapple.config.adapter;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import sh.miles.pineapple.StringUtils;
import sh.miles.pineapple.collection.Pair;
import sh.miles.pineapple.config.adapter.base.TypeAdapterString;

public class EnchantmentAdapter implements TypeAdapterString<Pair<Enchantment, Integer>> {

    @Override
    public Class<Pair<Enchantment, Integer>> getRuntimeType() {
        return (Class<Pair<Enchantment, Integer>>) (Object) Pair.class;
    }

    @Override
    public Pair<Enchantment, Integer> fromString(final String value) {
        var list = StringUtils.split(value, ':');
        return Pair.of(Registry.ENCHANTMENT.get(NamespacedKey.fromString(list.get(0))), Integer.parseInt(list.get(1)));
    }

    @Override
    public String toString(final Pair<Enchantment, Integer> value) {
        var left = value.left();
        String key;
        if (left.getKey().getNamespace().equals("minecraft")) {
            key = left.getKey().getKey();
        } else {
            key = left.getKey().toString();
        }
        return key + ":" + value.right();
    }
}
