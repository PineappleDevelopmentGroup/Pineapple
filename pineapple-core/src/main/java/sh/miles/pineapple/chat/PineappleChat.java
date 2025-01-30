package sh.miles.pineapple.chat;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * The main PineappleChat access point
 *
 * @since 1.0.0-SNAPSHOT
 */
public final class PineappleChat {

    private static final MiniMessage miniMessage = MiniMessage.builder().build();


    private PineappleChat() {
        throw new UnsupportedOperationException("can't instantiate utility class");
    }

    /**
     * Parses the given string into a Component
     *
     * @param string the string to parse
     * @return the Component
     * @since 1.0.0-SNAPSHOT
     */
    public static Component parse(@NotNull final String string) {
        return miniMessage.deserialize(string);
    }

    /**
     * Parses the given string into a Component
     *
     * @param string       the string to parse
     * @param replacements the replacements to put into the string
     * @return the Component
     * @since 1.0.0-SNAPSHOT
     */
    public static Component parse(@NotNull final String string, @NotNull final Map<String, Object> replacements) {
        List<TagResolver.Single> placeholders = replacements.entrySet().stream()
                .map(entry -> Placeholder.parsed(entry.getKey(), String.valueOf(entry.getValue())))
                .toList();
        return miniMessage.deserialize(string, TagResolver.resolver(placeholders));
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
