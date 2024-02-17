package sh.miles.pineapple.gui.slot;

import com.google.common.base.Preconditions;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * A SimpleGuiSlot is a basic implementation of the AbstractGuiSlot which allows for clicks to occur
 *
 * @since 1.0.0-SNAPSHOT
 */
public class SimpleGuiSlot extends AbstractGuiSlot {

    private final Consumer<InventoryClickEvent> click;

    /**
     * Creates a new SimpleGuiSlot
     *
     * @param inventory the inventory
     * @param index     the index
     * @param click     the click
     * @since 1.0.0-SNAPSHOT
     */
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
