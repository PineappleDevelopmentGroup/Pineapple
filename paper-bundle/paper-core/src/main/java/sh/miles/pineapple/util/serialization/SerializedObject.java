package sh.miles.pineapple.util.serialization;

import com.google.common.base.Preconditions;
import com.google.gson.internal.LinkedTreeMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sh.miles.pineapple.function.Option;
import sh.miles.pineapple.util.serialization.exception.InvalidSerializedTypeException;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Represents an object with key value pairs of serialized elements
 *
 * @since 1.0.0-SNAPSHOT
 */
public final class SerializedObject extends SerializedElement {

    private static final String KEY_NULL_PRECONDITIONS_MESSAGE = "The given key must not be null";

    private final LinkedTreeMap<String, SerializedElement> map;

    SerializedObject() {
        this.map = new LinkedTreeMap<>(false);
    }

    /**
     * Gets the value at the given key or null
     *
     * @param key the key
     * @return the serialized element if it exists at the key, or null
     * @since 1.0.0-SNAPSHOT
     */
    @Nullable
    public SerializedElement getOrNull(@NotNull final String key) {
        Preconditions.checkArgument(key != null, KEY_NULL_PRECONDITIONS_MESSAGE);
        return this.map.get(key);
    }

    /**
     * Gets the primitive value or null
     *
     * @param key the key
     * @return the SerializedPrimitive or null
     * @throws InvalidSerializedTypeException thrown if the key exists, but is not a SerializedPrimitive
     * @since 1.0.0-SNAPSHOT
     */
    @Nullable
    public SerializedPrimitive getPrimitiveOrNull(@NotNull final String key) throws InvalidSerializedTypeException {
        final SerializedElement element = getOrNull(key);
        if (element == null) {
            return null;
        }

        if (!(element instanceof SerializedPrimitive)) {
            throw new InvalidSerializedTypeException(SerializedPrimitive.class, element.getClass());
        }

        return (SerializedPrimitive) element;
    }

    /**
     * Gets the array value or null
     *
     * @param key the key
     * @return the SerializedArray or null
     * @throws InvalidSerializedTypeException thrown if the key exists, but is not a SerializedArray
     * @since 1.0.0-SNAPSHOT
     */
    @Nullable
    public SerializedArray getArrayOrNull(@NotNull final String key) throws InvalidSerializedTypeException {
        final SerializedElement element = getOrNull(key);
        if (element == null) {
            return null;
        }

        if (!(element instanceof SerializedArray)) {
            throw new InvalidSerializedTypeException(SerializedArray.class, element.getClass());
        }

        return (SerializedArray) element;
    }

    /**
     * Gets the object value or null
     *
     * @param key the key
     * @return the SerializedObject or null
     * @throws InvalidSerializedTypeException thrown if the key exists, but is not a SerializedObject
     * @since 1.0.0-SNAPSHOT
     */
    @Nullable
    public SerializedObject getObjectOrNull(@NotNull final String key) throws InvalidSerializedTypeException {
        final SerializedElement element = getOrNull(key);
        if (element == null) {
            return null;
        }

        if (!(element instanceof SerializedObject)) {
            throw new InvalidSerializedTypeException(SerializedObject.class, element.getClass());
        }

        return (SerializedObject) element;
    }

    /**
     * Gets the value at the give key
     *
     * @param key the key
     * @return the possible serialized element wrapped in an option
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public Option<SerializedElement> get(@NotNull final String key) {
        final SerializedElement element = getOrNull(key);
        if (element == null) {
            return Option.none();
        }
        return Option.some(element);
    }

    /**
     * Gets the value of the primitive at the key
     *
     * @param key the key
     * @return the possible SerializedPrimitive wrapped in an option
     * @throws InvalidSerializedTypeException thrown if the key exists, but is not a SerializedPrimitive
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public Option<SerializedPrimitive> getPrimitive(@NotNull final String key) throws InvalidSerializedTypeException {
        final SerializedPrimitive primitive = getPrimitiveOrNull(key);
        if (primitive == null) {
            return Option.none();
        }
        return Option.some(primitive);
    }

    /**
     * Gets the value of the array at the key
     *
     * @param key the key
     * @return the possible SerializedArray wrapped in an option
     * @throws InvalidSerializedTypeException thrown if the key exists, but is not a SerializedArray
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public Option<SerializedArray> getArray(@NotNull final String key) throws InvalidSerializedTypeException {
        final SerializedArray array = getArrayOrNull(key);
        if (array == null) {
            return Option.none();
        }
        return Option.some(array);
    }

    /**
     * Gets the value of the object at the key
     *
     * @param key the key
     * @return the possible SerializedObject wrapped in an option
     * @throws InvalidSerializedTypeException thrown if the key exists, but is not a SerializedObject
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public Option<SerializedObject> getObject(@NotNull final String key) throws InvalidSerializedTypeException {
        final SerializedObject array = getObjectOrNull(key);
        if (array == null) {
            return Option.none();
        }
        return Option.some(array);
    }

    /**
     * Adds the serialized element
     *
     * @param key   the key to add it at
     * @param value the value
     * @since 1.0.0-SNAPSHOT
     */
    public void add(@NotNull final String key, @NotNull final SerializedElement value) {
        Preconditions.checkArgument(key != null && value != null, KEY_NULL_PRECONDITIONS_MESSAGE);
        this.map.put(key, value);
    }

    /**
     * Adds the integer
     *
     * @param key     the key
     * @param integer the integer
     * @since 1.0.0-SNAPSHOT
     */
    public void add(@NotNull final String key, final int integer) {
        Preconditions.checkArgument(key != null, KEY_NULL_PRECONDITIONS_MESSAGE);
        this.map.put(key, new SerializedPrimitive(integer));
    }

    /**
     * Adds the decimal
     *
     * @param key     the key
     * @param decimal the decimal
     * @since 1.0.0-SNAPSHOT
     */
    public void add(@NotNull final String key, final double decimal) {
        Preconditions.checkArgument(key != null, KEY_NULL_PRECONDITIONS_MESSAGE);
        this.map.put(key, new SerializedPrimitive(decimal));
    }


    /**
     * Adds the long
     *
     * @param key   the key
     * @param int64 the long
     * @since 1.0.0-SNAPSHOT
     */
    public void add(@NotNull final String key, final long int64) {
        Preconditions.checkArgument(key != null, KEY_NULL_PRECONDITIONS_MESSAGE);
        this.map.put(key, new SerializedPrimitive(int64));
    }

    /**
     * Adds the bool
     *
     * @param key  the key
     * @param bool the bool
     * @since 1.0.0-SNAPSHOT
     */
    public void add(@NotNull final String key, final boolean bool) {
        Preconditions.checkArgument(key != null, KEY_NULL_PRECONDITIONS_MESSAGE);
        this.map.put(key, new SerializedPrimitive(bool));
    }

    /**
     * Adds the string
     *
     * @param key    the key
     * @param string the string
     * @since 1.0.0-SNAPSHOT
     */
    public void add(@NotNull final String key, @NotNull final String string) {
        Preconditions.checkArgument(key != null, KEY_NULL_PRECONDITIONS_MESSAGE);
        this.map.put(key, new SerializedPrimitive(string));
    }

    /**
     * Checks if the map has the given key
     *
     * @param key the key to check
     * @return true if the map has the key, otherwise false
     * @since 1.0.0-SNAPSHOT
     */
    public boolean has(@NotNull final String key) {
        Preconditions.checkArgument(key != null, KEY_NULL_PRECONDITIONS_MESSAGE);
        return this.map.containsKey(key);
    }

    /**
     * Gets the keySet of this SerializedObject
     *
     * @return the key set
     * @since 1.0.0-SNAPSHOT
     */
    public Set<String> keySet() {
        return this.map.keySet();
    }

    /**
     * Gets the entry set of this serialized object
     *
     * @return the entry set
     * @since 1.0.0-SNAPSHOT
     */
    public Set<Map.Entry<String, SerializedElement>> entrySet() {
        return this.map.entrySet();
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof final SerializedObject that)) {
            return false;
        }
        return Objects.equals(map, that.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("JsonObject(").append(this.map.size()).append(") {\n");
        for (final Map.Entry<String, SerializedElement> entry : map.entrySet()) {
            builder.append("\"").append(entry.getKey()).append("\"").append(": ").append(entry.getValue()).append(",\n");
        }
        builder.append("}");
        return builder.toString();
    }
}
