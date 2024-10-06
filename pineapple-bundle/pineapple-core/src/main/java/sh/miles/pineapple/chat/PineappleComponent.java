package sh.miles.pineapple.chat;


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
    private BaseComponent parsed;

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
    public BaseComponent component(@NotNull final Map<String, Object> replacements) {
        return PineappleChat.parse(this.source, replacements);
    }

    /**
     * Converts this PineappleComponent to a BaseComponent
     *
     * @return the BaseComponent
     * @since 1.0.0-SNAPSHOT
     */
    public BaseComponent component() {
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
