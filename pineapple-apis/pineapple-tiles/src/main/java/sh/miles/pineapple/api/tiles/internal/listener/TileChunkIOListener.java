package sh.miles.pineapple.api.tiles.internal.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;
import sh.miles.pineapple.api.tiles.api.TileTypeRegistry;
import sh.miles.pineapple.api.tiles.internal.ServerTileCache;
import sh.miles.pineapple.api.tiles.internal.util.TileChunkIOUtils;
import sh.miles.pineapple.api.tiles.internal.util.TileKeys;

/**
 * The listeners related to Chunk IO
 *
 * @since 1.0.0-SNAPSHOT
 */
@NullMarked
@ApiStatus.Internal
public final class TileChunkIOListener implements Listener {

    private final ServerTileCache cache;
    private final TileTypeRegistry registry;

    /**
     * Creates a new chunk io listener
     *
     * @param cache    the cache
     * @param registry the registry
     * @since 1.0.0-SNAPSHOT
     */
    public TileChunkIOListener(final ServerTileCache cache, final TileTypeRegistry registry) {
        this.cache = cache;
        this.registry = registry;
    }

    /**
     * _
     *
     * @param event _
     */
    @EventHandler
    public void onChunkLoad(final ChunkLoadEvent event) {
        if (!event.getChunk().getPersistentDataContainer().has(TileKeys.getTileContainerKey())) {
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
    public void onChunkUnload(final ChunkUnloadEvent event) {
        TileChunkIOUtils.saveTiles(cache, event.getChunk());
    }
}
