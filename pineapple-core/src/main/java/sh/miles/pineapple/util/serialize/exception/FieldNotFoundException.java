package sh.miles.pineapple.util.serialize.exception;

import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.util.serialize.impl.GenericSerializable;

/**
 * Exception thrown if a field is not found within a serializer
 *
 * @since 1.0.0-SNAPSHOT
 */
public class FieldNotFoundException extends GenericSerializationException {

    /**
     * Creates a new FieldNotFoundException
     *
     * @param field the field that wasn't found
     * @param clazz the class of the serializer
     * @since 1.0.0-SNAPSHOT
     */
    public FieldNotFoundException(@NotNull final String field, @NotNull final Class<? extends GenericSerializable> clazz) {
        super("The field %s does not exist for the generic serializer of type %s".formatted(field, clazz.getSimpleName()));
    }
}
