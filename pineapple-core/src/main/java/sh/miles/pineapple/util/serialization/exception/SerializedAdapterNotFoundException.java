package sh.miles.pineapple.util.serialization.exception;

/**
 * Error if no adapter is found for a type
 *
 * @since 1.0.0-SNAPSHOT
 */
public class SerializedAdapterNotFoundException extends SerializedException {
    public SerializedAdapterNotFoundException(final Class<?> type) {
        super("Could not find valid registered adapter for the type %s so it could not be handled".formatted(type));
    }
}
