package sh.miles.pineapple.tiles.internal.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.tiles.api.TileTypeRegistry;
import sh.miles.pineapple.tiles.internal.ServerTileCache;
import sh.miles.pineapple.tiles.internal.util.TileChunkIOUtils;
import sh.miles.pineapple.tiles.internal.util.TileKeys;

/**
 * The listeners related to Chunk IO
 *
 * @since 1.0.0-SNAPSHOT
 */
@ApiStatus.Internal
public final class TileChunkIOListener implements Listener {

    @NotNull
    private final ServerTileCache cache;
    @NotNull
    private final TileTypeRegistry registry;

    /**
     * Creates a new chunk io listener
     *
     * @param cache    the cache
     * @param registry the registry
     * @since 1.0.0-SNAPSHOT
     */
    public TileChunkIOListener(@NotNull final ServerTileCache cache, @NotNull final TileTypeRegistry registry) {
        this.cache = cache;
        this.registry = registry;
    }

    /**
     * _
     *
     * @param event _
     */
    @EventHandler
    public void onChunkLoad(@NotNull final ChunkLoadEvent event) {
        if (!event.getChunk().getPersistentDataContainer().has(TileKeys.TILE_CONTAINER_KEY)) {
            return;
        }
        TileChunkIOUtils.loadTiles(cache, registry, event.getChunk());
    }

    /**
     * _
     *
     * @param event _
     */
    @EventHandler
    public void onChunkUnload(@NotNull final ChunkUnloadEvent event) {
        TileChunkIOUtils.saveTiles(cache, event.getChunk());
    }
}
