package sh.miles.pineapple.gui.slot;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;


/**
 * A DeployableSlot is a child of SimpleSlot which stores its item so its context aware and can redeploy the stored
 * item.
 *
 * @since 1.0.0-SNAPSHOT
 */
public class DeployableSlot extends SimpleGuiSlot implements Deployable {

    private ItemStack deployItem;

    /**
     * Creates a new Deployable slot that can be deployed multiple times with the deploy method.
     *
     * @param inventory the inventory
     * @param index     the index
     * @param click     the click
     * @param drag      the drag
     * @since 1.0.0-SNAPSHOT
     */
    public DeployableSlot(@NotNull final Inventory inventory, final int index, @NotNull final Consumer<InventoryClickEvent> click, @NotNull final Consumer<InventoryDragEvent> drag) {
        super(inventory, index, click, drag);
        this.deployItem = new ItemStack(Material.AIR);
    }

    @Override
    public void setItem(@NotNull final ItemStack item) {
        if (item.getType().isAir()) {
            inventory.setItem(this.index, this.deployItem);
        }

        this.deployItem = item;
        inventory.setItem(this.index, this.deployItem);
    }

    @NotNull
    @Override
    public ItemStack getItem() {
        final ItemStack item = inventory.getItem(this.index);
        return item == null ? this.deployItem : item;
    }

    @Override
    public void deploy() {
        inventory.setItem(this.index, this.deployItem);
    }
}
