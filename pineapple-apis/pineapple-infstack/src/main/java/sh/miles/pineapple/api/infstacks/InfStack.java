package sh.miles.pineapple.api.infstacks;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.NullMarked;

/**
 * An ItemStack wrapper which allows the growth and shrinking beyond the
 */
@NullMarked
public class InfStack {

    private ItemStack display;
    private final ItemStack comparator;
    private final InfStackSettings settings;
    private long stackSize;
    private boolean manualUpdate;

    /**
     * Creates a new InfStack from the following data
     *
     * @param display    the display item
     * @param comparator the comparator item
     * @param stackSize  the stack current size
     * @param settings   all related settings
     */
    InfStack(final ItemStack display, final ItemStack comparator, final long stackSize, final InfStackSettings settings) {
        this.display = display.clone();
        this.comparator = comparator.clone();
        this.settings = settings;
        this.stackSize = stackSize;
        this.manualUpdate = settings.defaultManual();
    }

    /**
     * Stores as much of the given ItemStack as possible
     *
     * @param item the item to try to fit
     * @return the remaining item that was able to fit, or the same item stack if it could not fit at all
     */
    public ItemStack fit(final ItemStack item) {
        if (!isAir()) {
            return item;
        }
        if (!this.comparator.isSimilar(item)) {
            return item;
        }

        final int amount = item.getAmount();
        int remaining = (int) fit(amount);
        if (remaining == 0) {
            return ItemStack.empty();
        }

        final ItemStack clone = item.clone();
        clone.setAmount(remaining);
        return clone;
    }

    /**
     * Grows the InfStack with the ItemStack if it is of the same type
     *
     * @param item the item to grow with
     * @return true if the InfStack could be grown
     */
    public boolean grow(final ItemStack item) {
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
     * Fits as much of the specified amount into the stack as possible
     * and returns a long in the amount that could not fit
     *
     * @param amount the amount to try to fit into this stack
     * @return the amount unable to fit
     */
    public long fit(final long amount) {
        if (isAir() || isEmpty()) {
            return amount;
        }
        long potentialTotal = this.stackSize + amount;
        long amountToAdd = amount;
        if (potentialTotal > this.settings.maxStackSize()) {
            amountToAdd = this.settings.maxStackSize() - this.stackSize;
        }
        this.stackSize += amountToAdd;
        update(false);
        return amount - amountToAdd;
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
        update(false);
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
        update(false);
        return true;
    }

    public void update() {
        update(true);
    }

    private void update(boolean force) {
        if (this.manualUpdate && !force) {
            return;
        }

        this.display = this.settings.loreApplier()
            .apply(this.settings.lore(), this.stackSize, this.display, this.comparator, true);

        var meta = this.display.getItemMeta();
        assert meta != null;
        var container = meta.getPersistentDataContainer();
        container.set(InfStackUtils.STACK_SIZE_KEY, PersistentDataType.LONG, this.stackSize);
        this.display.setItemMeta(meta);
    }

    /**
     * Toggle whether or not this InfStack should be manually updated
     *
     * @param manualUpdate true to make it manual update only, otherwise false to toggle automatic updates
     */
    public void manualUpdates(boolean manualUpdate) {
        this.manualUpdate = manualUpdate;
    }

    /**
     * Checks if another item stack is similar to this InfStack
     *
     * @param other the other item
     * @return true if the items are similar, otherwise false
     */
    public boolean isSimilar(final ItemStack other) {
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
    public ItemStack getDisplay() {
        return this.display.clone();
    }

    /**
     * Gets the comparator ItemStack
     *
     * @return the comparator item stack
     */
    public ItemStack getComparator() {
        return this.comparator.clone();
    }

}
