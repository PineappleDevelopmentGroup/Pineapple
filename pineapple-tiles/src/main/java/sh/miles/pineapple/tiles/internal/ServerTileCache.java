package sh.miles.pineapple.tiles.internal;

import com.google.common.collect.Streams;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sh.miles.pineapple.tiles.api.Tile;
import sh.miles.pineapple.tiles.api.pos.ChunkPos;
import sh.miles.pineapple.tiles.api.pos.ChunkRelPos;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a cache of tiles for the entire server
 *
 * @since 1.0.0-SNAPSHOT
 */
@ApiStatus.Internal
public class ServerTileCache implements Iterable<Map.Entry<ChunkRelPos, Tile>> {
    private final Map<ChunkPos, ChunkTileCache> cache = new ConcurrentHashMap<>();
    private final BukkitTask task;

    /**
     * Creates a new instance of ServerTileCache
     *
     * @param plugin the plugin to be used for task creation
     * @since 1.0.0-SNAPSHOT
     */
    public ServerTileCache(@NotNull final Plugin plugin) {
        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (final var serverEntry : cache.entrySet()) {
                for (final Map.Entry<ChunkRelPos, Tile> chunkRelPosTileEntry : serverEntry.getValue()) {
                    final var tile = chunkRelPosTileEntry.getValue();
                    final var tileType = tile.getTileType();
                    if (tileType.isTicking) {
                        tileType.onTickLoop(tile);
                    }
                }
            }
        }, 0L, 1L);
    }

    /**
     * Caches the given tile at the given location
     *
     * @param location the location to cache at
     * @param tile     the tile to cache
     * @throws IllegalStateException thrown if a tile is already cached at the given location
     * @since 1.0.0-SNAPSHOT
     */
    public void cache(@NotNull final Location location, @NotNull final Tile tile) throws IllegalStateException {
        final ChunkPos position = ChunkPos.fromChunk(location.getChunk());
        final ChunkTileCache chunkCache = cache.computeIfAbsent(position, (k) -> new ChunkTileCache());
        chunkCache.cache(ChunkRelPos.fromLocation(location), tile);
    }

    /**
     * Gets a tile from the given location in the cache
     *
     * @param location the location to get the tile at
     * @return the tile at that location, or null
     */
    @Nullable
    public Tile get(@NotNull final Location location) {
        final ChunkPos position = ChunkPos.fromChunk(location.getChunk());
        final ChunkTileCache chunkCache = cache.get(position);
        return chunkCache != null ? chunkCache.get(ChunkRelPos.fromLocation(location)) : null;
    }

    /**
     * Using this method without proper intentions can cause memory leaks.
     * <p>
     * This method runs computeIfAbsent on the retrieval of a map value. If used in completely proper situations this
     * method is useful, however using this method outside internal use cases can cause memory leaks or worse.
     *
     * @param chunkPos the chunk position to use this illegal method at
     * @return ChunkTileCache
     * @since 1.0.0-SNAPSHOT
     * @deprecated DO NOT USE THIS METHOD
     */
    @NotNull
    @Deprecated
    public ChunkTileCache getChunkCacheNaive(@NotNull final ChunkPos chunkPos) {
        return cache.computeIfAbsent(chunkPos, (k) -> new ChunkTileCache());
    }

    /**
     * Attempts to find a chunk cache for the given chunk
     *
     * @param chunk the chunk to get the cache for
     * @return the chunk tile cache or null
     */
    @Nullable
    public ChunkTileCache getChunkCache(@NotNull final Chunk chunk) {
        return cache.get(ChunkPos.fromChunk(chunk));
    }

    /**
     * Evicts the tile from the given location in the cache
     *
     * @param location the location to get the tile at
     * @return the tile at the location or null
     */
    @Nullable
    public Tile evict(@NotNull final Location location) {
        final ChunkPos position = ChunkPos.fromChunk(location.getChunk());
        final ChunkTileCache chunkCache = cache.get(position);
        final Tile tile = chunkCache != null ? chunkCache.evict(ChunkRelPos.fromLocation(location)) : null;
        if (chunkCache.isEmpty()) {
            cache.remove(position);
        }
        return tile;
    }

    /**
     * Evicts an entire chunk from this cache
     *
     * @param chunk the chunk to evict
     * @return all entries in the cache
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public List<Map.Entry<ChunkRelPos, Tile>> evict(@NotNull final Chunk chunk) {
        final ChunkTileCache chunkCache = cache.remove(ChunkPos.fromChunk(chunk));
        return chunkCache != null ? Streams.stream(chunkCache.iterator()).toList() : List.of();
    }

    /**
     * Evicts all entries from this cache and saves them
     *
     * @since 1.0.0-SNAPSHOT
     */
    public void evictAndSaveAll() {
        // TODO: Implement
    }

    /**
     * @return if this cache is empty
     * @since 1.0.0-SNAPSHOT
     */
    public boolean isEmpty() {
        return this.cache.isEmpty();
    }

    @NotNull
    @Override
    public Iterator<Map.Entry<ChunkRelPos, Tile>> iterator() {
        return cache.entrySet().stream().flatMap((it) -> it.getValue().stream()).iterator();
    }
}
