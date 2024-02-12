package sh.miles.pineapple.gui.slot;

import com.google.common.base.Preconditions;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * A basic outline for what a GuiSlot implementation should be capable of
 *
 * @since 1.0.0-SNAPSHOT
 */
public interface GuiSlot {

    void click(@NotNull final InventoryClickEvent event);

    void setItem(@NotNull final ItemStack item);

    @NotNull
    ItemStack getItem();

    boolean hasItem();

    final class GuiSlotBuilder {

        private Inventory inventory = null;
        private int index = -999;
        private Consumer<InventoryClickEvent> click = null;
        private ItemStack item = null;

        public GuiSlotBuilder() {
        }

        public GuiSlotBuilder inventory(@NotNull final Inventory inventory) {
            Preconditions.checkArgument(this.inventory != null, "The given inventory must not be null");
            this.inventory = inventory;
            return this;
        }

        public GuiSlotBuilder index(final int index) {
            Preconditions.checkArgument(index > 0 && index < inventory.getSize(), "The given index must be in the bounds %d, %d".formatted(0, inventory.getSize()));
            this.index = index;
            return this;
        }

        public GuiSlotBuilder click(final Consumer<InventoryClickEvent> click) {
            Preconditions.checkArgument(click != null, "The given click consumer must not be null");
            this.click = click;
            return this;
        }

        public GuiSlotBuilder item(final ItemStack item) {
            Preconditions.checkArgument(item != null, "The given item must not be null");
            this.item = item;
            return this;
        }

        @NotNull
        public GuiSlot build() throws IllegalStateException {
            Preconditions.checkState(this.inventory != null, "A slot can not be constructed without an inventory");
            Preconditions.checkState(this.index != -999, "A slot can not be constructed without a valid slot");

            final GuiSlot slot;
            if (this.click == null) {
                slot = new DummyGuiSlot(this.inventory, index);
            } else {
                slot = new SimpleGuiSlot(this.inventory, index, click);
            }

            if (item != null) {
                slot.setItem(item);
            }

            return slot;
        }
    }
}
