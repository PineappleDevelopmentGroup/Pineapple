package sh.miles.pineapple.function;

/**
 * A supplier that can throw a checked exception
 *
 * @param <R> the type to be supplied
 * @since 1.0.0-SNAPSHOT
 */
@FunctionalInterface
public interface ThrowingSupplier<R> {

    /**
     * Gets the return type by applying the code inside the get method
     *
     * @return the return type
     * @throws Exception the checked exception thrown
     * @since 1.0.0-SNAPSHOT
     */
    R get() throws Exception;
}
