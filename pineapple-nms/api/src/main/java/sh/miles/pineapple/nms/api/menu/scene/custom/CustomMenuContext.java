package sh.miles.pineapple.nms.api.menu.scene.custom;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Gives context to states and data from a custom menu
 *
 * @since 1.0.0-SNAPSHOT
 */
@ApiStatus.NonExtendable
public interface CustomMenuContext {

    /**
     * Attempts to merge the provided item between the start and end indexes
     *
     * @param item       the item
     * @param startIndex the start index (included)
     * @param endIndex   the end index (excluded)
     * @param reverse    whether or not to reverse the search direction
     * @return the move result
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    MergeResult mergeItemStackBetween(@NotNull final ItemStack item, final int startIndex, final int endIndex, final boolean reverse);

    /**
     * Callback for when a crafting matrix is changed
     *
     * @since 1.0.0-SNAPSHOT
     */
    void slotsChanged();

    /**
     * Gets the slot at the given raw slot
     *
     * @param rawSlot the raw slot
     * @return the custom slot implementation
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    CustomMenuSlot getMenuSlot(final int rawSlot);

    /**
     * Retrieves a bukkit container
     *
     * @return the bukkit container
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    Inventory getBukkitContainer();

    /**
     * Gets the amount of rows the menu's container has
     *
     * @return the amount of rows
     * @since 1.0.0-SNAPSHOT
     */
    int getRowAmount();

    /**
     * Gets the amount of slots this container has
     *
     * @return the amount of slots
     * @since 1.0.0-SNAPSHOT
     */
    int getSlotAmount();

    /**
     * The result of merging to item stacks together
     *
     * @param item   the item merged
     * @param result whether or not the merge was successful
     * @since 1.0.0-SNAPSHOT
     */
    record MergeResult(@NotNull ItemStack item, boolean result) {
    }
}
