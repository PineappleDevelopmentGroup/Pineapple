package sh.miles.pineapple.nms.api.menu.scene.custom;

import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Provides a variety of methods that can be listened to when dealing with Custom Menu Slots
 *
 * @since 1.0.0-SNAPSHOT
 */
@ApiStatus.OverrideOnly
public interface CustomSlotListener {

    CustomSlotListener EMPTY = new CustomSlotListener() {
    };

    /**
     * Triggers when the internal onSlotChange method is called
     *
     * @param menuContext the menu context
     * @param slotContext the slot context
     * @since 1.0.0-SNAPSHOT
     */
    default void onSlotChange(@NotNull final CustomMenuContext menuContext, @NotNull final CustomMenuSlot slotContext) {
    }


    /**
     * Triggers when the internal check achievements method is called
     *
     * @param menuContext the menu context
     * @param slotContext the slot context
     * @param item        the given item that is going to be checked
     * @since 1.0.0-SNAPSHOT
     */
    default void onCheckAchievements(@NotNull final CustomMenuContext menuContext, @NotNull final CustomMenuSlot slotContext, @NotNull final ItemStack item) {
    }

    /**
     * Triggers when the internal onTake method is triggered
     *
     * @param menuContext the menu context
     * @param slotContext the slot context
     * @param player      the player who is taking the item
     * @param item        the item being taken
     * @return whether or not to allow the event to go through
     * @since 1.0.0-SNAPSHOT
     */
    default boolean onTakeItem(@NotNull final CustomMenuContext menuContext, @NotNull final CustomMenuSlot slotContext, @NotNull final HumanEntity player, @NotNull final ItemStack item) {
        return true;
    }

    /**
     * Triggers when the internal setByPlayer method is triggered
     * <p>
     * This method is an immediate precursor to {@link #onSetItem(CustomMenuContext, CustomMenuSlot, ItemStack)} and if
     * the return value is true the {@link #onSetItem(CustomMenuContext, CustomMenuSlot, ItemStack)} method is triggered
     * immediately after this
     *
     * @param menuContext the menu context
     * @param slotContext the slot context
     * @param item        the item being set
     * @return true if the item should be placed, and the menu updated
     * @since 1.0.0-SNAPSHOT
     */
    default boolean onSetItemByPlayer(@NotNull final CustomMenuContext menuContext, @NotNull final CustomMenuSlot slotContext, @NotNull final ItemStack item) {
        return true;
    }

    /**
     * Triggers when the internal set method is triggered
     *
     * @param menuContext the menu context
     * @param slotContext the slot context
     * @param item        the item being set
     * @return true if the item should be placed, and the menu updated
     * @since 1.0.0-SNAPSHOT
     */
    default boolean onSetItem(@NotNull final CustomMenuContext menuContext, @NotNull final CustomMenuSlot slotContext, @NotNull final ItemStack item) {
        return true;
    }

    /**
     * Triggers when the internal mayPlace method is triggered
     *
     * @param menuContext the menu context
     * @param slotContext the slot context
     * @param item        the item that is attempting to be placed
     * @return true if the item can be placed, otherwise false
     * @since 1.0.0-SNAPSHOT
     */
    default boolean dictateMayPlaceItem(@NotNull final CustomMenuContext menuContext, @NotNull final CustomMenuSlot slotContext, @NotNull final ItemStack item) {
        return true;
    }

    /**
     * Triggers when the internal mayPickup method is triggered
     *
     * @param menuContext the menu context
     * @param slotContext the slot context
     * @param player      the player
     * @return true if the item can be picked up, otherwise false
     * @since 1.0.0-SNAPSHOT
     */
    default boolean dictateMayPickupItem(@NotNull final CustomMenuContext menuContext, @NotNull final CustomMenuSlot slotContext, @NotNull final HumanEntity player) {
        return true;
    }

    /**
     * Triggers when the internal allowModification method is triggered
     *
     * @param menuContext the menu context
     * @param slotContext the slot context
     * @param player      the player
     * @return true if the item can be modified, otherwise false
     * @since 1.0.0-SNAPSHOT
     */
    default boolean dictateAllowSlotModification(@NotNull final CustomMenuContext menuContext, @NotNull final CustomMenuSlot slotContext, @NotNull final HumanEntity player) {
        return dictateMayPickupItem(menuContext, slotContext, player) && dictateMayPlaceItem(menuContext, slotContext, slotContext.getBukkitItem());
    }
}
