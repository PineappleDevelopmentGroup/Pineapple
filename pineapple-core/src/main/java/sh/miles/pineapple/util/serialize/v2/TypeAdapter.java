package sh.miles.pineapple.util.serialize.v2;

/**
 * Adapts types from a stored type to a runtime type
 *
 * @param <S> Saved or stored type
 * @param <R> Runtime type
 * @since 1.0.0-SNAPSHOT
 */
public interface TypeAdapter<S, R> {

    /**
     * Serializes a value to the stored type
     *
     * @param value    the value
     * @param existing the existing type
     * @param replace  whether to replace the existing content if it exists
     * @return the stored type
     * @since 1.0.0-SNAPSHOT
     */
    S serialize(R value, S existing, boolean replace);

    /**
     * Deserializes the stored type to the runtime type
     *
     * @param saved the saved object
     * @return the runtime type
     * @since 1.0.0-SNAPSHOT
     */
    R deserialize(S saved);

    /**
     * @return the saved type
     * @since 1.0.0-SNAPSHOT
     */
    Class<S> getSavedType();

    /**
     * @return the runtime type
     * @since 1.0.0-SNAPSHOT
     */
    Class<R> getRuntimeType();

}
