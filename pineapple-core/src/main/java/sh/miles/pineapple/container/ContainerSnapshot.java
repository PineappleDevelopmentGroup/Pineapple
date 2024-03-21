package sh.miles.pineapple.container;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

/**
 * A Snapshot of a container which provides read only access to a snapshot of a container
 *
 * @since 1.0.0-SNAPSHOT
 */
public interface ContainerSnapshot extends Iterable<ItemStack> {

    /**
     * Gets an item at the specified index
     *
     * @param index the index of the item
     * @return the ItemStack at that slot or an ItemStack of air
     * @throws IndexOutOfBoundsException if the index is out of bounds
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    ItemStack getItemAt(final int index) throws IndexOutOfBoundsException;

    /**
     * Checks if the snapshot contains the specified item
     *
     * @param item the item
     * @return true if the snapshot contains the item, otherwise false
     * @since 1.0.0-SNAPSHOT
     */
    boolean contains(@NotNull final ItemStack item);

    /**
     * Gets a list of contents
     *
     * @return a list of the Containers contents
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    List<ItemStack> getContents();

    /**
     * Gets the size of the container snapshot
     *
     * @return the container size
     * @since 1.0.0-SNAPSHOT
     */
    int getSize();

    @NotNull
    @Override
    default Iterator<ItemStack> iterator() {
        return getContents().iterator();
    }
}
