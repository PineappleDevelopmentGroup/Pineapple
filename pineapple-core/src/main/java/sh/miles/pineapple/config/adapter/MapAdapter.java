package sh.miles.pineapple.config.adapter;

import sh.miles.pineapple.PineappleLib;
import sh.miles.pineapple.config.ConfigType;
import sh.miles.pineapple.config.adapter.base.TypeAdapter;
import sh.miles.pineapple.config.adapter.base.TypeAdapterString;

import java.util.LinkedHashMap;
import java.util.Map;

class MapAdapter<K, V> implements TypeAdapter<Map<String, Object>, Map<K, V>> {

    private final TypeAdapterString<K> keyAdapter;
    private final TypeAdapter<Object, V> valueAdapter;

    @SuppressWarnings("unchecked")
    public MapAdapter(ConfigType<?> type) {
        this.keyAdapter = (TypeAdapterString<K>) (Object) PineappleLib.getConfigurationManager().getTypeAdapter(type.getComponentTypes().get(0));
        this.valueAdapter = (TypeAdapter<Object, V>)  PineappleLib.getConfigurationManager().getTypeAdapter(type.getComponentTypes().get(1));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<Map<String, Object>> getSavedType() {
        return (Class<Map<String, Object>>) (Object) Map.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<Map<K, V>> getRuntimeType() {
        return (Class<Map<K,V>>) (Object) Map.class;
    }

    @Override
    public Map<String, Object> write(Map<K, V> value, Map<String, Object> existing, boolean replace) {
        if (existing == null) {
            existing = new LinkedHashMap<>();
        }

        for (Map.Entry<K, V> entry : value.entrySet()) {
            String serializedKey = this.keyAdapter.toString(entry.getKey());

            if (!existing.containsKey(serializedKey) || replace) {
                Object saveValue = this.valueAdapter.write(entry.getValue(), existing.get(serializedKey), replace);
                if (saveValue == null) {
                    continue;
                }
                existing.put(serializedKey, saveValue);
            }
        }

        return existing;
    }

    @Override
    public Map<K, V> read(Map<String, Object> saved) {
        Map<K, V> read = new LinkedHashMap<>();

        for (Map.Entry<String, Object> entry : saved.entrySet()) {
            K key = this.keyAdapter.fromString(entry.getKey());
            V value = this.valueAdapter.read(entry.getValue());

            read.put(key, value);
        }

        return read;
    }
}
