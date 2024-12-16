package sh.miles.pineapple.chat;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * The main PineappleChat access point
 *
 * @since 1.0.0-SNAPSHOT
 */
public final class PineappleChat {

    static final MiniMessage MINI_MESSAGE = MiniMessage.builder()
            .strict(false)
            .build();

    private PineappleChat() {
        throw new UnsupportedOperationException("can't instantiate utility class");
    }

    /**
     * Parses the given string into a BaseComponent
     *
     * @param string the string to parse
     * @return the BaseComponent
     * @since 1.0.0-SNAPSHOT
     */
    public static Component parse(@NotNull final String string) {
        return MINI_MESSAGE.deserialize(string);
    }

    /**
     * Parses the given string into a BaseComponent
     *
     * @param string       the string to parse
     * @param replacements the replacements to put into the string
     * @return the BaseComponent
     * @since 1.0.0-SNAPSHOT
     */
    public static Component parse(@NotNull final String string, @NotNull final Map<String, Object> replacements) {
        return MINI_MESSAGE.deserialize(string, replacements.keySet().stream().map((key) ->
                Placeholder.parsed("$" + key, replacements.get(key).toString())
        ).toArray(TagResolver[]::new));
    }

    /**
     * Creates a PineappleComponent from a source string
     * <p>
     * For more information regarding pineapple components see {@link PineappleComponent}
     *
     * @param source the source string to create a PineappleComponent from.
     * @return the PineappleComponent
     * @since 1.0.0-SNAPSHOT
     */
    public static PineappleComponent component(@NotNull final String source) {
        return new PineappleComponent(source);
    }

}
