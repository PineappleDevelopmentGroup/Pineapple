package sh.miles.pineapple.gui.slot;

import com.google.common.base.Preconditions;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * An Abstract GuiSlot implementation that fulfills requirements all slots must fulfill
 */
public abstract class AbstractGuiSlot implements GuiSlot {

    protected final Inventory inventory;
    protected final int index;

    protected AbstractGuiSlot(@NotNull final Inventory inventory, final int index) {
        Preconditions.checkArgument(inventory != null, "The given inventory must not be null");
        Preconditions.checkArgument(index >= 0 && index < inventory.getSize(), "The given index must be in the bounds %d, %d you provided %d".formatted(0, inventory.getSize(), index));
        this.inventory = inventory;
        this.index = index;
    }

    @Override
    public void setItem(@NotNull final ItemStack item) {
        this.inventory.setItem(this.index, item);
    }

    @NotNull
    @Override
    public ItemStack getItem() {
        final ItemStack item = inventory.getItem(this.index);
        return item == null ? new ItemStack(Material.AIR) : item;
    }

    @Override
    public boolean hasItem() {
        return inventory.getItem(this.index) != null && !inventory.getItem(this.index).getType().isAir();
    }
}
