package sh.miles.pineapple.config.type;

import java.lang.reflect.Field;

public class FieldModifier {

    /**
     * Retrieve the value of a field
     *
     * @param field to get the value of
     * @param instance instance if required
     * @return the value
     */
    public static Object getValue(Field field, Object instance) {
        try {
            return field.get(instance);
        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException("Unable to access field: " + field.getName(), ex);
        }
    }

    /**
     * Sets the value of a field
     *
     * @param field to set the value of
     * @param value value to set
     * @param instance instance if required
     */
    public static void setField(Field field, Object value, Object instance) {
        try {
            field.set(instance, value);
        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException("Unable to set field value: " + field.getName(), ex);
        }
    }
}
