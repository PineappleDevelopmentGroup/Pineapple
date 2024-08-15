package sh.miles.pineapple.tiles.api;

import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * Represents a tile within a world. Tile store state that can be read and saved.
 * <p>
 * Tile objects allow for the illusion of the addition of block entities within the game. They behave similarly and
 * store to the actual world file instead of needing to be managed by external flat files or database.
 *
 * @since 1.0.0-SNAPSHOT
 */
public interface Tile {

    /**
     * Saves this tile to the given persistent data container
     *
     * @param container     the container to save to
     * @param excludeFields the excluded fields set, or null if no excluded fields
     * @since 1.0.0-SNAPSHOT
     */
    void save(@NotNull PersistentDataContainer container, @Nullable final Set<String> excludeFields);

    /**
     * Saves this tile to the given persistent data container
     *
     * @param container the container to save to
     * @since 1.0.0-SNAPSHOT
     */
    default void save(@NotNull final PersistentDataContainer container) {
        save(container, null);
    }

    /**
     * Loads this tile with information from the given container
     *
     * @param container the container to load this tile with
     * @since 1.0.0-SNAPSHOT
     */
    void load(@NotNull final PersistentDataContainer container);

    /**
     * Gets the TileType associated with this Tile
     *
     * @return the tile type
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    TileType<?> getTileType();
}
