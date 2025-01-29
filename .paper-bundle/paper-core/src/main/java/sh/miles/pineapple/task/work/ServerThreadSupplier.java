package sh.miles.pineapple.task.work;

/**
 * Intended to be used within the {@link ServerThreadWorker}. Basic supplier of a value and can easily be extended
 *
 * @param <R> the result type
 * @since 1.0.0-SNAPSHOT
 */
public interface ServerThreadSupplier<R> extends ServerThreadWorker {

    /**
     * The result of the computation
     *
     * @return the resulting object
     * @since 1.0.0-SNAPSHOT
     */
    R getResult();

}
