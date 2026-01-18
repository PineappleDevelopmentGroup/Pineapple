package sh.miles.pineapple.exception;

import org.jspecify.annotations.NullMarked;

import java.util.logging.Logger;

/**
 * A Factory for creating anomalies
 */
@NullMarked
public class AnomalyFactory {

    private final Logger logger;

    /**
     * Creates a new anomaly factory
     *
     * @param logger the logger to use
     */
    public AnomalyFactory(final Logger logger) {
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
    public <R> Anomaly<R> create(final R returnValue) {
        return new Anomaly<R>(this.logger)
            .run(() -> returnValue);
    }

}
