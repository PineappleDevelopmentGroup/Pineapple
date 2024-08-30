package sh.miles.pineapple;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A Collection of Utilities useful for string manipulation
 *
 * @since 1.0.0-SNAPSHOT
 */
public final class StringUtils {

    private static final int LINE_CHAR_LIMIT = 50;
    private static final char HORIZONTAL_BAR = '⎯';
    private static final char VERTICAL_BAR = '│';

    private StringUtils() {
        throw new UnsupportedOperationException("no");
    }

    /**
     * Splits over a character instead of using regex
     *
     * @param string       the string to split
     * @param character    the character to split over
     * @param ignoreQuotes whether or not to ignore the split character in single quotes
     * @return the split string
     * @since 1.0.0-SNAPSHOT
     */
    public static List<String> split(String string, char character, boolean ignoreQuotes) {
        List<String> split = new ArrayList<>();
        char at;
        int last = 0;
        boolean escaped = false;
        for (int i = 0; i < string.length(); i++) {
            at = string.charAt(i);
            if (at == character && !escaped) {
                split.add(string.substring(last, i));
                last = i + 1;
            }

            if (at == '\'') {
                escaped = !escaped;
            }
        }

        if (last < string.length()) {
            split.add(string.substring(last));
        }

        return split;
    }

    /**
     * Splits over a character instead of using regex
     *
     * @param character the character to split over
     * @param string    the string to split
     * @return the split string
     * @since 1.0.0-SNAPSHOT
     */
    public static List<String> split(String string, char character) {
        List<String> split = new ArrayList<>();
        char at;
        int last = 0;
        for (int i = 0; i < string.length(); i++) {
            at = string.charAt(i);
            if (at == character) {
                split.add(string.substring(last, i));
                last = i + 1;
            }
        }

        if (last < string.length()) {
            split.add(string.substring(last));
        }

        return split;
    }

    /**
     * Boxes up an error in a pretty looking message
     *
     * @param errorMessage the errorMessage to box up
     * @return the formatted string
     */
    public static String boxError(@NotNull final String errorMessage) {
        Objects.requireNonNull(errorMessage);
        StringBuilder builder = new StringBuilder().append("\n");
        builder.append(VERTICAL_BAR).append(String.valueOf(HORIZONTAL_BAR).repeat(LINE_CHAR_LIMIT + 1)).append(VERTICAL_BAR).append('\n');
        builder.append(" ").append(" An Error Has Occurred").append("\n"); // 18 characters
        builder.append(VERTICAL_BAR).append(String.valueOf(HORIZONTAL_BAR).repeat(LINE_CHAR_LIMIT + 1)).append(VERTICAL_BAR).append('\n').append(' ');

        final char[] messageChars = errorMessage.toCharArray();
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
