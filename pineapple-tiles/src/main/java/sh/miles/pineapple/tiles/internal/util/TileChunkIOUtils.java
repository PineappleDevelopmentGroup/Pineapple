package sh.miles.pineapple.tiles.internal.util;

import org.bukkit.Chunk;
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
        final PersistentDataContainer container = chunk.getPersistentDataContainer().get(TileKeys.TILE_CONTAINER_KEY, PersistentDataType.TAG_CONTAINER);
        assert container != null;
        for (final NamespacedKey key : container.getKeys()) {
            final ChunkRelPos chunkPos = new ChunkRelPos(Long.parseLong(key.getKey()));
            final PersistentDataContainer tileContainer = container.get(key, PersistentDataType.TAG_CONTAINER);
            final TileType<?> tileType = registry.getOrNull(NamespacedKey.fromString(tileContainer.get(TileKeys.TILE_TYPE_KEY, PersistentDataType.STRING)));
            if (tileType == null) {
                throw new IllegalStateException("Unable to load tile at chunk position %s in chunk %s".formatted(chunkPos, chunk));
            }
            final Tile tile = tileType.createTile();
            tile.load(tileContainer);
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
        final PersistentDataContainer container;
        if (chunkContainer.has(TileKeys.TILE_CONTAINER_KEY)) {
            container = chunkContainer.get(TileKeys.TILE_CONTAINER_KEY, PersistentDataType.TAG_CONTAINER);
        } else {
            container = chunkContainer.getAdapterContext().newPersistentDataContainer();
        }
        final PersistentDataAdapterContext context = container.getAdapterContext();
        for (final Map.Entry<ChunkRelPos, Tile> entry : tiles) {
            final PersistentDataContainer tileContainer = context.newPersistentDataContainer();
            tileContainer.set(TileKeys.TILE_TYPE_KEY, PersistentDataType.STRING, entry.getValue().getTileType().getKey().toString());
            entry.getValue().save(tileContainer);
            container.set(TileKeys.buildChunkRelPosKey(entry.getKey()), PersistentDataType.TAG_CONTAINER, tileContainer);
        }

        if (!container.isEmpty()) {
            chunkContainer.set(TileKeys.TILE_CONTAINER_KEY, PersistentDataType.TAG_CONTAINER, container);
        }
    }
}
