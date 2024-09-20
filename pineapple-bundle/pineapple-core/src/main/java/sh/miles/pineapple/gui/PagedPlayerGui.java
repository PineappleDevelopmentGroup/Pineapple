package sh.miles.pineapple.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.collection.NonNullArray;
import sh.miles.pineapple.gui.slot.DummyGuiSlot;
import sh.miles.pineapple.gui.slot.GuiSlot;
import sh.miles.pineapple.nms.annotations.NMS;
import sh.miles.pineapple.nms.api.menu.scene.MenuScene;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Represents a visible, item holding, display to the player
 *
 * @param <T> the type of MenuScene
 * @since 1.0.0-SNAPSHOT
 */
@NMS
public abstract class PagedPlayerGui<T extends MenuScene> extends PlayerGui<T> {

    private final Map<Integer, NonNullArray<GuiSlot>> pages;
    private final Inventory topInventory;
    private int currentPage;

    protected PagedPlayerGui(@NotNull final Function<Player, T> scene, @NotNull final Player viewer) {
        super(scene, viewer);
        this.topInventory = topInventory();
        this.pages = new HashMap<>();
        this.currentPage = 0;

        for (int i = 0; i < findMaxPage(); i++) {
            this.pages.put(i, new NonNullArray<>(0, () -> new DummyGuiSlot(topInventory, -1)));
        }
    }

    /**
     * Sets the slot at the given index
     *
     * @param page      the page
     * @param index     the index
     * @param slotMaker the gui slot maker
     * @throws IllegalArgumentException if the slot index is invalid
     * @since 1.0.0-SNAPSHOT
     */
    public void slot(final int page, final int index, @NotNull final Function<Inventory, GuiSlot> slotMaker) throws IllegalArgumentException {
        verifySlotIndex(index);
        this.pages.get(page).set(index, slotMaker.apply(this.topInventory));
    }

    /**
     * Deploys the next page, if an overflow occurs it wraps to the minimum page
     *
     * @since 1.0.0-SNAPSHOT
     */
    public void nextPage() {
        this.currentPage += 1;
        if (this.currentPage >= this.pages.size()) {
            this.currentPage = 0;
        }

        this.deployPage(this.currentPage);
    }

    /**
     * Deploys the previous page, if an overflow occurs it wraps to the maximum page
     *
     * @since 1.0.0-SNAPSHOT
     */
    public void previousPage() {
        this.currentPage -= 1;
        if (this.currentPage < 0) {
            this.currentPage = this.pages.size() - 1;
        }

        this.deployPage(this.currentPage);
    }

    /**
     * Deploys the given page to the gui
     *
     * @param page the page to deploy
     */
    protected void deployPage(int page) {
        final NonNullArray<GuiSlot> slots = pages.get(page);
        regenerateEmptySlots();

        for (int i = 0; i < slots.size(); i++) {
            final GuiSlot slot = slots.get(i);
            if (slot instanceof DummyGuiSlot) {
                continue;
            }
            slot(i, (inventory) -> slot);
        }
    }

    /**
     * Method used to find the maximum amount of pages
     *
     * @return the maximum amount of pages
     */
    protected abstract int findMaxPage();
}
