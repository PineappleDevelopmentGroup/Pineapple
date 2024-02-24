package sh.miles.pineapple.config;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * @param <T> ClassType
 * @author Coll1234567/Jishuna
 */
public class ConfigType<T> {

    private final Class<T> clazz;
    private final List<ConfigType<?>> componentTypes;

    /**
     * Create a ConfigType from a {@link Type}
     *
     * @param type the type
     * @return ConfigType of type
     */
    public static ConfigType<?> create(Type type) {
        if (type instanceof Class) {
            return new ConfigType<>((Class<?>) type, new ArrayList<>());
        }
        return create(type.getTypeName());
    }

    public static ConfigType<?> get(Field field) {
        return create(field.getGenericType());
    }

    private static ConfigType<?> create(String typeName) {
        try {
            int ind = typeName.indexOf('<');
            if (ind == -1) {
                return new ConfigType<>(Class.forName(typeName));
            }
            Class<?> clazz = Class.forName(typeName.substring(0, ind));
            List<ConfigType<?>> componentTypes = splitTypeName(typeName, ind + 1, typeName.length() - 1)
                    .stream()
                    .map(ConfigType::create)
                    .collect(Collectors.toList());
            return new ConfigType<>(clazz, componentTypes);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("All parameter types for config must be known at compile time", e);
        }
    }

    private static List<String> splitTypeName(String str, int start, int end) {
        final StringBuilder current = new StringBuilder();
        final List<String> splitOff = new ArrayList<>();
        char cur = str.charAt(end);
        if (cur != '>') {
            return splitOff; // there is no generics no need to initiate loop
        }

        for (int i = end - 1; i >= start; i--) {
            cur = str.charAt(i);

            if (cur == ',') {
                splitOff.add(current.toString().trim());
                current.setLength(0);
                continue;
            }
            current.insert(0, cur);
        }

        if (!current.isEmpty()) {
            splitOff.add(current.toString());
        }

        return splitOff;
    }

    public ConfigType(Class<T> clazz, List<ConfigType<?>> componentTypes) {
        this.clazz = clazz;
        this.componentTypes = componentTypes;
    }

    public ConfigType(Class<T> clazz) {
        this(clazz, new ArrayList<>());
    }

    public Class<T> getType() {
        return this.clazz;
    }

    public List<ConfigType<?>> getComponentTypes() {
        return this.componentTypes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.clazz, this.componentTypes);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ConfigType)) {
            return false;
        }
        ConfigType<?> type = (ConfigType<?>) o;
        return type.clazz.equals(this.clazz) && type.componentTypes.equals(this.componentTypes);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder().append(this.clazz.getName());
        if (!this.componentTypes.isEmpty()) {
            str.append("<").append(this.componentTypes.stream().map(ConfigType::toString).collect(Collectors.joining(", "))).append(">");
        }
        return str.toString();
    }
}
