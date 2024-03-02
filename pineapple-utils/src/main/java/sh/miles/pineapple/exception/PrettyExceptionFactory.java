package sh.miles.pineapple.exception;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;

/**
 * Factory used to mass produce beautiful exceptions
 */
public class PrettyExceptionFactory {

    private final Logger logger;
    @Nullable
    private final Runnable onFail;

    /**
     * Creates a pretty exception factory
     *
     * @param logger the logger used for the exceptions
     * @param onFail what should occur on fail by default
     */
    public PrettyExceptionFactory(@NotNull Logger logger, final @Nullable Runnable onFail) {
        this.logger = logger;
        this.onFail = onFail;
    }

    /**
     * Creates a new PrettyException
     *
     * @param message   the message to send
     * @param exception the exception
     * @return a pretty exception
     */
    @NotNull
    public PrettyException create(@NotNull final String message, @NotNull final Exception exception) {
        return new PrettyException(message, this.logger, exception, this.onFail == null ? () -> {
        } : this.onFail);
    }


    /**
     * Creates a new PrettyException
     *
     * @param message the message to send
     * @return a pretty exception
     */
    @NotNull
    public PrettyException create(@NotNull final String message) {
        return new PrettyException(message, this.logger, null, this.onFail);
    }


    /**
     * Creates a new PrettyException
     *
     * @param message the message to send
     * @param onFail  the event to occur on fail
     * @return a pretty exception
     */
    @NotNull
    public PrettyException create(@NotNull final String message, @NotNull final Runnable onFail) {
        return new PrettyException(message, this.logger, null, onFail);
    }

    /**
     * Creates a PrettyException
     *
     * @param message   the message to send
     * @param exception the exception source
     * @param onFail    what occurs on fail
     * @return this pretty exception
     */
    @NotNull
    public PrettyException create(@NotNull final String message, @NotNull final Exception exception, @NotNull final Runnable onFail) {
        return new PrettyException(message, this.logger, exception, onFail);
    }

}
