package sh.miles.pineapple.json;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;

import java.util.function.Function;

/**
 * An adapter for to and from json operations
 *
 * @param <T> the type
 * @since 1.0.0-SNAPSHOT
 */
public interface JsonAdapter<T> extends JsonSerializer<T>, JsonDeserializer<T> {

    /**
     * Retrieves the type of the adapter
     *
     * @return the type of the adapter
     * @since 1.0.0-SNAPSHOT
     */
    Class<T> getAdapterType();

    /**
     * Decides whether or not the JsonAdapter should be registered within a GsonBuilder as a Type Hierarchical Adapter
     * or just a normal type adapter.
     *
     * @return true if the adapter should be used in a hierarchy, otherwise false
     * @since 1.0.0-SNAPSHOT
     */
    default boolean isHierarchy() {
        return false;
    }

    /**
     * Gets a json value from the provided parent JsonObject or throws an error with a default message
     *
     * @param key        the key of the value to get
     * @param parent     the parent json object
     * @param conversion the conversion method from JsonElement to the desired type
     * @param <K>        the return value that was mapped from a JsonElement
     * @return the desired value
     * @throws IllegalArgumentException throws an exception given the given key has no proper field
     * @since 1.0.0-SNAPSHOT
     */
    static <K> K getOrFail(String key, JsonObject parent, Function<JsonElement, K> conversion) throws IllegalArgumentException {
        return getOrFail(key, parent, conversion, "The provided key %s does not exist so the given key could not be adapted".formatted(key));
    }

    /**
     * Gets a json value from the provided parent JsonObject or throws an error with a default message
     *
     * @param key         the key of the value to get
     * @param parent      the parent json object
     * @param conversion  the conversion method from JsonElement to the desired type
     * @param failMessage the message sent when a failure occurs
     * @param <K>         the return value that was mapped from a JsonElement
     * @return the desired value
     * @throws IllegalArgumentException throws an exception given the given key has no proper field
     * @since 1.0.0-SNAPSHOT
     */
    static <K> K getOrFail(String key, JsonObject parent, Function<JsonElement, K> conversion, String failMessage) throws IllegalArgumentException {
        if (!parent.has(key)) {
            throw new IllegalArgumentException(failMessage);
        }

        return conversion.apply(parent.get(key));
    }
}
