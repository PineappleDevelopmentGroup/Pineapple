package sh.miles.pineapple.config.adapter;

import sh.miles.pineapple.PineappleLib;
import sh.miles.pineapple.collection.Pair;
import sh.miles.pineapple.config.ConfigType;
import sh.miles.pineapple.config.adapter.base.TypeAdapter;
import sh.miles.pineapple.config.adapter.base.TypeAdapterString;

import java.util.HashMap;
import java.util.Map;

final class PairAdapter<L, R> implements TypeAdapter<Map<String, Object>, Pair<L, R>> {

    private final TypeAdapterString<L> keyAdapter;
    private final TypeAdapter<Object, R> valueAdapter;

    @SuppressWarnings("unchecked")
    public PairAdapter(ConfigType<?> type) {
        this.keyAdapter = (TypeAdapterString<L>) (Object) PineappleLib.getConfigurationManager().getTypeAdapter(type.getComponentTypes().get(0));
        this.valueAdapter = (TypeAdapter<Object, R>) PineappleLib.getConfigurationManager().getTypeAdapter(type.getComponentTypes().get(1));
    }

    @Override
    public Map<String, Object> write(final Pair<L, R> value, Map<String, Object> existing, final boolean replace) {
        if (existing == null) {
            existing = new HashMap<>();
        }

        String serializedKey = this.keyAdapter.toString(value.left());
        if (!existing.containsKey(serializedKey) || replace) {
            Object saveValue = this.valueAdapter.write(value.right(), existing.get(serializedKey), replace);
            if (saveValue == null) {
                return existing;
            }
            existing.put(serializedKey, saveValue);
        }

        return existing;
    }

    @Override
    public Pair<L, R> read(final Map<String, Object> saved) {
        var entry = saved.entrySet().stream().findFirst().orElseThrow();
        L key = this.keyAdapter.fromString(entry.getKey());
        R value = this.valueAdapter.read(entry.getValue());
        return Pair.of(key, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<Map<String, Object>> getSavedType() {
        return (Class<Map<String, Object>>) (Object) Map.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<Pair<L, R>> getRuntimeType() {
        return (Class<Pair<L, R>>) (Object) Map.class;
    }
}
