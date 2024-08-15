package sh.miles.pineapple.tiles.internal.util;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.tiles.api.pos.ChunkRelPos;

/**
 * Contains a list of tile keys important for identifying things
 *
 * @since 1.0.0-SNAPSHOT
 */
@ApiStatus.Internal
public final class TileKeys {

    public static final NamespacedKey TILE_CONTAINER_KEY = NamespacedKey.fromString("pineapple_tiles:tile_container");
    public static final NamespacedKey TILE_TYPE_KEY = NamespacedKey.fromString("pineapple_tiles:tile_type_key");

    private TileKeys() {
        throw new UnsupportedOperationException("Can not instantiate utility class");
    }

    /**
     * Builds a key from the given {@link ChunkRelPos}
     *
     * @param pos the position
     * @return the key
     *
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public static NamespacedKey buildChunkRelPosKey(@NotNull final ChunkRelPos pos) {
        return NamespacedKey.fromString("pineapple_tiles:" + pos.asLongString());
    }
}
