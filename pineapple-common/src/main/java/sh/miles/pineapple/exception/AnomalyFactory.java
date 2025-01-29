package sh.miles.pineapple.exception;

import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

/**
 * A Factory for creating anomalies
 */
public class AnomalyFactory {

    private final Logger logger;

    /**
     * Creates a new anomaly factory
     *
     * @param logger the logger to use
     */
    public AnomalyFactory(@NotNull final Logger logger) {
        this.logger = logger;
    }

    /**
     * Creates an empty anomaly
     *
     * @return the new anomaly
     */
    public Anomaly<Void> create() {
        return new Anomaly<>(this.logger);
    }

    /**
     * Creates an anomaly with the return value
     *
     * @param returnValue the return value
     * @param <R>         the return type
     * @return the new anomaly
     */
    public <R> Anomaly<R> create(@NotNull final R returnValue) {
        return new Anomaly<R>(this.logger)
                .run(() -> returnValue);
    }

}
