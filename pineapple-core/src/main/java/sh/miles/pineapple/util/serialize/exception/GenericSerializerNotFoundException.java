package sh.miles.pineapple.util.serialize.exception;

import org.jetbrains.annotations.NotNull;

public class GenericSerializerNotFoundException extends GenericSerializationException {

    public GenericSerializerNotFoundException(@NotNull final Class<?> clazz) {
        super("The class %s has no GenericSerializer".formatted(clazz.getName()));
    }
}
