package sh.miles.pineapple.json.adapter.base;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import sh.miles.pineapple.json.JsonAdapter;

import java.lang.reflect.Type;

/**
 * A Base implementation for a JsonAdapter which completes logic required to read to and from bukkit registries
 *
 * @param <T> the keyed type
 */
public interface BukkitRegistryJsonAdapter<T extends Keyed> extends JsonAdapter<T> {

    @Override
    default T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        final T keyedEntry = registry().get(context.deserialize(jsonElement, NamespacedKey.class));
        if (keyedEntry == null) {
            throw new JsonParseException("Could not find keyed entry in registry %s for json element %s".formatted(getAdapterType(), jsonElement));
        }
        return keyedEntry;
    }

    @Override
    default JsonElement serialize(T keyed, Type type, JsonSerializationContext context) {
        return context.serialize(keyed.getKey(), NamespacedKey.class);
    }

    default Registry<T> registry() {
        return Bukkit.getRegistry(getAdapterType());
    }


}
