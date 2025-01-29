package sh.miles.pineapple.util.serialization.bridges.yaml;

import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.ReflectionUtils;
import sh.miles.pineapple.config.adapter.base.TypeAdapter;
import sh.miles.pineapple.util.serialization.Serialized;
import sh.miles.pineapple.util.serialization.SerializedArray;
import sh.miles.pineapple.util.serialization.SerializedElement;
import sh.miles.pineapple.util.serialization.SerializedObject;
import sh.miles.pineapple.util.serialization.SerializedPrimitive;

import java.lang.invoke.MethodHandle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sh.miles.pineapple.util.serialization.SerializedElement.array;
import static sh.miles.pineapple.util.serialization.SerializedElement.object;
import static sh.miles.pineapple.util.serialization.SerializedElement.primitive;

class SerializedAdapterToYamlAdapter<T> implements TypeAdapter<Object, T> {

    private static final MethodHandle SERIALIZED_PRIMITIVE_OBJECT = ReflectionUtils.getFieldAsGetter(SerializedPrimitive.class, "object");

    private final Class<T> clazz;

    public SerializedAdapterToYamlAdapter(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Class<Object> getSavedType() {
        return Object.class;
    }

    @Override
    public Class<T> getRuntimeType() {
        return this.clazz;
    }

    @Override
    public Object write(final T value, final Object existing, final boolean replace) {
        if (existing != null && !replace) {
            return null;
        }

        final SerializedElement element = Serialized.INSTANCE.serialize(value, this.clazz);
        return toObject(element);
    }

    @Override
    public T read(final Object saved) {
        final SerializedElement element = fromObject(saved);
        return Serialized.INSTANCE.deserialize(element, this.clazz);
    }

    private static SerializedElement fromObject(@NotNull final Object object) {
        if (object instanceof Map) {
            return fromMapObject((Map<String, Object>) object);
        } else if (object instanceof Collection) {
            return fromListObject((Collection<Object>) object);
        } else if (isPrimitive(object)) {
            return fromPrimitiveObject(object);
        } else {
            throw new IllegalStateException("Object of type %s can not be permitted for conversion to SerializedElement".formatted(object.getClass()));
        }
    }

    private static SerializedObject fromMapObject(@NotNull final Map<String, Object> map) {
        final SerializedObject object = object();
        for (final Map.Entry<String, Object> entry : map.entrySet()) {
            object.add(entry.getKey(), fromObject(entry.getValue()));
        }

        return object;
    }

    private static SerializedArray fromListObject(@NotNull final Collection<Object> collection) {
        SerializedArray array = array();
        for (final Object o : collection) {
            array.add(fromObject(o));
        }

        return array;
    }

    private static SerializedPrimitive fromPrimitiveObject(@NotNull final Object object) {
        if ((object instanceof Byte || object.getClass().isAssignableFrom(byte.class)) || (object instanceof Short || object.getClass().isAssignableFrom(short.class)) || (object instanceof Integer || object.getClass().isAssignableFrom(int.class))) {
            return primitive((int) object);
        } else if (object instanceof Long || object.getClass().isAssignableFrom(long.class)) {
            return primitive((long) object);
        } else if (object instanceof  Float || object.getClass().isAssignableFrom(float.class) || object instanceof Double || object.getClass().isAssignableFrom(double.class)) {
            return primitive((double) object);
        } else if (object instanceof String) {
            return primitive((String) object);
        } else {
            throw new IllegalStateException("Object of type %s can not be permitted for conversion to SerializedElement".formatted(object.getClass()));
        }
    }

    private static boolean isPrimitive(Object object) {
        return (object instanceof Byte || object.getClass().isAssignableFrom(byte.class)) || (object instanceof Short || object.getClass().isAssignableFrom(short.class)) || (object instanceof Integer || object.getClass().isAssignableFrom(int.class)) || (object instanceof Long || object.getClass().isAssignableFrom(long.class)) || (object instanceof Float || object.getClass().isAssignableFrom(float.class)) || (object instanceof Double || object.getClass().isAssignableFrom(double.class)) || (object instanceof Boolean || object.getClass().isAssignableFrom(boolean.class)) || (object instanceof String);
    }

    private static Object toObject(@NotNull final SerializedElement element) {
        if (element.isObject()) {
            return toMapObject(element.getAsObject());
        } else if (element.isArray()) {
            return toListObject(element.getAsArray());
        } else if (element.isPrimitive()) {
            return toPrimitiveObject(element.getAsPrimitive());
        } else {
            throw new IllegalStateException("SerializedElement is not instance of object, array, or primitive! This is a bug!");
        }
    }

    private static List<Object> toListObject(@NotNull final SerializedArray array) {
        final List<Object> list = new ArrayList<>();
        for (final SerializedElement element : array) {
            list.add(toObject(element));
        }

        return list;
    }

    private static Map<String, Object> toMapObject(@NotNull final SerializedObject object) {
        final Map<String, Object> map = new HashMap<>();
        for (final Map.Entry<String, SerializedElement> entry : object.entrySet()) {
            map.put(entry.getKey(), toObject(entry.getValue()));
        }

        return map;
    }

    private static Object toPrimitiveObject(@NotNull final SerializedPrimitive primitive) {
        try {
            return SERIALIZED_PRIMITIVE_OBJECT.bindTo(primitive).invoke();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
