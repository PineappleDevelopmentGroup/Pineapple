package sh.miles.pineapple.chat;


import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

/**
 * A PineappleComponent wraps a BaseComponent and provides extra useful functionality
 *
 * @since 1.0.0-SNAPSHOT
 */
public class PineappleComponent {

    private final String source;
    private Component parsed;

    PineappleComponent(@NotNull final String source) {
        this.source = source;
        this.parsed = null;
    }

    /**
     * Converts this PineappleComponent to a BaseComponent using the given replacements
     *
     * @param replacements the replacements
     * @return the BaseComponent
     * @since 1.0.0-SNAPSHOT
     */
    public Component component(@NotNull final Map<String, Object> replacements) {
        return PineappleChat.parse(this.source, replacements);
    }

    /**
     * Converts this PineappleComponent to a BaseComponent
     *
     * @return the BaseComponent
     * @since 1.0.0-SNAPSHOT
     */
    public Component component() {
        if (this.parsed != null) {
            return parsed;
        }

        this.parsed = PineappleChat.parse(this.source);
        return this.parsed;
    }

    public String getSource() {
        return source;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final PineappleComponent that)) return false;
        return Objects.equals(source, that.source);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(source);
    }
}
