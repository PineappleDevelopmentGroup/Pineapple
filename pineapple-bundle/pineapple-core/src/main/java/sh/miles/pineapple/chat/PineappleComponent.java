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
    private BaseComponent parsed;
    private Component converted;

    PineappleComponent(@NotNull final String source) {
        this.source = source;
        this.parsed = null;
        this.converted = null;
    }

    /**
     * Converts this PineappleComponent into a {@link Component} and caches the result
     *
     * @return the component
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public Component convert() {
        if (this.converted == null) {
            this.converted = PineappleChat.convert(this.source);
        }

        return this.converted;
    }

    /**
     * Converts this PineappleComponent into a {@link Component} given the replacements
     *
     * @param replacements the replacements
     * @return the component
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public Component convert(@NotNull final Map<String, Object> replacements) {
        return PineappleChat.convert(this.source, replacements);
    }

    /**
     * Converts this PineappleComponent to a BaseComponent using the given replacements
     *
     * @param replacements the replacements
     * @return the BaseComponent
     * @since 1.0.0-SNAPSHOT
     * @deprecated use {@link #convert(Map)} )} instead
     */
    @Deprecated(forRemoval = true)
    public BaseComponent component(@NotNull final Map<String, Object> replacements) {
        return PineappleChat.parse(this.source, replacements);
    }

    /**
     * Converts this PineappleComponent to a BaseComponent
     *
     * @return the BaseComponent
     * @since 1.0.0-SNAPSHOT
     * @deprecated use {@link #convert()} instead
     */
    @Deprecated(forRemoval = true)
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
        return Objects.equals(this.source, that.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source);
    }
}
