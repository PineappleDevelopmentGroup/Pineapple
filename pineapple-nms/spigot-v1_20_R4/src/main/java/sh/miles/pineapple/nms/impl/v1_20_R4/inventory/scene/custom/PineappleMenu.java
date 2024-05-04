package sh.miles.pineapple.nms.impl.v1_20_R4.inventory.scene.custom;

import com.google.common.base.Preconditions;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftItemStack;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.nms.api.menu.scene.custom.CustomMenuContext;
import sh.miles.pineapple.nms.api.menu.scene.custom.CustomMenuListener;
import sh.miles.pineapple.nms.api.menu.scene.custom.CustomMenuListener.QuickMoveResult;
import sh.miles.pineapple.nms.api.menu.scene.custom.CustomMenuSlot;

public class PineappleMenu extends AbstractContainerMenu implements CustomMenuContext {

    private final CustomMenuListener menuListener;
    private InventoryView bukkitView;

    private final Container container;
    private final Inventory playerInventory;
    private final int rows;

    public PineappleMenu(@NotNull final CustomMenuListener menuListener, @NotNull final MenuType<?> menuType, final int syncId, @NotNull final Inventory playerInventory, final int rows) {
        super(menuType, syncId);
        this.menuListener = menuListener;
        this.playerInventory = playerInventory;
        this.rows = rows;
        this.container = new SimpleContainer(9 * rows);

        int rawIndex = 0;
        for (int row = 0; row < this.rows; ++row) {
            for (int column = 0; column < 9; ++column) {
                final int index = column + row * 9;
                this.addSlot(new PineappleSlot(this, menuListener, this.container, index, rawIndex++));
            }
        }

        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 9; ++column) {
                final int index = column + row * 9 + 9;
                this.addSlot(new PineappleSlot(this, menuListener, this.playerInventory, index, rawIndex++));
            }
        }

        for (int column = 0; column < 9; ++column) {
            this.addSlot(new PineappleSlot(this, menuListener, this.playerInventory, column, rawIndex++));
        }
    }

    @Override
    public InventoryView getBukkitView() {
        if (this.bukkitView == null) {
            this.bukkitView = new CraftInventoryView(playerInventory.player.getBukkitEntity(), new CraftInventory(this.container), this);
        }

        return this.bukkitView;
    }

    @Override
    public ItemStack quickMoveStack(final Player player, final int rawSlot) {
        final QuickMoveResult result = this.menuListener.quickMoveItem(this, player.getBukkitEntity(), rawSlot);

        if (result.shouldDelegateToDefault()) {
            return quickMoveStackChest(player, rawSlot);
        }

        if (result.shouldCancel()) {
            return ItemStack.EMPTY;
        }

        return CraftItemStack.asNMSCopy(result.item());
    }

    public ItemStack quickMoveStackChest(Player entityhuman, int i) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.slots.get(i);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (i < this.rows * 9) {
                if (!this.moveItemStackTo(itemstack1, this.rows * 9, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, this.rows * 9, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(final Player player) {
        return this.menuListener.dictateValidity(this, player.getBukkitEntity());
    }

    @NotNull
    @Override
    public MergeResult mergeItemStackBetween(@NotNull final org.bukkit.inventory.ItemStack item, final int startIndex, final int endIndex, final boolean reverse) {
        final ItemStack copy = CraftItemStack.asNMSCopy(item);
        boolean bool = moveItemStackTo(copy, startIndex, endIndex, reverse);
        return new MergeResult(CraftItemStack.asCraftMirror(copy), bool);
    }

    @Override
    public void slotsChanged() {
        this.slotsChanged(this.container);
    }

    @NotNull
    @Override
    public CustomMenuSlot getMenuSlot(final int rawSlot) {
        return (CustomMenuSlot) this.slots.get(rawSlot);
    }

    @NotNull
    @Override
    public org.bukkit.inventory.Inventory getBukkitContainer() {
        Preconditions.checkState(this.bukkitView != null, "The container must be opened before this container can be accessed");
        return this.bukkitView.getTopInventory();
    }

    @Override
    public int getRowAmount() {
        return this.rows;
    }

    @Override
    public int getSlotAmount() {
        return this.slots.size();
    }
}
