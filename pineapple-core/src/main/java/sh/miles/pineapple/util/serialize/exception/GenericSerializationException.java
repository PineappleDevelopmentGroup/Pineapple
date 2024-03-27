package sh.miles.pineapple.util.serialize.exception;

import org.jetbrains.annotations.NotNull;

/**
 * A Generic exception that all GenericSerialization exceptions sprout from
 *
 * @since 1.0.0-SNAPSHOT
 */
public class GenericSerializationException extends IllegalStateException {

    /**
     * Throws a generic serialization exception
     *
     * @param exception the exception message
     * @since 1.0.0-SNAPSHOT
     */
    public GenericSerializationException(@NotNull final String exception) {
        super(exception);
    }

}
