package sh.miles.pineapple.json.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import org.bukkit.NamespacedKey;
import sh.miles.pineapple.json.JsonAdapter;

import java.lang.reflect.Type;

class NamespacedKeyAdapter implements JsonAdapter<NamespacedKey> {

    @Override
    public NamespacedKey deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        var string = jsonElement.getAsString();
        if (!string.contains(":")) {
            return NamespacedKey.minecraft(string);
        }
        return NamespacedKey.fromString(string);
    }

    @Override
    public JsonElement serialize(final NamespacedKey key, final Type type, final JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(key.toString());
    }

    @Override
    public Class<NamespacedKey> getAdapterType() {
        return NamespacedKey.class;
    }

    @Override
    public boolean isHierarchy() {
        return true;
    }
}
