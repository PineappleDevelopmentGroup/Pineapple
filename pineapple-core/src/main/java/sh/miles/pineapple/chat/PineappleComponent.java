package sh.miles.pineapple.chat;


import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

/**
 * A PineappleComponent wraps a Component and provides extra useful functionality
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
     * Converts this PineappleComponent to a Component using the given replacements
     *
     * @param replacements the replacements
     * @return the Component
     * @since 1.0.0-SNAPSHOT
     */
    public Component component(@NotNull final Map<String, Object> replacements) {
        return PineappleChat.parse(this.source, replacements);
    }

    /**
     * Converts this PineappleComponent to a Component
     *
     * @return the Component
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PineappleComponent that = (PineappleComponent) o;
        return Objects.equals(source, that.source) && Objects.equals(parsed, that.parsed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source);
    }
}
