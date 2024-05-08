package sh.miles.pineapple.util.serialization.exception;

/**
 * Most generic SerializedException serving as a base for all other SerializedExceptions
 *
 * @since 1.0.0-SNAPSHOT
 */
public class SerializedException extends RuntimeException {

    /**
     * Creates a SerializedException from a string
     *
     * @param exception the string exception
     * @since 1.0.0-SNAPSHOT
     */
    public SerializedException(String exception) {
        super(exception);
    }

    /**
     * Creates a SerializedException from a SerializedException
     *
     * @param exception the exception
     * @since 1.0.0-SNAPSHOT
     */
    public SerializedException(SerializedException exception) {
        super(exception);
    }

    /**
     * Creates a SerializedException from an exception
     *
     * @param exception the exception
     * @since 1.0.0-SNAPSHOT
     */
    public SerializedException(Exception exception) {
        super(exception);
    }

}
