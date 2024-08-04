package sh.miles.pineapple.exception;

import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.StringUtils;
import sh.miles.pineapple.function.Option;
import sh.miles.pineapple.function.Option.None;
import sh.miles.pineapple.function.Option.Some;
import sh.miles.pineapple.function.ThrowingSupplier;

import java.util.logging.Logger;

/**
 * Tracks "Anomalies" That occur in code and prettify any errors that they cause
 */
public class Anomaly<R> {
    private Option<R> returnValue = Option.none();
    // display actions
    private Option<String> message = Option.none();
    private Option<Exception> exception = Option.none();
    private Option<Runnable> onFail = Option.none();
    // settings
    private boolean logException = true;
    private boolean throwLog = true;
    private boolean throwRuntimeException = true;
    private boolean useExceptionMessage = false;

    private final Logger logger;

    /**
     * Creates a new anomaly
     *
     * @param logger the logger
     */
    public Anomaly(@NotNull final Logger logger) {
        this.logger = logger;
    }

    /**
     * Sets the message of the returned message
     *
     * @param message the message
     * @return a newly created anomaly
     */
    public Anomaly<R> message(@NotNull final String message) {
        var anomaly = copy(this);
        anomaly.message = Option.some(StringUtils.boxError(message));
        return anomaly;
    }

    /**
     * Sets the failure function on the returned anomaly
     *
     * @param onFail the onFail function
     * @return a newly created anomaly
     */
    public Anomaly<R> onFail(@NotNull final Runnable onFail) {
        var anomaly = copy(this);
        anomaly.onFail = Option.some(onFail);
        return anomaly;
    }

    /**
     * Sets the throwLog setting to false on the returned anomaly
     * <p>
     * The throwLog setting determines whether the error should be printed as in the log file
     *
     * @return a newly created anomaly
     */
    public Anomaly<R> noThrowLog() {
        var anomaly = copy(this);
        anomaly.throwLog = false;
        return anomaly;
    }

    /**
     * Sets the useExceptionMessage setting to true on the returned anomaly
     * <p>
     * the useExceptionMessage automatically uses the exception message if an error is thrown from the supplier in
     * {@link #run(ThrowingSupplier)}
     *
     * @return the newly created anomaly
     */
    public Anomaly<R> useExceptionMessage() {
        var anomaly = copy(this);
        anomaly.useExceptionMessage = true;
        return anomaly;
    }

    /**
     * Sets the logException field to false on the returned anomaly
     * <p>
     * when log exception is false the exception is not logged in any capacity. Unlike {@link #noThrowLog} this is a
     * package deal and includes the "Pretty" exception
     *
     * @return the newly created anomaly
     */
    public Anomaly<R> noLogException() {
        var anomaly = copy(this);
        anomaly.logException = false;
        return anomaly;
    }

    /**
     * Sets the throwRuntimeException field to false on the returned anomaly
     * <p>
     * when running {@link #hard(Class, String)} throwRuntimeException does not throw a RuntimeException even if an
     * exception has occurred.
     *
     * @return the newly created anomaly
     */
    public Anomaly<R> noThrowRuntimeException() {
        var anomaly = copy(this);
        anomaly.throwRuntimeException = false;
        return anomaly;
    }

    /**
     * Runs a method and returns a new anomaly updating the values according to its return
     *
     * @param supplier the function to run
     * @param <T>      the type returned by the supplier
     * @return a newly created anomaly
     */
    public <T> Anomaly<T> run(@NotNull final ThrowingSupplier<T> supplier) {
        Anomaly<T> copy;
        try {
            var value = supplier.get();
            copy = copyWithValue(this, value);
            copy.returnValue = Option.some(value);
        } catch (Exception exception) {
            copy = copyWithoutValue(this);
            copy.exception = Option.some(exception);
            if (useExceptionMessage) {
                copy.message = Option.some(StringUtils.boxError(exception.getMessage()));
            }
        }

        return copy;
    }

    /**
     * Completes the Anomaly transaction using the settings the anomaly was given
     * <p>
     * Unlike {@link #hard(Class, String)} this method will never throw a runtime error but instead logs the error and
     * peacefully continues execution. This plugin will also never trigger a runnable provided by
     * {@link #onFail(Runnable)}
     *
     * @param clazz  the class the anomaly was completed in
     * @param method the method the anomaly was completed in
     * @return the possible return value
     */
    public Option<R> soft(@NotNull final Class<?> clazz, @NotNull final String method) {
        if (exception instanceof None<Exception>) {
            return this.returnValue;
        }

        var exception = this.exception.orThrow();
        if (this.logException) {
            if (this.message instanceof Some<String> some) {
                this.logger.severe(some.some());
            }

            if (this.throwLog) {
                this.logger.throwing(clazz.getName(), method, exception);
            }
        }

        return this.returnValue;
    }

    /**
     * Completes the Anomaly transaction using the settings the anomaly was given
     * <p>
     * Unlike {@link #soft(Class, String)} this method can throw a RuntimeException. This method also triggers the
     * {@link #onFail(Runnable)}. If a method is supplied for {@link #onFail(Runnable)} no RuntimeException would be
     * executed.
     *
     * @param clazz  the class the anomaly was completed in
     * @param method the method the anomaly was completed in
     * @return the possible return value
     * @throws RuntimeException the possible exception that occurred
     */
    public Option<R> hard(@NotNull final Class<?> clazz, @NotNull final String method) throws RuntimeException {
        if (this.exception instanceof None<Exception>) {
            return this.returnValue;
        }

        var exception = this.exception.orThrow();
        if (this.logException) {
            if (this.message instanceof Some<String> some) {
                this.logger.severe(some.some());
            }

            if (this.throwLog) {
                this.logger.throwing(clazz.getName(), method, exception);
            }
        }

        if (this.onFail instanceof Some<Runnable> some) {
            some.some().run();
            return this.returnValue;
        }

        if (throwRuntimeException) {
            throw new RuntimeException(exception);
        }

        return this.returnValue;
    }

    private static <R> Anomaly<R> copy(Anomaly<R> old) {
        Anomaly<R> copy = copyWithoutValue(old);
        copy.returnValue = old.returnValue;
        return copy;
    }

    private static <R, T> Anomaly<R> copyWithValue(Anomaly<T> old, R value) {
        Anomaly<R> copy = copyWithoutValue(old);
        copy.returnValue = Option.some(value);
        return copy;
    }

    private static <R> Anomaly<R> copyWithoutValue(Anomaly<?> old) {
        var copy = new Anomaly<R>(old.logger);
        // display action
        copy.message = old.message;
        copy.exception = old.exception;
        copy.onFail = old.onFail;
        // settings
        copy.throwLog = old.throwLog;
        copy.logException = old.logException;
        copy.useExceptionMessage = old.useExceptionMessage;
        copy.throwRuntimeException = old.throwRuntimeException;
        return copy;
    }
}
