package sh.miles.pineapple.container;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface Container extends ContainerSnapshot {

    /**
     * Sets the item at the specified index
     *
     * @param index the index to set the item at
     * @param item  the item to set
     * @throws IndexOutOfBoundsException thrown if index exceeds the container size
     */
    void setItem(int index, @NotNull final ItemStack item) throws IndexOutOfBoundsException;
}
