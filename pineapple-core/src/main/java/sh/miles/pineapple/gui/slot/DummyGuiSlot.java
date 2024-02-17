package sh.miles.pineapple.gui.slot;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * A GuiSlot implementation that can not be interacted with
 */
public class DummyGuiSlot extends AbstractGuiSlot {
    public DummyGuiSlot(@NotNull final Inventory inventory, final int index) {
        super(inventory, index);
    }

    @Override
    public void click(@NotNull final InventoryClickEvent event) {
    }
}
