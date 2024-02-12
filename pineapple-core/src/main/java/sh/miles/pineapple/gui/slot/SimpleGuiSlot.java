package sh.miles.pineapple.gui.slot;

import com.google.common.base.Preconditions;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class SimpleGuiSlot extends AbstractGuiSlot {

    private final Consumer<InventoryClickEvent> click;

    public SimpleGuiSlot(@NotNull final Inventory inventory, final int index, @NotNull final Consumer<InventoryClickEvent> click) {
        super(inventory, index);
        Preconditions.checkArgument(click != null, "The given click must not be null");
        this.click = click;
    }

    @Override
    public void click(@NotNull final InventoryClickEvent event) {
        this.click.accept(event);
    }
}
