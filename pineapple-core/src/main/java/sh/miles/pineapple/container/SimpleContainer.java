package sh.miles.pineapple.container;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.collection.NonNullArray;

import java.util.List;

/**
 * A Simple Implementation of the {@link Container} interface which provides a basic List backed Container
 *
 * @since 1.0.0-SNAPSHOT
 */
public class SimpleContainer implements Container {

    private final NonNullArray<ItemStack> contents;

    /**
     * Creates a new SimpleContainer
     *
     * @param size the size of the container
     * @since 1.0.0-SNAPSHOT
     */
    public SimpleContainer(final int size) {
        this.contents = new NonNullArray<>(size, () -> new ItemStack(Material.AIR));
    }

    /**
     * Creates a new SimpleContainer
     *
     * @param expandBy  the amount to expandBy negative numbers shrink
     * @param container the container to use as a base
     * @since 1.0.0-SNAPSHOT
     */
    public SimpleContainer(final int expandBy, @NotNull final SimpleContainer container) {
        if (expandBy >= 0) {
            this.contents = container.contents.grow(expandBy);
        } else {
            this.contents = container.contents.shrink(expandBy);
        }
    }

    @NotNull
    @Override
    public ItemStack getItemAt(final int index) throws IndexOutOfBoundsException {
        return this.contents.get(index);
    }

    @Override
    public void setItemAt(final int index, @NotNull final ItemStack item) throws IndexOutOfBoundsException {
        this.contents.set(index, item);
    }

    @Override
    public boolean addItem(@NotNull final ItemStack item) {
        return this.contents.add(item);
    }

    @Override
    public boolean contains(@NotNull final ItemStack item) {
        return this.contents.contains(item);
    }

    @NotNull
    @Override
    public ContainerSnapshot getSnapshot() {
        return new SimpleContainerSnapshot(this.contents);
    }

    @NotNull
    @Override
    public List<ItemStack> getContents() {
        return this.contents.stream().map(ItemStack::clone).toList();
    }

    @Override
    public int getSize() {
        return this.contents.size();
    }
}
