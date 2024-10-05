package sh.miles.pineapple.tiles.api;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Streams;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sh.miles.pineapple.collection.Pair;
import sh.miles.pineapple.tiles.api.pos.ChunkRelPos;
import sh.miles.pineapple.tiles.internal.ChunkTileCache;
import sh.miles.pineapple.tiles.internal.ServerTileCache;
import sh.miles.pineapple.tiles.internal.listener.TileChunkIOListener;
import sh.miles.pineapple.tiles.internal.listener.TileGeneralInteractionListener;
import sh.miles.pineapple.tiles.internal.util.TileChunkIOUtils;
import sh.miles.pineapple.tiles.internal.util.TileKeys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Main access point for tiles
 *
 * @since 1.0.0-SNAPSHOT
 */
public final class Tiles {

    private static Tiles instance;

    private final Plugin plugin;
    private final ServerTileCache cache;
    private final TileTypeRegistry registry;

    private Tiles(@NotNull final Plugin plugin) {
        this.plugin = plugin;
        this.cache = new ServerTileCache(plugin);
        this.registry = new TileTypeRegistry();
        plugin.getServer().getPluginManager().registerEvents(new TileChunkIOListener(cache, registry), plugin);
        plugin.getServer().getPluginManager().registerEvents(new TileGeneralInteractionListener(cache, registry), plugin);
    }

    /**
     * Registers a {@link TileType} to the internal registry
     *
     * @param tileType the {@link TileType} to register
     * @throws IllegalArgumentException thrown if the given {@link TileType} is null
     */
    public void registerTileType(@NotNull final TileType<?> tileType) throws IllegalArgumentException {
        Preconditions.checkArgument(tileType != null, "The given tile must not be null");
        registry.register(tileType);
    }

    /**
     * Attempts to get a tile at the given location
     *
     * @param location the location
     * @return the tile if one exists at the location, or null
     * @throws IllegalArgumentException thrown if location is null
     * @since 1.0.0-SNAPSHOT
     */
    @Nullable
    public Tile getTile(@NotNull final Location location) {
        return getTile(location, (__) -> true);
    }

    /**
     * Gets all loaded tiles
     *
     * @return a list of all loaded tiles
     */
    @NotNull
    public List<Pair<ChunkRelPos, Tile>> getAllLoadedTiles() {
        return getAllLoadedTiles((tile) -> true);
    }

    /**
     * Gets all currently loaded tiles within the server
     *
     * @param filter filters the result
     * @return the list of all loaded tiles
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public List<Pair<ChunkRelPos, Tile>> getAllLoadedTiles(Predicate<Tile> filter) {
        return Streams.stream(this.cache.iterator()).filter((entry) -> filter.apply(entry.getValue())).map((entry) -> Pair.of(entry.getKey(), entry.getValue())).toList();
    }

    /**
     * Gets a map of tiles in the given chunk
     *
     * @param chunk the chunk to get tiles from
     * @return the tiles found
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public Map<ChunkRelPos, Tile> getTiles(@NotNull final Chunk chunk) {
        return getTiles(chunk, (__) -> true);
    }

    /**
     * Attempts to get a tile at the given location
     *
     * @param location the location
     * @param filter   the filter that must be true to get the tile at the location
     * @return the tile if one existed and passed the filter
     * @throws IllegalStateException if the location is null
     * @since 1.0.0-SNAPSHOT
     */
    @Nullable
    public Tile getTile(@NotNull final Location location, @NotNull final Predicate<Tile> filter) {
        final Tile tile = cache.get(location);
        if (tile == null || !filter.apply(tile)) {
            return null;
        }

        return tile;
    }

    /**
     * Gets a map of tiles in the given chunk
     *
     * @param chunk  the chunk to get tiles from
     * @param filter the filter that must be true to get certain tiles in this chunk
     * @return the tiles found
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public Map<ChunkRelPos, Tile> getTiles(@NotNull final Chunk chunk, @NotNull final Predicate<Tile> filter) {
        final ChunkTileCache chunkCache = cache.getChunkCache(chunk);
        if (chunkCache == null) {
            return Map.of();
        }

        return chunkCache.stream().filter((entry) -> filter.apply(entry.getValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Places a tile at the given position
     *
     * @param location the location to place the tile at
     * @param tile     the tile to place
     * @since 1.0.0-SNAPSHOT
     */
    public void placeTile(@NotNull final Location location, @NotNull final Tile tile) {
        this.cache.cache(location, tile);
    }

    /**
     * Attempts to delete a tile at the given location
     *
     * @param location the location
     * @param filter   the filter that must be true for the tile to be deleted
     * @return the tile if one existed and was deleted, or null
     * @throws IllegalArgumentException thrown if the location is null
     * @since 1.0.0-SNAPSHOT
     */
    @Nullable
    public Tile deleteTile(@NotNull final Location location, @NotNull final Predicate<Tile> filter) {
        final Tile tile = cache.evict(location);
        if (tile == null || !filter.apply(tile)) {
            return null;
        }
        TileChunkIOUtils.deleteTile(cache, location, false);
        return tile;
    }

    /**
     * Attempts to delete a tile at the given location
     *
     * @param location the location
     * @param hard     whether or not to hard delete the tile even if it isn't loaded, in the case where a tile isn't
     *                 loaded and hard is selected, the deleted tile is not ever loaded.
     * @return the deleted tile or null, this method returns null so long as the tile was never cached.
     * @throws IllegalArgumentException thrown if the location is null
     * @since 1.0.0-SNAPSHOT
     */
    public Tile deleteTile(@NotNull final Location location, boolean hard) {
        final Tile tile = cache.evict(location);
        if (tile == null && !hard) {
            return null;
        }

        TileChunkIOUtils.deleteTile(cache, location, true);
        return tile;
    }

    /**
     * Attempts to delete all tiles in the given chunk
     *
     * @param chunk  the chunk to delete
     * @param filter the filter that must be true for the tiles to be deleted
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public Map<ChunkRelPos, Tile> deleteTiles(@NotNull final Chunk chunk, @NotNull final Predicate<Tile> filter) {
        final ChunkTileCache chunkCache = cache.getChunkCache(chunk);
        if (chunkCache == null) {
            return Map.of();
        }

        List<ChunkRelPos> deathMark = new ArrayList<>();
        for (final Map.Entry<ChunkRelPos, Tile> entry : chunkCache) {
            if (filter.apply(entry.getValue())) {
                deathMark.add(entry.getKey());
            }
        }

        final Map<ChunkRelPos, Tile> deleted = new HashMap<>();
        final PersistentDataContainer container = chunk.getPersistentDataContainer();
        for (final ChunkRelPos chunkRelPos : deathMark) {
            final Tile tile = chunkCache.evict(chunkRelPos);
            assert tile != null;
            TileChunkIOUtils.deleteTile(cache, chunk, chunkRelPos, false);
            deleted.put(chunkRelPos, tile);
        }

        return deleted;
    }

    /**
     * Loads all spawn chunks
     *
     * @since 1.0.0-SNAPSHOT
     */
    public void loadSpawnChunks() {
        for (final World world : Bukkit.getWorlds()) {
            for (final Chunk loadedChunk : world.getLoadedChunks()) {
                if (!loadedChunk.getPersistentDataContainer().has(TileKeys.getTileContainerKey())) {
                    continue;
                }
                TileChunkIOUtils.loadTiles(this.cache, this.registry, loadedChunk);
            }
        }
    }

    /**
     * Creates a namespaced key with the given key name
     *
     * @param name the key name
     * @return the created namespaced key
     */
    @NotNull
    public NamespacedKey makeKey(String name) {
        return new NamespacedKey(plugin, name);
    }

    /**
     * Sets up tiles
     *
     * @param plugin the plugin to use for setup
     * @throws IllegalArgumentException thrown if plugin is null
     * @throws IllegalStateException    if there is already an active instance of Tiles
     * @since 1.0.0-SNAPSHOT
     */
    public static void setup(@NotNull final Plugin plugin) throws IllegalArgumentException, IllegalStateException {
        Preconditions.checkArgument(plugin != null, "The given plugin must not be null!");
        Preconditions.checkState(instance == null, "There is already an instance of Tiles, Tiles can not be set up again");
        instance = new Tiles(plugin);
    }

    /**
     * Cleans up tiles
     *
     * @throws IllegalStateException thrown if tiles is not setup with {@link Tiles#setup(Plugin)}
     * @since 1.0.0-SNAPSHOT
     */
    public static void cleanup() throws IllegalStateException {
        Preconditions.checkState(instance != null, "There is no active instance of Tiles, Tiles can not be shut down");
        instance.cache.evictAndSaveAll();
    }

    /**
     * Gets the instance of tiles
     *
     * @return the tiles instance
     * @throws IllegalStateException thrown if tiles is not setup with {@link Tiles#setup(Plugin)}
     * @since 1.0.0-SNAPSHOT
     */
    public static Tiles getInstance() throws IllegalStateException {
        if (instance == null) {
            throw new IllegalStateException("Can not get instance of tiles if it has not been set up run Tiles#setup first");
        }
        return instance;
    }


}
