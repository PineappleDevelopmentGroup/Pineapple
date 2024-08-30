package sh.miles.crown.infstacks;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

/**
 * An ItemStack wrapper which allows the growth and shrinking beyond the
 */
public class InfStack {

    private ItemStack display;
    private final ItemStack comparator;
    private final InfStackSettings settings;
    private long stackSize;

    /**
     * Creates a new InfStack from the following data
     *
     * @param display    the display item
     * @param comparator the comparator item
     * @param stackSize  the stack current size
     * @param settings   all related settings
     */
    InfStack(@NotNull final ItemStack display, @NotNull final ItemStack comparator, final long stackSize, @NotNull final InfStackSettings settings) {
        this.display = display.clone();
        this.comparator = comparator.clone();
        this.settings = settings;
        this.stackSize = stackSize;
    }

    /**
     * Grows the InfStack with the ItemStack if it is of the same type
     *
     * @param item the item to grow with
     * @return true if the InfStack could be grown
     */
    public boolean grow(@NotNull final ItemStack item) {
        if (isAir()) {
            return false;
        }
        if (!this.comparator.isSimilar(item)) {
            return false;
        }

        final int amount = item.getAmount();
        return grow(amount);
    }

    /**
     * Extracts an ItemStack from this InfStack
     *
     * @param amount the size of the ItemStack to extract
     * @return the expected extraction or air
     */
    @NotNull
    public ItemStack extract(int amount) {
        if (isAir()) {
            return new ItemStack(Material.AIR);
        }
        if (amount > 64 || amount < 0) {
            throw new IllegalStateException("The given amount %d is not within the range 0 to 64".formatted(amount));
        }

        if (this.stackSize < amount) {
            amount = (int) this.stackSize;
        }
        final ItemStack extracted = comparator.clone();
        if (!shrink(amount)) {
            throw new IllegalStateException("Extracted stack unable to shrink, this is a bug!");
        }
        extracted.setAmount(amount);
        return extracted;
    }

    /**
     * Grows the stack by a specified amount
     *
     * @param amount the amount to grow by
     * @return true if the growth succeeded, otherwise false
     */
    public boolean grow(final long amount) {
        if (isAir() || isEmpty()) {
            return false;
        }
        final long finalAmount = amount + this.stackSize;
        if (finalAmount > this.settings.maxStackSize()) {
            return false;
        }

        this.stackSize = finalAmount;
        update();
        return true;
    }

    /**
     * Shrinks the stack by a specified amount
     *
     * @param amount the amount to grow by
     * @return true if the growth succeeded, otherwise false
     */
    public boolean shrink(final long amount) {
        if (isAir()) {
            return false;
        }
        final long finalAmount = this.stackSize - amount;
        if (finalAmount < 0) {
            return false;
        }

        this.stackSize = finalAmount;
        update();
        return true;
    }

    private void update() {
        this.display = this.settings.loreApplier().apply(this.settings.lore(), this.stackSize, this.display, this.comparator, true);

        var meta = this.display.getItemMeta();
        assert meta != null;
        var container = meta.getPersistentDataContainer();
        container.set(InfStackUtils.STACK_SIZE_KEY, PersistentDataType.LONG, this.stackSize);
        this.display.setItemMeta(meta);
    }

    /**
     * Checks if another item stack is similar to this InfStack
     *
     * @param other the other item
     * @return true if the items are similar, otherwise false
     */
    public boolean isSimilar(@NotNull final ItemStack other) {
        return this.comparator.isSimilar(other);
    }

    /**
     * Checks whether this InfStack is air
     *
     * @return true if it is air
     */
    public boolean isAir() {
        return this.comparator.getType().isAir();
    }

    /**
     * Checks if the InfStack is empty
     *
     * @return true if the InfStack is empty
     */
    public boolean isEmpty() {
        return this.stackSize <= 0;
    }

    /**
     * Gets hte stack size of the InfStack
     *
     * @return the stack size
     */
    public long getStackSize() {
        return this.stackSize;
    }

    /**
     * Gets the display ItemStack
     *
     * @return the display item stack
     */
    @NotNull
    public ItemStack getDisplay() {
        return this.display.clone();
    }

    /**
     * Gets the comparator ItemStack
     *
     * @return the comparator item stack
     */
    @NotNull
    public ItemStack getComparator() {
        return this.comparator.clone();
    }


}
