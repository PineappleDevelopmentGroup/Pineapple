package sh.miles.pineapple.tiles.internal;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sh.miles.pineapple.tiles.api.Tile;
import sh.miles.pineapple.tiles.api.pos.ChunkRelPos;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * Represents a cache within a chunk used for grabbing loaded tiles
 *
 * @since 1.0.0-SNAPSHOT
 */
@ApiStatus.Internal
public final class ChunkTileCache implements Iterable<Map.Entry<ChunkRelPos, Tile>> {
    private final Map<ChunkRelPos, Tile> cache = new ConcurrentHashMap<>();

    /**
     * Caches the given pos and tile into the map
     *
     * @param chunkPos the chunk position
     * @param tile     the tile
     * @throws IllegalStateException thrown if a tile is already cached at the given location
     * @since 1.0.0-SNAPSHOT
     */
    public void cache(@NotNull final ChunkRelPos chunkPos, @NotNull final Tile tile) throws IllegalStateException {
        if (cache.containsKey(chunkPos)) {
            throw new IllegalStateException("Two tiles can not exist at the same location. You must evict a tile before caching another one at the same location");
        }
        cache.put(chunkPos, tile);
    }

    /**
     * Gets a possible tile from the given relative position
     *
     * @param chunkPos the relative chunk position
     * @return the given tile if it exists, or null
     * @since 1.0.0-SNAPSHOT
     */
    @Nullable
    public Tile get(@NotNull final ChunkRelPos chunkPos) {
        return cache.get(chunkPos);
    }

    /**
     * Evicts a possible tile from the given relative position
     *
     * @param chunkPos the relative chunk position
     * @return the given tile if it exists, or null
     * @since 1.0.0-SNAPSHOT
     */
    @Nullable
    public Tile evict(@NotNull final ChunkRelPos chunkPos) {
        return cache.remove(chunkPos);
    }

    /**
     * @return whether or not this cache is empty
     * @since 1.0.0-SNAPSHOT
     */
    public boolean isEmpty() {
        return cache.isEmpty();
    }

    /**
     * Streams
     *
     * @return the stream
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public Stream<Map.Entry<ChunkRelPos, Tile>> stream() {
        return cache.entrySet().stream();
    }

    @NotNull
    @Override
    public Iterator<Map.Entry<ChunkRelPos, Tile>> iterator() {
        return cache.entrySet().iterator();
    }
}
