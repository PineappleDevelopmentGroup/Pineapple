package sh.miles.pineapple.nms.impl.v1_20_R3.inventory.scene.custom;

import com.google.common.base.Preconditions;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.nms.api.menu.scene.custom.CustomMenuContext;
import sh.miles.pineapple.nms.api.menu.scene.custom.CustomMenuListener;
import sh.miles.pineapple.nms.api.menu.scene.custom.CustomMenuSlot;
import sh.miles.pineapple.nms.api.menu.scene.custom.CustomSlotListener;

public class PineappleSlot extends Slot implements CustomMenuSlot {

    private final CustomMenuContext context;
    private final CustomSlotListener listener;

    public PineappleSlot(@NotNull final CustomMenuContext context, @NotNull final CustomMenuListener listener, final Container container, final int slotIndex, final int rawIndex) {
        super(container, slotIndex, 0, 0);
        this.context = context;
        this.listener = listener.getSlotListener(rawIndex);
        Preconditions.checkState(this.listener != null, "The given slot listener must not be null");
    }

    @Override
    public void onQuickCraft(final ItemStack original, final ItemStack newStack) {
        super.onQuickCraft(original, newStack);
    }

    @Override
    protected void checkTakeAchievements(final ItemStack item) {
        this.listener.onCheckAchievements(this.context, this, mirror(item));
    }

    @Override
    public void onTake(final Player player, final ItemStack item) {
        if (this.listener.onTakeItem(this.context, this, player.getBukkitEntity(), mirror(item))) {
            super.onTake(player, item);
        }
    }

    @Override
    public boolean mayPlace(final ItemStack item) {
        return this.listener.dictateMayPlaceItem(this.context, this, mirror(item));
    }

    @Override
    public void setByPlayer(final ItemStack item) {
        if (listener.onSetItemByPlayer(this.context, this, mirror(item))) {
            super.setByPlayer(item);
        }
    }

    @Override
    public void set(final ItemStack item) {
        if (listener.onSetItem(this.context, this, mirror(item))) {
            super.set(item);
        }
    }

    @Override
    public boolean mayPickup(final Player player) {
        return listener.dictateMayPickupItem(this.context, this, player.getBukkitEntity());
    }

    @Override
    public boolean allowModification(final Player player) {
        return listener.dictateAllowSlotModification(this.context, this, player.getBukkitEntity());
    }

    @Override
    public void setChanged() {
        this.listener.onSlotChange(this.context, this);
        super.setChanged();
    }

    // Pineapple Required
    @Override
    public void onQuickCraftItem(@NotNull final org.bukkit.inventory.ItemStack originalStack, @NotNull final org.bukkit.inventory.ItemStack newStack) {
        this.onQuickCraft(convert(originalStack), convert(newStack));
    }

    @Override
    public void checkForAchievements(@NotNull final org.bukkit.inventory.ItemStack item) {
        this.checkTakeAchievements(convert(item));
    }

    @Override
    public void onTakeItem(@NotNull final HumanEntity player, @NotNull final org.bukkit.inventory.ItemStack item) {
        this.onTake(((CraftHumanEntity) player).getHandle(), convert(item));
    }

    @Override
    public boolean mayPlaceItem(@NotNull final org.bukkit.inventory.ItemStack item) {
        return this.mayPlace(convert(item));
    }

    @Override
    public void setItemByPlayer(@NotNull final org.bukkit.inventory.ItemStack item) {
        this.setByPlayer(convert(item));
    }

    @Override
    public void setItem(@NotNull final org.bukkit.inventory.ItemStack item) {
        this.set(convert(item));
    }

    @Override
    public boolean mayPickupItem(@NotNull final HumanEntity player) {
        return this.mayPickup(((CraftHumanEntity) player).getHandle());
    }

    @Override
    public boolean allowSlotModification(@NotNull final HumanEntity player) {
        return this.allowModification(((CraftHumanEntity) player).getHandle());
    }

    @Override
    public void setSlotChanged() {
        super.setChanged();
    }

    @NotNull
    @Override
    public org.bukkit.inventory.ItemStack getBukkitItem() {
        return mirror(getItem());
    }

    @Override
    public boolean hasBukkitItem() {
        return super.hasItem();
    }

    private static CraftItemStack mirror(ItemStack item) {
        return CraftItemStack.asCraftMirror(item);
    }

    private static ItemStack convert(@NotNull final org.bukkit.inventory.ItemStack item) {
        return CraftItemStack.asNMSCopy(item);
    }
}
