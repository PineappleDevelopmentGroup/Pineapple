package sh.miles.pineapple.gui;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.PineappleLib;
import sh.miles.pineapple.gui.slot.DummyGuiSlot;
import sh.miles.pineapple.gui.slot.GuiSlot;
import sh.miles.pineapple.nms.annotations.NMS;
import sh.miles.pineapple.nms.api.menu.scene.MenuScene;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Represents a visible, item holding, display to the player
 *
 * @param <T> the type of MenuScene
 * @since 1.0.0-SNAPSHOT
 */
@NMS
public abstract class PlayerGui<T extends MenuScene> {

    protected final T scene;
    protected final Player viewer;
    private final Inventory topInventory;
    protected final List<GuiSlot> slots;

    protected PlayerGui(@NotNull final Function<Player, T> scene, @NotNull final Player viewer) {
        Preconditions.checkArgument(scene != null, "The given scene must not be null");
        Preconditions.checkState(viewer != null, "The given viewer must not be null");
        this.scene = scene.apply(viewer);
        Preconditions.checkState(this.scene != null, "The given scene function must return a non null value");
        this.viewer = viewer;
        this.topInventory = this.scene.getBukkitView().getTopInventory();
        Preconditions.checkState(this.topInventory != null, "The top inventory must not be null! This is a spigot bug!");
        this.slots = new ArrayList<>(this.topInventory.getSize());

        regenerateEmptySlots();
    }

    /**
     * Opens the GUI interaction screen
     *
     * @since 1.0.0-SNAPSHOT
     */
    public void open() throws IllegalStateException {
        if (this.scene.getBukkitView() == viewer.getOpenInventory()) {
            throw new IllegalStateException("Can not re-open same menu twice");
        }

        decorate();
        PineappleLib.getGuiManager().register(this);
        this.viewer.openInventory(this.scene.getBukkitView());
    }

    /**
     * Closes the GUI interaction screen if this is the screen open
     *
     * @throws IllegalStateException if the currently viewed gui by the player is not this gui
     * @since 1.0.0-SNAPSHOT
     */
    public void close() throws IllegalStateException {
        if (this.scene.getBukkitView() == viewer.getOpenInventory()) {
            viewer.closeInventory();
            PineappleLib.getGuiManager().unregister(this.topInventory);
            return;
        }
        throw new IllegalStateException("Attempted to close non-open player gui");
    }

    /**
     * Gets the viewer of this PlayerGui
     *
     * @return the viewer
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public Player viewer() {
        return this.viewer;
    }

    /**
     * Gets the scene of this PlayerGui
     *
     * @return the scene
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public T scene() {
        return this.scene;
    }

    /**
     * Sets the slot at the given index
     *
     * @param index     the index
     * @param slotMaker the gui slot maker
     * @throws IllegalArgumentException if the slot index is invalid
     * @since 1.0.0-SNAPSHOT
     */
    public void slot(final int index, @NotNull final Function<Inventory, GuiSlot> slotMaker) throws IllegalArgumentException {
        verifySlotIndex(index);
        this.slots.set(index, slotMaker.apply(this.topInventory));
    }

    /**
     * Gets the slot at the given index
     *
     * @param index the index
     * @return the gui slot
     * @throws IllegalArgumentException if the given index is invalid
     * @since 1.0.0-SNAPSHOT
     */
    public GuiSlot slot(final int index) throws IllegalArgumentException {
        verifySlotIndex(index);
        return this.slots.get(index);
    }

    /**
     * Verifies the slot index given
     *
     * @param index the index
     * @throws IllegalArgumentException the exception thrown if the slot is invalid
     * @since 1.0.0-SNAPSHOT
     */
    protected void verifySlotIndex(final int index) throws IllegalArgumentException {
        if (index < 0 && index >= this.topInventory.getSize()) {
            throw new IllegalArgumentException("The given index is not within the bounds %s to %s".formatted(0, this.topInventory.getSize()));
        }
    }

    /**
     * Obtains the size of this inventory
     *
     * @return the inventory size
     */
    protected int size() {
        return this.topInventory.getSize();
    }

    protected void regenerateEmptySlots() {
        this.slots.clear();
        for (int i = 0; i < this.topInventory.getSize(); i++) {
            this.slots.add(new DummyGuiSlot(this.topInventory, i));
        }
    }

    /**
     * Gets the top inventory of this player gui
     * <p>
     * This method SHOULD NEVER BE USED!!! Are you using this method? You are doing something wrong! Please instead use
     * {@link #slot(int)} and {@link #slot(int, Function)}! Need to know the size of this gui? {@link #size()}. If you
     * see a method you think you need make a PR or even just an issue!!!! this method could be randomly renamed or
     * moved / scope changed in the future!
     *
     * @return a dangerous inventory
     * @since 1.0.0-SNAPSHOT
     * @deprecated could be randomly renamed or moved in the future, this method is always on the chopping block for
     * removal
     */
    @Deprecated(forRemoval = true)
    @ApiStatus.Internal
    public Inventory topInventory() {
        return this.topInventory;
    }

    /**
     * Handles the click event for this inventory
     *
     * @param event the event
     */
    public void handleClick(@NotNull final InventoryClickEvent event) {
        if (event.getClickedInventory() != null && event.getClickedInventory().equals(topInventory)) {
            final var slot = event.getSlot();
            this.slots.get(slot).click(event);
        }
    }

    /**
     * Handles the drag event for this inventory
     *
     * @param event the event
     */
    public void handleDrag(@NotNull final InventoryDragEvent event) {
        if (event.getInventory().equals(topInventory)) {
            for (final Integer slot : event.getInventorySlots()) {
                this.slots.get(slot).drag(event);
            }
        }
    }

    /**
     * Handles the open event for this inventory
     *
     * @param event the event
     */
    public void handleOpen(@NotNull final InventoryOpenEvent event) {
    }

    /**
     * Handles the close event for this inventory
     *
     * @param event the event
     */
    public void handleClose(@NotNull final InventoryCloseEvent event) {
        PineappleLib.getGuiManager().unregister(this.topInventory);
    }

    /**
     * Handles decorating the gui before it is opened
     *
     * @since 1.0.0-SNAPSHOT
     */
    public abstract void decorate();
}
