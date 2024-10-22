package sh.miles.pineapple.tiles.internal.util;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.tiles.api.Tiles;
import sh.miles.pineapple.tiles.api.pos.ChunkRelPos;

/**
 * Contains a list of tile keys important for identifying things
 *
 * @since 1.0.0-SNAPSHOT
 */
@ApiStatus.Internal
public final class TileKeys {

    private static NamespacedKey tileContainerKey = null;
    private static NamespacedKey tileTypeKey = null;

    private TileKeys() {
        throw new UnsupportedOperationException("Can not instantiate utility class");
    }

    /**
     * Gets the tile container key
     *
     * @return the tile container key
     */
    @NotNull
    public static NamespacedKey getTileContainerKey() {
        if (tileContainerKey == null) {
            tileContainerKey = Tiles.getInstance().makeKey("tile_container");
        }

        return tileContainerKey;
    }

    /**
     * Gets the tile type key
     *
     * @return the tile type key
     */
    public static NamespacedKey getTileTypeKey() {
        if (tileTypeKey == null) {
            tileTypeKey = Tiles.getInstance().makeKey("tile_type");
        }

        return tileTypeKey;
    }

    /**
     * Builds a key from the given {@link ChunkRelPos}
     *
     * @param pos the position
     * @return the key
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public static NamespacedKey buildChunkRelPosKey(@NotNull final ChunkRelPos pos) {
        return NamespacedKey.fromString("pineapple_tiles:" + pos.asLongString());
    }
}
