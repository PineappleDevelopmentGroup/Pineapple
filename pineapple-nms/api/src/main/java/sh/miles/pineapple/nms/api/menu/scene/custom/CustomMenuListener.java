package sh.miles.pineapple.nms.api.menu.scene.custom;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.nms.api.menu.scene.custom.CustomMenuContext.MergeResult;

/**
 * Gives a variety of methods that can be listened to react to Menu changes
 */
@ApiStatus.OverrideOnly
public interface CustomMenuListener {

    /**
     * Handles item shift click behavior. Usually this transaction is occurring between the player inventory and the
     * open inventory
     *
     * @param context the menu context
     * @param player  the player
     * @param rawSlot the destination raw slot index
     * @return the given quick move result
     */
    default QuickMoveResult quickMoveItem(@NotNull final CustomMenuContext context, @NotNull final HumanEntity player, final int rawSlot) {
        ItemStack result = new ItemStack(Material.AIR);
        final CustomMenuSlot slot = context.getMenuSlot(rawSlot);

        if (slot != null && slot.hasBukkitItem()) {
            ItemStack slotItem = slot.getBukkitItem();
            result = slotItem.clone();
            MergeResult mergeResult;
            if (rawSlot < context.getRowAmount() * 9) {
                mergeResult = context.mergeItemStackBetween(slotItem, context.getRowAmount() * 9, context.getSlotAmount(), true);
                if (!mergeResult.result()) {
                    return QuickMoveResult.cancel();
                }

                slotItem = mergeResult.item();
            } else {
                mergeResult = context.mergeItemStackBetween(slotItem, 0, context.getRowAmount() * 9, false);
                if (!mergeResult.result()) {
                    return QuickMoveResult.cancel();
                }
                slotItem = mergeResult.item();
            }

            if (slotItem.getType().isAir() || slotItem.getAmount() == 0) {
                slot.setItemByPlayer(new ItemStack(Material.AIR));
            } else {
                slot.setSlotChanged();
            }
        }

        return QuickMoveResult.complete(result);
    }

    /**
     * Determines the validity of the viewing players session
     *
     * @param context the menu context
     * @param player  the player
     * @return true to continue the session, false to determine the session is invalid and close the menu
     */
    default boolean dictateValidity(@NotNull final CustomMenuContext context, @NotNull final HumanEntity player) {
        return true;
    }

    /**
     * Retrieves a custom slot listener for the given slot
     * <p>
     * IT IS ABSOLUTELY NECESSARY THAT NO IMPLEMENTATION OF THIS METHOD RETURNS NULL
     *
     * @param rawSlot the raw slot
     * @return the given custom slot listener
     */
    @NotNull
    default CustomSlotListener getSlotListener(final int rawSlot) {
        return CustomSlotListener.EMPTY;
    }


    record QuickMoveResult(@NotNull ItemStack item, boolean shouldCancel, boolean shouldDelegateToDefault) {
        public static QuickMoveResult cancel() {
            return new QuickMoveResult(new ItemStack(Material.AIR), true, false);
        }

        public static QuickMoveResult delegate() {
            return new QuickMoveResult(new ItemStack(Material.AIR), false, true);
        }

        public static QuickMoveResult complete(@NotNull final ItemStack item) {
            return new QuickMoveResult(new ItemStack(item), false, false);
        }
    }

}
