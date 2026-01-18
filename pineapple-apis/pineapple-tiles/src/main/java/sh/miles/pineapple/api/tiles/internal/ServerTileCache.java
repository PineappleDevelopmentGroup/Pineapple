package sh.miles.pineapple.api.tiles.internal;

import com.google.common.collect.Streams;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import sh.miles.pineapple.api.tiles.api.Tile;
import sh.miles.pineapple.api.tiles.api.pos.ChunkPos;
import sh.miles.pineapple.api.tiles.api.pos.ChunkRelPos;
import sh.miles.pineapple.api.tiles.internal.util.TileChunkIOUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a cache of tiles for the entire server
 *
 * @since 1.0.0-SNAPSHOT
 */
@NullMarked
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
    public ServerTileCache(final Plugin plugin) {
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
    public void cache(final Location location, final Tile tile) throws IllegalStateException {
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
    public Tile get(final Location location) {
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
    @Deprecated
    public ChunkTileCache getChunkCacheNaive(final ChunkPos chunkPos) {
        return cache.computeIfAbsent(chunkPos, (k) -> new ChunkTileCache());
    }

    /**
     * Attempts to find a chunk cache for the given chunk
     *
     * @param chunk the chunk to get the cache for
     * @return the chunk tile cache or null
     */
    @Nullable
    public ChunkTileCache getChunkCache(final Chunk chunk) {
        return cache.get(ChunkPos.fromChunk(chunk));
    }

    /**
     * Evicts the tile from the given location in the cache
     *
     * @param location the location to get the tile at
     * @return the tile at the location or null
     */
    @Nullable
    public Tile evict(final Location location) {
        return evict(location.getChunk(), ChunkRelPos.fromLocation(location));
    }

    /**
     * Evicts a tile from the given chunk and position
     *
     * @param chunk  the chunk to remove a  tile from
     * @param relPos the relative position to remove the tile from
     * @return the removed tile, otherwise null
     */
    @Nullable
    public Tile evict(final Chunk chunk, final ChunkRelPos relPos) {
        final ChunkPos position = ChunkPos.fromChunk(chunk);
        final ChunkTileCache chunkCache = cache.get(position);
        if (chunkCache == null) {
            return null;
        }
        final Tile tile = chunkCache != null ? chunkCache.evict(relPos) : null;
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
    public List<Map.Entry<ChunkRelPos, Tile>> evict(final Chunk chunk) {
        final ChunkTileCache chunkCache = cache.remove(ChunkPos.fromChunk(chunk));
        return chunkCache != null ? Streams.stream(chunkCache.iterator()).toList() : List.of();
    }

    /**
     * Evicts all entries from this cache and saves them
     *
     * @since 1.0.0-SNAPSHOT
     */
    public void evictAndSaveAll() {
        final List<Chunk> chunks = this.cache.keySet().stream().map(ChunkPos::toChunk).toList();
        for (final Chunk chunk : chunks) {
            TileChunkIOUtils.saveTiles(this, chunk);
        }

    }

    /**
     * @return if this cache is empty
     * @since 1.0.0-SNAPSHOT
     */
    public boolean isEmpty() {
        return this.cache.isEmpty();
    }

    @Override
    public Iterator<Map.Entry<ChunkRelPos, Tile>> iterator() {
        return cache.entrySet().stream().flatMap((it) -> it.getValue().stream()).iterator();
    }
}
