package sh.miles.pineapple.util.serialization.bridges.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.ReflectionUtils;
import sh.miles.pineapple.util.serialization.Serialized;
import sh.miles.pineapple.util.serialization.SerializedArray;
import sh.miles.pineapple.util.serialization.SerializedElement;
import sh.miles.pineapple.util.serialization.SerializedObject;
import sh.miles.pineapple.util.serialization.SerializedPrimitive;
import sh.miles.pineapple.util.serialization.exception.SerializedException;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Type;
import java.util.Map;

class SerializedAdapterToJsonAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {

    private static final MethodHandle JSON_PRIMITIVE_VALUE_FIELD = ReflectionUtils.getFieldAsGetter(JsonPrimitive.class, "value");

    private final Class<T> clazz;

    public SerializedAdapterToJsonAdapter(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
        final SerializedElement element = fromJson(json);
        return Serialized.INSTANCE.deserialize(element, this.clazz);
    }

    @Override
    public JsonElement serialize(final T src, final Type typeOfSrc, final JsonSerializationContext context) {
        final SerializedElement element = Serialized.INSTANCE.serialize(src, this.clazz);
        return fromSerialized(element);
    }

    private JsonElement fromSerialized(@NotNull final SerializedElement element) {
        if (element.isPrimitive()) {
            return fromSerializedPrimitive(element.getAsPrimitive());
        } else if (element.isArray()) {
            return fromSerializedArray(element.getAsArray());
        } else if (element.isObject()) {
            return fromSerializedObject(element.getAsObject());
        } else {
            throw new SerializedException("Unable to convert SerializedElement to JsonElement this should never occur and is a serious bug!");
        }
    }

    private JsonObject fromSerializedObject(@NotNull final SerializedObject serializedObject) {
        final JsonObject object = new JsonObject();
        for (final Map.Entry<String, SerializedElement> entry : serializedObject.entrySet()) {
            object.add(entry.getKey(), fromSerialized(entry.getValue()));
        }
        return object;
    }

    private JsonArray fromSerializedArray(@NotNull final SerializedArray serializedArray) {
        final JsonArray array = new JsonArray();
        for (final SerializedElement element : serializedArray) {
            array.add(fromSerialized(element));
        }
        return array;
    }

    private JsonPrimitive fromSerializedPrimitive(@NotNull final SerializedPrimitive serializedPrimitive) {
        JsonPrimitive primitive;
        if (serializedPrimitive.isInt()) {
            return new JsonPrimitive(serializedPrimitive.getAsInt());
        } else if (serializedPrimitive.isLong()) {
            return new JsonPrimitive(serializedPrimitive.getAsLong());
        } else if (serializedPrimitive.isDouble()) {
            return new JsonPrimitive(serializedPrimitive.getAsDouble());
        } else if (serializedPrimitive.isBoolean()) {
            return new JsonPrimitive(serializedPrimitive.getAsBoolean());
        } else if (serializedPrimitive.isString()) {
            return new JsonPrimitive(serializedPrimitive.getAsString());
        }

        throw new SerializedException("The type of the primitive value %s can not be expressed. THIS IS A BUG!".formatted(serializedPrimitive.getTypeOfPrimitive()));
    }

    private SerializedElement fromJson(@NotNull final JsonElement jsonElement) {
        if (jsonElement.isJsonPrimitive()) {
            return fromJsonPrimitive(jsonElement.getAsJsonPrimitive());
        } else if (jsonElement.isJsonArray()) {
            return fromJsonArray(jsonElement.getAsJsonArray());
        } else if (jsonElement.isJsonObject()) {
            return fromJsonObject(jsonElement.getAsJsonObject());
        } else {
            throw new SerializedException("Unable to convert JsonElement to SerializedElement because this element is likely null");
        }
    }

    private SerializedObject fromJsonObject(@NotNull final JsonObject jsonObject) {
        final SerializedObject object = SerializedElement.object();
        for (final Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            object.add(entry.getKey(), fromJson(entry.getValue()));
        }
        return object;
    }

    private SerializedArray fromJsonArray(@NotNull final JsonArray jsonArray) {
        final SerializedArray array = SerializedElement.array(jsonArray.size());
        for (final JsonElement jsonElement : jsonArray) {
            array.add(fromJson(jsonElement));
        }
        return array;
    }

    private SerializedPrimitive fromJsonPrimitive(@NotNull final JsonPrimitive primitive) {
        try {
            return SerializedElement.primitive(JSON_PRIMITIVE_VALUE_FIELD.bindTo(primitive).invoke());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
