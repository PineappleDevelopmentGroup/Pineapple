package sh.miles.pineapple.task.work;

import org.bukkit.Bukkit;

/**
 * Intended to be used within the {@link ServerThreadWorker}. Basic computation value and can easily be extended
 */
public interface ServerThreadWorker {

    /**
     * Does the necessary computation
     *
     * @since 1.0.0-SNAPSHOT
     */
    void compute() throws Throwable;

    /**
     * Run when an exception occurs and output is needed
     *
     * @param throwable the error to be thrown
     * @since 1.0.0-SNAPSHOT
     */
    default void exceptionally(Throwable throwable) {
        Bukkit.getLogger().throwing("ServerThreadWorker", "exceptionally", throwable);
    }

}
