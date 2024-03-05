package sh.miles.pineapple.exception;

import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.StringUtils;
import sh.miles.pineapple.function.Option;
import sh.miles.pineapple.function.Option.Some;
import sh.miles.pineapple.function.ThrowingSupplier;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * Tracks "Anomalies" That occur in code and prettify any errors that they cause
 */
public class Anomaly<R> {

    private final Logger logger;
    private Option<R> returnValue = Option.none();
    private Option<String> message = Option.none();
    private Option<Exception> exception = Option.none();
    private Option<Runnable> onError = Option.none();
    private boolean logged = false;
    private boolean throwExceptionIfExists = true;

    /**
     * Creates an empty anomaly with a logger
     *
     * @param logger the logger
     */
    public Anomaly(@NotNull final Logger logger) {
        this.logger = Objects.requireNonNull(logger);
    }

    /**
     * Creates an anomaly with an exception and message
     *
     * @param logger    the logger
     * @param exception the exception
     * @param message   the message
     */
    public Anomaly(@NotNull final Logger logger, @NotNull final Exception exception, @NotNull final String message) {
        this.logger = Objects.requireNonNull(logger);
        this.exception = Option.some(exception);
        this.message = Option.some(message);
    }

    /**
     * Makes the returned anomaly not log the given message
     *
     * @return the new anomaly
     */
    public Anomaly<R> noLog() {
        var copy = copy(this);
        copy.logged = true;
        return copy;
    }

    /**
     * Causes the returned anomaly to not throw an exception if {@link #hard()} is used even if one exists
     *
     * @return the new anomaly
     */
    public Anomaly<R> noThrowException() {
        var copy = copy(this);
        copy.throwExceptionIfExists = false;
        return copy;
    }

    /**
     * Sets the message for a copy of this anomaly
     *
     * @param message the message
     * @return the new anomaly
     */
    public Anomaly<R> message(@NotNull final String message) {
        var copy = copy(this);
        copy.message = Option.some(StringUtils.boxError(message));
        return copy;
    }

    /**
     * Executes a runnable when the returned Anomaly errors
     *
     * @param runnable the runnable
     * @return the new anomaly
     */
    public Anomaly<R> onError(@NotNull final Runnable runnable) {
        var copy = copy(this);
        copy.onError = Option.some(runnable);
        return copy;
    }

    /**
     * Runs a piece of code in a throwing supplier which allows the anomaly to swallow its error and propagate it
     * forward
     *
     * @param supplier the supplier to run
     * @param <T>      the new return value
     * @return the new anomaly
     */
    public <T> Anomaly<T> run(@NotNull final ThrowingSupplier<T> supplier) {
        try {
            var value = supplier.get();
            return copyNewValue(this, Option.some(value));
        } catch (Exception e) {
            var copy = copyNewValue(this, new Option.None<T>());
            copy.exception = Option.some(e);
            return copy;
        }
    }

    /**
     * Logs the result of the anomaly with the given message if an error occurred
     *
     * @param runningClass the running class of this anomaly
     * @param methodName   the method name this anomaly is running in
     * @return this anomaly
     */
    @NotNull
    public Anomaly<R> log(@NotNull final Class<?> runningClass, @NotNull final String methodName) {
        if (this.exception instanceof Some<Exception> some && !this.logged) {
            if (this.message instanceof Some<String> someMessage) {
                this.logger.severe(someMessage.some());
            }
            this.logger.throwing(runningClass.getName(), methodName, some.some());
            this.logged = true;
        }
        return this;
    }

    /**
     * Completes the anomaly softly and does not throw or log any exceptions even if they occurred
     *
     * @return the return value
     */
    public Option<R> soft() {
        if (this.exception instanceof Some<Exception> && this.onError instanceof Some<Runnable> some) {
            some.some().run();
        }
        return this.returnValue;
    }

    /**
     * Completes the anomaly with a RuntimeException takes into account if the error has been logged already or not when
     * throwing
     * <p>
     * This method only throws a runtime exception if {@link #noThrowException()} is not used
     *
     * @return the return value
     */
    public Option<R> hard() throws RuntimeException {
        if (this.exception instanceof Some<Exception> some && this.throwExceptionIfExists) {
            if (this.onError instanceof Some<Runnable> someRunnable) {
                someRunnable.some().run();
            }
            if (logged) {
                throw new RuntimeException(some.some());
            } else {
                if (this.message instanceof Some<String> someMessage) {
                    throw new RuntimeException(someMessage.some(), some.some());
                } else {
                    throw new RuntimeException(some.some());
                }
            }
        }
        return this.returnValue;
    }

    /**
     * Copies the current anomaly settings but returns an anomaly with a different return value
     *
     * @param anomaly     the original anomaly
     * @param returnValue the new return value
     * @param <R>         the return value
     * @param <T>         the old type of return value
     * @return the new anomaly
     */
    private static <R, T> Anomaly<R> copyNewValue(@NotNull final Anomaly<T> anomaly, @NotNull final Option<R> returnValue) {
        final Anomaly<R> copy = new Anomaly<>(anomaly.logger);
        copy.logged = anomaly.logged;
        copy.exception = anomaly.exception;
        copy.message = anomaly.message;
        copy.onError = anomaly.onError;
        copy.returnValue = returnValue;
        return copy;
    }

    /**
     * Copies the given anomaly into another object
     *
     * @param anomaly the anomaly
     * @param <R>     the return value type
     * @return the newly created anomaly
     */
    private static <R> Anomaly<R> copy(Anomaly<R> anomaly) {
        final Anomaly<R> copy = new Anomaly<>(anomaly.logger);
        copy.returnValue = anomaly.returnValue;
        copy.logged = anomaly.logged;
        copy.exception = anomaly.exception;
        copy.onError = anomaly.onError;
        copy.message = anomaly.message;
        return copy;
    }

}
