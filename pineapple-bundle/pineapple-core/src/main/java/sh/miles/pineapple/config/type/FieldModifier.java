package sh.miles.pineapple.config.type;

import java.lang.reflect.Field;

public class FieldModifier {

    public static Object getValue(Field field, Object instance) {
        try {
            return field.get(instance);
        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException("Unable to access field: " + field.getName(), ex);
        }
    }

    public static void setField(Field field, Object value, Object instance) {
        try {
            field.set(instance, value);
        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException("Unable to set field value: " + field.getName(), ex);
        }
    }
}
