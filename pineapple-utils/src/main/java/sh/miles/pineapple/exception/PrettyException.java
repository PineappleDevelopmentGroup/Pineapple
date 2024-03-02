package sh.miles.pineapple.exception;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * Allows the creation of "pretty" exceptions
 */
public final class PrettyException {

    private static final int LINE_CHAR_LIMIT = 50;
    private static final char WORD_SPLITTER = '-';
    private static final char HORIZONTAL_BAR = '⎯';
    private static final char VERTICAL_BAR = '│';

    private final String message;
    private final Logger logger;
    private final Exception exception;
    private final Runnable onFailure;

    /**
     * Creates a new pretty exception
     *
     * @param message   the message
     * @param exception the exception that has occurred
     * @param onFailure the failure action
     */
    public PrettyException(@NotNull final String message, @NotNull final Logger logger, @Nullable final Exception exception, final Runnable onFailure) {
        this.message = prettify(message);
        this.logger = logger;
        this.exception = exception;
        this.onFailure = onFailure;
    }

    /**
     * Logs the exception runs the onFailure action and returns this pretty exception
     *
     * @return this pretty exception
     */
    public PrettyException andLogged() {
        this.logger.severe(this.message);
        this.onFailure.run();
        return this;
    }

    /**
     * Logs the exception and runs the onFailure action
     */
    public void logged() {
        this.logger.severe(this.message);
        this.onFailure.run();
    }

    public void throwEmpty() throws RuntimeException {
        throw new RuntimeException();
    }

    /**
     * Throws a runtime exception with the given exception
     *
     * @throws RuntimeException the runtime exception
     */
    public void throwWithoutMessage() throws RuntimeException {
        if (this.exception != null) {
            throw new RuntimeException(this.exception);
        } else {
            throw new RuntimeException();
        }
    }

    /**
     * Throws a runtime exception with a RuntimeException and triggers the onFailure action
     *
     * @throws RuntimeException the runtime exception
     */
    public void throwing() throws RuntimeException {
        Objects.requireNonNull(this.exception);
        this.onFailure.run();
        throw new RuntimeException("\n" + this.message, this.exception);
    }

    public static String prettify(String message) {
        StringBuilder builder = new StringBuilder();
        builder.append(VERTICAL_BAR).append(String.valueOf(HORIZONTAL_BAR).repeat(LINE_CHAR_LIMIT + 1)).append(VERTICAL_BAR).append('\n');
        builder.append(" ").append(" An Error Has Occurred").append("\n"); // 18 characters
        builder.append(VERTICAL_BAR).append(String.valueOf(HORIZONTAL_BAR).repeat(LINE_CHAR_LIMIT + 1)).append(VERTICAL_BAR).append('\n').append(' ');

        final char[] messageChars = message.toCharArray();
        boolean wrappedOnEnd = false;
        for (int i = 0; i < messageChars.length; i++) {
            final char cur = messageChars[i];
            if ((i != 0 && i % LINE_CHAR_LIMIT == 0) || wrappedOnEnd) {
                if (cur != ' ') {
                    wrappedOnEnd = true;
                } else {
                    builder.append('\n');
                    wrappedOnEnd = false;
                }
            }

            builder.append(cur);
        }

        builder.append("\n").append(VERTICAL_BAR).append(String.valueOf(HORIZONTAL_BAR).repeat(LINE_CHAR_LIMIT + 1)).append(VERTICAL_BAR);

        return builder.toString();
    }

}
