package sh.miles.pineapple.task.work;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.collection.Pair;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * injects tasks into the main thread. The ServerThreadTicker will attempt to only run as many tasks as possible on the
 * main thread without heavily impacting main thread performance
 *
 * @since 1.0.0-SNAPSHOT
 */
public class ServerThreadTicker implements Runnable {

    /**
     * MAX MILLIS PER TICK
     */
    public static final double MAX_MILLIS_PER_TICK = 2.5;
    /**
     * MAX NANOS PER TICK
     */
    public static final int MAX_NANOS_PER_TICK = (int) (MAX_MILLIS_PER_TICK * 1E6);

    private final Deque<Pair<ServerThreadWorker, ServerThreadCallback<Object>>> workers = new ConcurrentLinkedDeque<>();

    public ServerThreadTicker(@NotNull final Plugin plugin) {
        Bukkit.getScheduler().runTaskTimer(plugin, this, 1L, 1L);
    }

    /**
     * Queues a task on the main thread
     *
     * @param worker the worker to queue
     * @since 1.0.0-SNAPSHOT
     */
    public void queue(@NotNull final ServerThreadWorker worker) {
        this.workers.add(Pair.of(worker, null));
    }

    /**
     * Queues a task on the main thread with a callback
     *
     * @param worker   the worker to queue
     * @param callback the callback to execute when the worker finished
     * @since 1.0.0-SNAPSHOT
     */
    public void queue(@NotNull final ServerThreadWorker worker, @NotNull final ServerThreadCallback<Object> callback) {
        this.workers.add(Pair.of(worker, callback));
    }

    @ApiStatus.Internal
    @Override
    public void run() {
        long stopTime = System.nanoTime() + MAX_NANOS_PER_TICK;

        Pair<ServerThreadWorker, ServerThreadCallback<Object>> next;
        ServerThreadWorker worker;
        ServerThreadCallback<Object> callback;
        while (System.nanoTime() <= stopTime && (next = this.workers.poll()) != null) {
            worker = next.left();
            callback = next.right();
            try {
                worker.compute();
                Object value = null;
                if (worker instanceof ServerThreadSupplier<?>) {
                    value = ((ServerThreadSupplier<?>) worker).getResult();
                }
                callback.complete(value);
            } catch (Throwable throwable) {
                worker.exceptionally(throwable);
            }
        }
    }
}
