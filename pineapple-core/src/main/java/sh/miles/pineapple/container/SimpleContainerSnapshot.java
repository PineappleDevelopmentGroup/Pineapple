package sh.miles.pineapple.container;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.collection.NonNullArray;

import java.util.List;

/**
 * A Simple implementation of ContainerSnapshot backed by a list
 *
 * @since 1.0.0-SNAPSHOT
 */
public class SimpleContainerSnapshot implements ContainerSnapshot {

    private final NonNullArray<ItemStack> contents;

    /**
     * Creates a new SimpleContainerSnapshot
     *
     * @param contents the contents to snapshot
     * @since 1.0.0-SNAPSHOT
     */
    public SimpleContainerSnapshot(@NotNull final NonNullArray<ItemStack> contents) {
        this.contents = contents.copy(ItemStack::clone);
    }

    @NotNull
    @Override
    public ItemStack getItemAt(final int index) throws IndexOutOfBoundsException {
        return this.contents.get(index).clone();
    }

    @Override
    public boolean contains(@NotNull final ItemStack item) {
        return this.contents.contains(item);
    }

    @NotNull
    @Override
    public List<ItemStack> getContents() {
        return this.contents.stream().toList();
    }

    @Override
    public int getSize() {
        return this.contents.size();
    }
}
