package sh.miles.pineapple.tiles.internal.util;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.tiles.api.Tile;
import sh.miles.pineapple.tiles.api.TileType;
import sh.miles.pineapple.tiles.api.TileTypeRegistry;
import sh.miles.pineapple.tiles.api.pos.ChunkPos;
import sh.miles.pineapple.tiles.api.pos.ChunkRelPos;
import sh.miles.pineapple.tiles.internal.ServerTileCache;

import java.util.Map;

/**
 * IO Utilities for reading and writing from chunks
 *
 * @since 1.0.0-SNAPSHOT
 */
@ApiStatus.Internal
public final class TileChunkIOUtils {

    private TileChunkIOUtils() {
        throw new UnsupportedOperationException("Can not instantiate utility class");
    }

    /**
     * Loads tiles from the given chunk
     *
     * @param cache    the cache to cache the tiles in
     * @param registry the TileType registry
     * @param chunk    the chunk to load from
     * @throws IllegalStateException thrown if there is no found TileType for the a given loaded tile
     * @since 1.0.0-SNAPSHOT
     */
    public static void loadTiles(@NotNull final ServerTileCache cache, @NotNull final TileTypeRegistry registry, @NotNull final Chunk chunk) throws IllegalStateException {
        final PersistentDataContainer container = chunk.getPersistentDataContainer().get(TileKeys.getTileContainerKey(), PersistentDataType.TAG_CONTAINER);
        assert container != null;
        for (final NamespacedKey key : container.getKeys()) {
            final ChunkRelPos chunkPos = new ChunkRelPos(Long.parseLong(key.getKey()));
            final PersistentDataContainer tileContainer = container.get(key, PersistentDataType.TAG_CONTAINER);
            final TileType<?> tileType = registry.getOrNull(NamespacedKey.fromString(tileContainer.get(TileKeys.getTileTypeKey(), PersistentDataType.STRING)));
            if (tileType == null) {
                throw new IllegalStateException("Unable to load tile at chunk position %s in chunk %s".formatted(chunkPos, chunk));
            }
            final Tile tile = tileType.createTile();
            try {
                tile.load(tileContainer);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            cache.getChunkCacheNaive(ChunkPos.fromChunk(chunk)).cache(chunkPos, tile);
        }
    }

    /**
     * Saves tiles to the given cache from the given chunk
     *
     * @param cache the cache
     * @param chunk the chunk
     * @since 1.0.0-SNAPSHOT
     */
    public static void saveTiles(@NotNull final ServerTileCache cache, @NotNull final Chunk chunk) {
        final var tiles = cache.evict(chunk);
        final PersistentDataContainer chunkContainer = chunk.getPersistentDataContainer();
        final PersistentDataContainer tilesContainer;
        if (chunkContainer.has(TileKeys.getTileContainerKey())) {
            tilesContainer = chunkContainer.get(TileKeys.getTileContainerKey(), PersistentDataType.TAG_CONTAINER);
        } else {
            tilesContainer = chunkContainer.getAdapterContext().newPersistentDataContainer();
        }
        final PersistentDataAdapterContext context = tilesContainer.getAdapterContext();
        for (final Map.Entry<ChunkRelPos, Tile> entry : tiles) {
            final PersistentDataContainer tileContainer = context.newPersistentDataContainer();
            tileContainer.set(TileKeys.getTileTypeKey(), PersistentDataType.STRING, entry.getValue().getTileType().getKey().toString());
            entry.getValue().save(tileContainer);
            tilesContainer.set(TileKeys.buildChunkRelPosKey(entry.getKey()), PersistentDataType.TAG_CONTAINER, tileContainer);
        }

        if (!tilesContainer.isEmpty()) {
            chunkContainer.set(TileKeys.getTileContainerKey(), PersistentDataType.TAG_CONTAINER, tilesContainer);
        }
    }

    /**
     * Deletes a tile from the given chunk and relative position
     *
     * @param cache the cache
     * @param chunk the chunk the relative position is in
     * @param pos   the relative position to delete the tile from
     * @param hard  whether or not to delete the tile even if it is not cached
     * @return true if the tile was successfully deleted
     */
    public static boolean deleteTile(@NotNull final ServerTileCache cache, @NotNull final Chunk chunk, @NotNull final ChunkRelPos pos, boolean hard) {
        final Tile tile = cache.evict(chunk, pos);
        if (tile == null && !hard) {
            return false;
        }

        final PersistentDataContainer chunkContainer = chunk.getPersistentDataContainer();
        if (!chunkContainer.has(TileKeys.getTileContainerKey())) {
            return false;
        }

        final PersistentDataContainer container = chunkContainer.get(TileKeys.getTileContainerKey(), PersistentDataType.TAG_CONTAINER);
        container.remove(TileKeys.buildChunkRelPosKey(pos));
        if (!container.getKeys().isEmpty()) {
            chunkContainer.set(TileKeys.getTileContainerKey(), PersistentDataType.TAG_CONTAINER, container);
            return true;
        }

        chunkContainer.remove(TileKeys.getTileContainerKey());
        return true;

    }

    /**
     * Deletes the tile from the given location
     *
     * @param cache    the cache
     * @param location the location to delete the tile from
     * @param hard     whether or not to delete the tile even if it is not cached
     * @return true if the tile was successfully deleted
     */
    public static boolean deleteTile(@NotNull final ServerTileCache cache, @NotNull final Location location, boolean hard) {
        return deleteTile(cache, location.getChunk(), ChunkRelPos.fromLocation(location), hard);
    }
}
