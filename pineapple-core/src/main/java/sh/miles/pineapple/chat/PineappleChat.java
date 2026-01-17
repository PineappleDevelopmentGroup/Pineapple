package sh.miles.pineapple.chat;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Map;

/**
 * The main PineappleChat access point
 *
 * @since 1.0.0-SNAPSHOT
 */
@NullMarked
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
    public static Component parse(final String string) {
        return string == null || string.isEmpty() ? Component.empty() : miniMessage.deserialize(string);
    }

    /**
     * Parses the given string into a Component
     *
     * @param string       the string to parse
     * @param replacements the replacements to put into the string
     * @return the Component
     * @since 1.0.0-SNAPSHOT
     */
    public static Component parse(final String string, final Map<String, Object> replacements) {
        List<TagResolver.Single> placeholders = replacements.entrySet().stream()
            .map(entry -> Placeholder.parsed(entry.getKey(), String.valueOf(entry.getValue())))
            .toList();
        return miniMessage.deserialize(string, TagResolver.resolver(placeholders));
    }

    /**
     * Parses the given string into a Component
     *
     * @param string    the string to parse
     * @param resolvers the resolvers to use to fill in placeholders
     * @return the Component
     * @since 1.0.0-SNAPSHOT
     */
    public static Component parse(final String string, final TagResolver... resolvers) {
        return miniMessage.deserialize(string, TagResolver.resolver(resolvers));
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
    public static PineappleComponent component(final String source) {
        return new PineappleComponent(source);
    }

}
