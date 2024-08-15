package sh.miles.pineapple.tiles.api;

import com.google.common.base.Preconditions;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sh.miles.pineapple.tiles.internal.ChunkTileCache;
import sh.miles.pineapple.tiles.internal.ServerTileCache;
import sh.miles.pineapple.tiles.internal.listener.TileChunkIOListener;
import sh.miles.pineapple.tiles.internal.listener.TileGeneralInteractionListener;

import java.util.List;
import java.util.Map;

/**
 * Main access point for tiles
 *
 * @since 1.0.0-SNAPSHOT
 */
public final class Tiles {

    private static Tiles instance;

    private final ServerTileCache cache;
    private final TileTypeRegistry registry;

    private Tiles(@NotNull final Plugin plugin) {
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
        return cache.get(location);
    }

    /**
     * Gets a list of tiles in the given chunk
     *
     * @param chunk the chunk to get tiles from
     * @return the tiles found
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public List<Tile> getTiles(@NotNull final Chunk chunk) {
        final ChunkTileCache chunkCache = cache.getChunkCache(chunk);
        if (chunkCache == null) {
            return List.of();
        }

        return chunkCache.stream().map(Map.Entry::getValue).toList();

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
