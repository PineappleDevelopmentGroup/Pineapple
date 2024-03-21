package sh.miles.pineapple.container;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * A Container which can contain a list of items
 *
 * @since 1.0.0-SNAPSHOT
 */
public interface Container extends ContainerSnapshot {

    /**
     * Sets the item at the specified index
     *
     * @param index the index to set the item at
     * @param item  the item to set
     * @throws IndexOutOfBoundsException thrown if index exceeds the container size
     * @since 1.0.0-SNAPSHOT
     */
    void setItemAt(int index, @NotNull final ItemStack item) throws IndexOutOfBoundsException;

    /**
     * Adds an item to the Container
     *
     * @param item the item to add
     * @return false if the item could not be added, otherwise true
     * @since 1.0.0-SNAPSHOT
     */
    boolean addItem(@NotNull final ItemStack item);

    /**
     * Takes a Snapshot of the container's contents
     *
     * @return the snapshot
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    ContainerSnapshot getSnapshot();
}
