package sh.miles.pineapple.tiles.api;

import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sh.miles.pineapple.collection.registry.RegistryKey;
import sh.miles.pineapple.item.ItemBuilder;
import sh.miles.pineapple.tiles.internal.util.TileKeys;

/**
 * Represents a type of tile within the world.
 * <p>
 * Every type of tile within the world has behavior this class can be used to define such behaviors such as ticking,
 * placing picking up and more.
 *
 * @param <T> the type of tile this tile type represents behavior for
 * @since 1.0.0-SNAPSHOT
 */
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
     */
    @ApiStatus.Internal
    public final void onBlockDropItemEvent(@NotNull final BlockDropItemEvent event, @NotNull final Tile tile) {
        onDrop(event, (T) tile);
    }

    /**
     * Wrapper method used for casting for internal use only
     *
     * @param event event
     * @param tile  tile
     */
    @ApiStatus.Internal
    public final void onPlayerInteractEvent(@NotNull final PlayerInteractEvent event, @NotNull final Tile tile) {
        onInteract(event, (T) tile);
    }

    /**
     * Wrapper method used for casting for internal use only
     *
     * @param event event
     * @param tile  tile
     */
    @ApiStatus.Internal
    public final void onBlockBreakEvent(@NotNull final BlockBreakEvent event, @NotNull final Tile tile) {
        onBreak(event, (T) tile);
    }

    /**
     * Wrapper method used for casting for internal use only
     *
     * @param event event
     * @param tile  tile
     */
    @ApiStatus.Internal
    public final void onBlockPlaceEvent(@NotNull final BlockPlaceEvent event, @NotNull final Tile tile) {
        onPlace(event, (T) tile);
    }

    /**
     * Wrapper method used for casting for internal use only
     *
     * @param tile tile
     */
    @ApiStatus.Internal
    public final void onTickLoop(@NotNull final Tile tile) {
        tick((T) tile);
    }

    /**
     * Creates an item from a possible tile
     *
     * @param tile the tile
     * @return the item stack
     */
    public ItemStack createItem(@Nullable final T tile) {
        return createItemShell(tile)
                .persistentData(TileKeys.TILE_TYPE_KEY, PersistentDataType.STRING, getKey().toString())
                .build();
    }

    /**
     * Defines drop logic for this tile type.
     *
     * @param event the block drop event
     * @param tile  the tile being dropped
     * @since 1.0.0-SNAPSHOT
     */
    protected void onDrop(@NotNull final BlockDropItemEvent event, @NotNull final T tile) {
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            return;
        }
        if (event.getItems().size() > 1 || event.getItems().isEmpty()) {
            throw new UnsupportedOperationException("The default implementation of TileType#onDrop can not handle multi or no drop blocks");
        }
        event.getItems().get(0).setItemStack(createItem(tile));
    }

    /**
     * Defines interaction logic for this tile type.
     *
     * @param event the interact event
     * @param tile  the tile being interacted with
     * @since 1.0.0-SNAPSHOT
     */
    protected void onInteract(@NotNull final PlayerInteractEvent event, @NotNull final T tile) {
    }

    /**
     * Defines break logic for this tile type.
     *
     * @param event the break event
     * @param tile  the tile being broken
     * @since 1.0.0-SNAPSHOT
     */
    protected void onBreak(@NotNull final BlockBreakEvent event, @NotNull final T tile) {
    }

    /**
     * Defines place logic for this tile type.
     *
     * @param event the place event
     * @param tile  the tile being place
     * @since 1.0.0-SNAPSHOT
     */
    protected void onPlace(@NotNull final BlockPlaceEvent event, @NotNull final T tile) {
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
    protected void tick(@NotNull final T tile) {
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
    @NotNull
    public abstract ItemBuilder createItemShell(@Nullable T tile);

    /**
     * Creates a tile
     *
     * @return the created tile
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public abstract T createTile();

    /**
     * Creates a tile from an item
     *
     * @param item the item to create this tile from
     * @return the created tile
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public abstract T createTile(@NotNull final ItemStack item);
}
