package sh.miles.pineapple.gui.slot;

import com.google.common.base.Preconditions;
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

    /**
     * An Action that occurs when clicking the item
     *
     * @param event the click event
     * @since 1.0.0-SNAPSHOT
     */
    void click(@NotNull final InventoryClickEvent event);

    /**
     * Sets the content of this gui slot
     *
     * @param item the item content
     * @since 1.0.0-SNAPSHOT
     */
    void setItem(@NotNull final ItemStack item);

    /**
     * Gets hte item content of this gui slot
     *
     * @return the item
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    ItemStack getItem();

    /**
     * Checks whether or not this slot has an item
     *
     * @return true if this slot has an item
     * @since 1.0.0-SNAPSHOT
     */
    boolean hasItem();

    /**
     * A GuiSlotBuilder that builds a different slot type depending on the given parameters
     *
     * @since 1.0.0-SNAPSHOT
     */
    final class GuiSlotBuilder {

        private Inventory inventory = null;
        private int index = -999;
        private Consumer<InventoryClickEvent> click = null;
        private ItemStack item = null;

        public GuiSlotBuilder() {
        }

        /**
         * Sets the inventory
         *
         * @param inventory the inventory
         * @return the current GuiSlotBuilder
         * @since 1.0.0-SNAPSHOT
         */
        public GuiSlotBuilder inventory(@NotNull final Inventory inventory) {
            Preconditions.checkArgument(inventory != null, "The given inventory must not be null");
            this.inventory = inventory;
            return this;
        }

        /**
         * Sets the slot index
         *
         * @param index the slot index
         * @return the current GuiSlotBuilder
         * @since 1.0.0-SNAPSHOT
         */
        public GuiSlotBuilder index(final int index) {
            Preconditions.checkArgument(index >= 0 && index < inventory.getSize(), "The given index must be in the bounds %d, %d".formatted(0, inventory.getSize()));
            this.index = index;
            return this;
        }

        /**
         * Sets the click event
         *
         * @param click the click event
         * @return the current GuiSlotBuilder
         * @since 1.0.0-SNAPSHOT
         */
        public GuiSlotBuilder click(final Consumer<InventoryClickEvent> click) {
            Preconditions.checkArgument(click != null, "The given click consumer must not be null");
            this.click = click;
            return this;
        }

        /**
         * Sets the item icon
         *
         * @param item the item
         * @return the current GuiSlotBuilder
         * @since 1.0.0-SNAPSHOT
         */
        public GuiSlotBuilder item(final ItemStack item) {
            Preconditions.checkArgument(item != null, "The given item must not be null");
            this.item = item;
            return this;
        }

        /**
         * Builds the current slot builder into a slot given the preconditions are met
         *
         * @return the newly created GuiSlot
         * @throws IllegalStateException thrown if the inventory and index aren't set
         * @since 1.0.0-SNAPSHOT
         */
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
