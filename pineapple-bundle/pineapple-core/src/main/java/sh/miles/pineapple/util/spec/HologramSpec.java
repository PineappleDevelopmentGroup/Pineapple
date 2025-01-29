package sh.miles.pineapple.util.spec;

import org.bukkit.Location;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.chat.PineappleChat;
import sh.miles.pineapple.chat.PineappleComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Represents the basic specification for a hologram
 *
 * @param hologramText the hologram text
 * @param offset       the offset of the hologram
 * @since 1.0.0-SNAPSHOT
 */
public record HologramSpec(@NotNull PineappleComponent hologramText, @NotNull VectorSpec offset) {

    /**
     * Spawns a {@link TextDisplay} given the basic Hologram Specifications.
     *
     * @param location      the location to spawn the hologram at
     * @param configuration any extra configuration to be applied
     * @return the spawned text display
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public TextDisplay spawn(@NotNull final Location location, @NotNull final Consumer<TextDisplay> configuration) {
        return spawn(location, new HashMap<>(), configuration);
    }

    /**
     * Spawns a {@link TextDisplay} given the basic Hologram Specifications.
     *
     * @param location      the location to spawn the hologram at
     * @param replacements  the replacements to apply to the hologram text
     * @param configuration any extra configurations to be applied
     * @return the spawned text display
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public TextDisplay spawn(@NotNull final Location location, @NotNull final Map<String, Object> replacements, @NotNull final Consumer<TextDisplay> configuration) {
        return location.getWorld().spawn(this.offset.modify(location), TextDisplay.class, (display) -> {
            display.setText(PineappleChat.parseLegacy(hologramText.getSource(), replacements));
            configuration.accept(display);
        });
    }

}
