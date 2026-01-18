package sh.miles.pineapple.api.tiles.api;

import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import sh.miles.pineapple.collection.registry.RegistryKey;
import sh.miles.pineapple.item.ItemBuilder;
import sh.miles.pineapple.api.tiles.internal.util.TileKeys;

/**
 * Represents a type of tile within the world.
 * <p>
 * Every type of tile within the world has behavior this class can be used to define such behaviors such as ticking,
 * placing picking up and more.
 *
 * @param <T> the type of tile this tile type represents behavior for
 * @since 1.0.0-SNAPSHOT
 */
@NullMarked
public abstract class TileType<T extends Tile> implements RegistryKey<NamespacedKey> {

    public final boolean isTicking;

    protected TileType(final boolean isTicking) {
        this.isTicking = isTicking;
    }

    /**
     * Wrapper method used for casting for internal use only
     *
     * @param event event
     * @param tile  tile
     * @since 1.0.0-SNAPSHOT
     */
    @ApiStatus.Internal
    public final void onBlockDropItemEvent(final BlockDropItemEvent event, final Tile tile) {
        onDrop(event, (T) tile);
    }

    /**
     * Wrapper method used for casting for internal use only
     *
     * @param event     the event
     * @param tile      the tile
     * @param tileBlock the block the tile is
     * @since 1.0.0-SNAPSHOT
     */
    @ApiStatus.Internal
    public final void onBlockExplodeEvent(final BlockExplodeEvent event, final Tile tile, final Block tileBlock) {
        onBlockExplode(event, (T) tile, tileBlock);
    }

    /**
     * Wrapper method used for casting for internal use only
     *
     * @param event     the event
     * @param tile      the tile
     * @param tileBlock the block the tile is
     * @since 1.0.0-SNAPSHOT
     */
    @ApiStatus.Internal
    public final void onEntityExplodeEvent(final EntityExplodeEvent event, final Tile tile, final Block tileBlock) {
        onEntityExplode(event, (T) tile, tileBlock);
    }

    /**
     * Wrapper method used for casting for internal use only
     *
     * @param event event
     * @param tile  tile
     * @since 1.0.0-SNAPSHOT
     */
    @ApiStatus.Internal
    public final void onPlayerInteractEvent(final PlayerInteractEvent event, final Tile tile) {
        onInteract(event, (T) tile);
    }

    /**
     * Wrapper method used for casting for internal use only
     *
     * @param event event
     * @param tile  tile
     * @since 1.0.0-SNAPSHOT
     */
    @ApiStatus.Internal
    public final void onBlockBreakEvent(final BlockBreakEvent event, final Tile tile) {
        onBreak(event, (T) tile);
    }

    /**
     * Wrapper method used for casting for internal use only
     *
     * @param event event
     * @param tile  tile
     * @since 1.0.0-SNAPSHOT
     */
    @ApiStatus.Internal
    public final void onBlockPlaceEvent(final BlockPlaceEvent event, final Tile tile) {
        onPlace(event, (T) tile);
    }

    /**
     * Wrapper method used for casting for internal use only
     *
     * @param tile tile
     * @since 1.0.0-SNAPSHOT
     */
    @ApiStatus.Internal
    public final void onTickLoop(final Tile tile) {
        tick((T) tile);
    }

    /**
     * Creates an item from a possible tile
     *
     * @param tile the tile
     * @return the item stack
     * @since 1.0.0-SNAPSHOT
     */
    public ItemStack createItem(@Nullable final T tile) {
        return createItemShell(tile)
                .persistentData(TileKeys.getTileTypeKey(), PersistentDataType.STRING, getKey().toString())
                .build();
    }

    /**
     * Defines drop logic for this tile type.
     *
     * @param event the block drop event
     * @param tile  the tile being dropped
     * @since 1.0.0-SNAPSHOT
     */
    protected void onDrop(final BlockDropItemEvent event, final T tile) {
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            return;
        }
        if (event.getItems().size() > 1 || event.getItems().isEmpty()) {
            throw new UnsupportedOperationException("The default implementation of TileType#onDrop can not handle multi or no drop blocks");
        }
        event.getItems().get(0).setItemStack(createItem(tile));
    }

    /**
     * Defines explosion logic for this tile type.
     *
     * @param event     the explode event
     * @param tile      the tile being exploded
     * @param tileBlock the block the tile is
     * @since 1.0.0-SNAPSHOT
     */
    protected void onBlockExplode(final BlockExplodeEvent event, final T tile, final Block tileBlock) {
        event.blockList().remove(tileBlock);
    }

    /**
     * Defines explosion logic for this tile type.
     *
     * @param event     the explode event
     * @param tile      the tile being exploded
     * @param tileBlock the block the tile is
     * @since 1.0.0-SNAPSHOT
     */
    protected void onEntityExplode(final EntityExplodeEvent event, final T tile, final Block tileBlock) {
        event.blockList().remove(tileBlock);
    }

    /**
     * Defines interaction logic for this tile type.
     *
     * @param event the interact event
     * @param tile  the tile being interacted with
     * @since 1.0.0-SNAPSHOT
     */
    protected void onInteract(final PlayerInteractEvent event, final T tile) {
    }

    /**
     * Defines break logic for this tile type.
     *
     * @param event the break event
     * @param tile  the tile being broken
     * @since 1.0.0-SNAPSHOT
     */
    protected void onBreak(final BlockBreakEvent event, final T tile) {
    }

    /**
     * Defines place logic for this tile type.
     *
     * @param event the place event
     * @param tile  the tile being place
     * @since 1.0.0-SNAPSHOT
     */
    protected void onPlace(final BlockPlaceEvent event, final T tile) {
    }

    /**
     * Defines the behavior on tick for this tile type.
     * <p>
     * It is important to note that when implementing this method. This method runs every single tick. This means heavy
     * operations could freeze the server.
     *
     * @param tile the tile being ticked
     * @since 1.0.0-SNAPSHOT
     */
    protected void tick(final T tile) {
    }

    /**
     * Creates a shell item for the TileType.
     * <p>
     * This shell item is transformed in order to add extra information needed to identify the item later.
     *
     * @param tile the possible tile
     * @return the item builder
     * @since 1.0.0-SNAPSHOT
     */
    public abstract ItemBuilder createItemShell(@Nullable T tile);

    /**
     * Creates a tile
     *
     * @return the created tile
     * @since 1.0.0-SNAPSHOT
     */
    public abstract T createTile();

    /**
     * Creates a tile from an item
     *
     * @param item the item to create this tile from
     * @return the created tile
     * @since 1.0.0-SNAPSHOT
     */
    public abstract T createTile(final ItemStack item);
}
