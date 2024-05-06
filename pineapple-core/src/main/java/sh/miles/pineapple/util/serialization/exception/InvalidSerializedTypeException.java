package sh.miles.pineapple.util.serialization.exception;

/**
 * An exception thrown if a serialized type is invalid.
 * <p>
 * An Example is if you have a SerializedPrimitive and use getAsString and the type is actually an integer.
 *
 * @since 1.0.0-SNAPSHOT
 */
public class InvalidSerializedTypeException extends SerializedException {

    /**
     * Creates a new InvalidSerializedTypeException
     *
     * @param expected the expected type
     * @param found    the found type
     * @since 1.0.0-SNAPSHOT
     */
    public InvalidSerializedTypeException(Class<?> expected, Class<?> found) {
        super("Invalid SerializedType found, expected %s, but found %s");
    }
}
