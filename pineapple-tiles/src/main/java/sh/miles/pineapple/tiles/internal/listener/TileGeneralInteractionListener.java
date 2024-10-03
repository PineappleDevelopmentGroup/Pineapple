package sh.miles.pineapple.tiles.internal.listener;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.PineappleLib;
import sh.miles.pineapple.collection.Pair;
import sh.miles.pineapple.tiles.api.Tile;
import sh.miles.pineapple.tiles.api.TileType;
import sh.miles.pineapple.tiles.api.TileTypeRegistry;
import sh.miles.pineapple.tiles.internal.ServerTileCache;
import sh.miles.pineapple.tiles.internal.util.TileChunkIOUtils;
import sh.miles.pineapple.tiles.internal.util.TileKeys;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

/**
 * The listeners related to general tile interaction
 *
 * @since 1.0.0-SNAPSHOT
 */
@ApiStatus.Internal
public final class TileGeneralInteractionListener implements Listener {

    @NotNull
    private final ServerTileCache cache;
    @NotNull
    private final TileTypeRegistry registry;
    private final Cache<Location, Tile> broken = CacheBuilder.newBuilder().expireAfterWrite(Duration.ofMinutes(1)).build();

    /**
     * Creates a new tile interaction listener
     *
     * @param cache    the cache
     * @param registry the registry
     * @since 1.0.0-SNAPSHOT
     */
    public TileGeneralInteractionListener(@NotNull final ServerTileCache cache, @NotNull final TileTypeRegistry registry) {
        this.cache = cache;
        this.registry = registry;
    }

    /**
     * _
     *
     * @param event _
     */
    @EventHandler
    public void onBlockBreak(@NotNull final BlockBreakEvent event) {
        final Tile tile = cache.get(event.getBlock().getLocation());
        if (tile == null) {
            return;
        }

        tile.getTileType().onBlockBreakEvent(event, tile);
        if (!event.isCancelled()) {
            final Location location = event.getBlock().getLocation();
            broken.put(location, tile);
            TileChunkIOUtils.deleteTile(cache, location, false);
        }
    }

    /**
     * _
     *
     * @param event _
     */
    @EventHandler
    public void onBlockPlace(@NotNull final BlockPlaceEvent event) {
        final PersistentDataContainer container = event.getItemInHand().getItemMeta().getPersistentDataContainer();
        if (!container.has(TileKeys.getTileTypeKey())) {
            return;
        }
        final String tileTypeKeyRaw = container.get(TileKeys.getTileTypeKey(), PersistentDataType.STRING);
        final NamespacedKey tileTypeKey = NamespacedKey.fromString(tileTypeKeyRaw);
        final TileType<?> tileType = registry.getOrNull(tileTypeKey);
        if (tileType == null) {
            throw new IllegalStateException("can not determine the tileType of the placed item found tag with type %s".formatted(tileTypeKeyRaw));
        }

        final Tile tile = tileType.createTile(event.getItemInHand());
        tileType.onBlockPlaceEvent(event, tile);
        if (!event.isCancelled()) {
            cache.cache(event.getBlock().getLocation(), tile);
        }
    }

    /**
     * _
     *
     * @param event _
     */
    @EventHandler
    public void onBlockDrop(@NotNull final BlockDropItemEvent event) {
        final Tile dropping = broken.getIfPresent(event.getBlock().getLocation());
        if (dropping != null) {
            dropping.getTileType().onBlockDropItemEvent(event, dropping);
            broken.invalidate(event.getBlock().getLocation());
        }
    }

    /**
     * _
     *
     * @param event _
     */
    @EventHandler
    public void onBlockInteract(@NotNull final PlayerInteractEvent event) {
        if (!event.hasBlock()) {
            return;
        }
        final Tile tile = cache.get(event.getClickedBlock().getLocation());
        if (tile != null) {
            tile.getTileType().onPlayerInteractEvent(event, tile);
        }
    }

    /**
     * _
     *
     * @param event _
     */
    @EventHandler
    public void onBlockExplode(@NotNull final BlockExplodeEvent event) {
        final List<Pair<Tile, Block>> explodedTiles = event.blockList().stream()
                .map((block) -> Pair.of(cache.get(block.getLocation()), block))
                .filter((pair) -> pair.left() != null)
                .toList();
        if (explodedTiles.isEmpty()) {
            return;
        }

        Tile tile;
        Block block;
        for (final Pair<Tile, Block> explodedTile : explodedTiles) {
            tile = explodedTile.left();
            block = explodedTile.right();

            tile.getTileType().onBlockExplodeEvent(event, tile, block);
            if (event.isCancelled() || !event.blockList().contains(block)) {
                return;
            }

            broken.put(block.getLocation(), tile);
        }
    }

    @EventHandler
    public void onEntityExplode(@NotNull final EntityExplodeEvent event) {
        final List<Pair<Tile, Block>> explodedTiles = event.blockList().stream()
                .map((block) -> Pair.of(cache.get(block.getLocation()), block))
                .filter((pair) -> pair.left() != null)
                .toList();
        if (explodedTiles.isEmpty()) {
            return;
        }

        Tile tile;
        Block block;
        for (final Pair<Tile, Block> explodedTile : explodedTiles) {
            tile = explodedTile.left();
            block = explodedTile.right();

            tile.getTileType().onEntityExplodeEvent(event, tile, block);
            if (event.isCancelled() || !event.blockList().contains(block)) {
                return;
            }

            broken.put(block.getLocation(), tile);
        }
    }

}
