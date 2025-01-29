package sh.miles.pineapple.task.work;

/**
 * Executed as a callback when a ServerThreadWorker completes
 *
 * @param <R> the result
 * @since 1.0.0-SNAPSHOT
 */
public interface ServerThreadCallback<R> {

    /**
     * Completes using the result given to the callback
     *
     * @param result the result
     * @since 1.0.0-SNAPSHOT
     */
    void complete(R result);
}
