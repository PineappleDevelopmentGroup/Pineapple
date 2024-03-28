package sh.miles.pineapple.util.serialize.exception;

import org.jetbrains.annotations.NotNull;

public class GenericSerializerNotFoundException extends GenericSerializationException {

    public GenericSerializerNotFoundException(@NotNull final Class<?> complexType, @NotNull final Class<?> storedType) {
        super("The class %s has no GenericSerializer with the storedType of %s".formatted(complexType.getName(), storedType.getName()));
    }
}
