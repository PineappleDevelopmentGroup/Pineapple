package sh.miles.pineapple.util.serialization.exception;

/**
 * Exception thrown if an exception occurs during the serialization or deserialization with Serialized adapters
 *
 * @since 1.0.0-SNAPSHOT
 */
public class SerializedAdaptationException extends SerializedException {

    public SerializedAdaptationException(final String exception) {
        super(exception);
    }

    public SerializedAdaptationException(final Exception exception) {
        super(exception);
    }
}
