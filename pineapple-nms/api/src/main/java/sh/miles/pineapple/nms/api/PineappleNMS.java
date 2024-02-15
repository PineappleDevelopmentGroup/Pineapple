package sh.miles.pineapple.nms.api;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sh.miles.pineapple.nms.annotations.PullRequested;
import sh.miles.pineapple.nms.api.menu.scene.MenuScene;
import sh.miles.pineapple.nms.api.menu.scene.custom.CustomMenuListener;
import sh.miles.pineapple.nms.api.menu.scene.custom.CustomSlotListener;
import sh.miles.pineapple.nms.api.packet.PineapplePackets;

import java.util.List;

/**
 * Pineapple NMS adapts a bunch of useful stuffs from NMS so that we can use it. Don't tell MD_5 he won't be happy.
 *
 * @since 1.0.0-SNAPSHOT
 */
public interface PineappleNMS {

    /**
     * Creates a custom MenuScene with the given player and behavior
     * <p>
     * You can create custom behavior by implementing {@link CustomMenuListener} as well as implementing
     * {@link CustomSlotListener}. In conjunction you can specify a wide arrays of functions an Inventory can take
     *
     * @param player       the player
     * @param menuListener the menu listener
     * @param rows         the amount of rows, no more than 6 no less than 1
     * @param title        the title of the menu
     * @return the MenuScene that can be opened and modified
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    MenuScene createMenuCustom(@NotNull final Player player, @NotNull final CustomMenuListener menuListener, final int rows, @NotNull final BaseComponent title);

    /**
     * Opens an inventory with a base component title. Note this implementation uses CraftContainer so Inventories
     * opened in this way will not work as intended
     *
     * @param player    the player
     * @param inventory the inventory to open
     * @param title     the title to use
     * @return the legacy bukkit inventory view associated with this task
     * @since 1.0.0-SNAPSHOT
     */
    @Nullable
    @PullRequested
    InventoryView openInventory(@NotNull final Player player, @NotNull final Inventory inventory, @NotNull BaseComponent title);

    /**
     * Renames the given item stack with the base component
     *
     * @param item        the item to rename
     * @param displayName the display name
     * @return the item with its display name changed
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    @PullRequested
    ItemStack setItemDisplayName(@NotNull final ItemStack item, BaseComponent displayName);

    /**
     * Sets the lore of the given item stack with the base component list
     *
     * @param item the item to set the lore of
     * @param lore the lore
     * @return the item with its lore changed
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    @PullRequested
    ItemStack setItemLore(@NotNull final ItemStack item, List<BaseComponent> lore);

    /**
     * Gets the lore of the given item stack and returns the component list
     *
     * @param item the item to get the lore of
     * @return the lore
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    @PullRequested
    List<BaseComponent> getItemLore(@NotNull final ItemStack item);

    /**
     * Converts the given ItemStack into bytes
     *
     * @param itemStack the item stack to convert to bytes
     * @return the bytes
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    byte[] itemToBytes(@NotNull final ItemStack itemStack);

    /**
     * Converts teh given bytes into an ItemStack
     *
     * @param bytes the bytes to convert into an ItemStack
     * @return the ItemStack
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    ItemStack itemFromBytes(@NotNull final byte[] bytes);

    /**
     * Gets the packet handler
     *
     * @return the packet handler
     * @since 1.0.0-SNAPSHOT
     */
    PineapplePackets getPackets();

    /**
     * Gets the PineappleUnsafe class
     *
     * @return the PineappleUnsafe class
     * @since 1.0.0-SNAPSHOT
     */
    @ApiStatus.Internal
    @NotNull
    PineappleUnsafe getUnsafe();

}
