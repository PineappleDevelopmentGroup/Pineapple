package sh.miles.pineapple.nms.api.menu.scene.custom;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Gives context to states and data from a custom menu
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
     */
    @NotNull
    MergeResult mergeItemStackBetween(@NotNull final ItemStack item, final int startIndex, final int endIndex, final boolean reverse);

    /**
     * Callback for when a crafting matrix is changed
     */
    void slotsChanged();

    /**
     * Gets the slot at the given raw slot
     *
     * @param rawSlot the raw slot
     * @return the custom slot implementation
     */
    @NotNull
    CustomMenuSlot getMenuSlot(final int rawSlot);

    /**
     * Retrieves a bukkit container
     *
     * @return the bukkit container
     */
    @NotNull
    Inventory getBukkitContainer();

    /**
     * Gets the amount of rows the menu's container has
     *
     * @return the amount of rows
     */
    int getRowAmount();

    /**
     * Gets the amount of slots this container has
     *
     * @return the amount of slots
     */
    int getSlotAmount();

    record MergeResult(@NotNull ItemStack item, boolean result) {
    }
}
