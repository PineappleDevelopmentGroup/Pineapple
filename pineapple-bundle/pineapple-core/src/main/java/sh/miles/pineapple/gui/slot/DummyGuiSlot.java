package sh.miles.pineapple.gui.slot;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * A GuiSlot implementation that can not be interacted with.
 *
 * @since 1.0.0-SNAPSHOT
 */
public class DummyGuiSlot extends AbstractGuiSlot implements Deployable {
    public DummyGuiSlot(@NotNull final Inventory inventory, final int index) {
        super(inventory, index);
    }

    @Override
    public void click(@NotNull final InventoryClickEvent event) {
    }

    @Override
    public void drag(@NotNull final InventoryDragEvent event) {
    }

    @Override
    public void deploy() {
        inventory.setItem(this.index, new ItemStack(Material.AIR));
    }
}
