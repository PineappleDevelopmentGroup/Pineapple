package sh.miles.pineapple.nms.api.menu.scene.custom;

import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a custom slot within a menu.
 *
 * @since 1.0.0-SNAPSHOT
 */
@ApiStatus.NonExtendable
public interface CustomMenuSlot {

    /**
     * Quick Crafts together a old and new item
     *
     * @param originalStack the original stack
     * @param newStack      the new stack
     * @since 1.0.0-SNAPSHOT
     */
    void onQuickCraftItem(@NotNull final ItemStack originalStack, @NotNull final ItemStack newStack);

    /**
     * A Function used to check for achievements
     *
     * @param item the item
     * @since 1.0.0-SNAPSHOT
     */
    void checkForAchievements(@NotNull final ItemStack item);

    /**
     * Executed when a player takes an item
     *
     * @param player the player
     * @param item   the item
     * @since 1.0.0-SNAPSHOT
     */
    void onTakeItem(@NotNull final HumanEntity player, @NotNull final ItemStack item);

    /**
     * Checks whether or not the item may be placed in this slot
     *
     * @param item the item to place
     * @return true if the item can be placed, otherwise false
     * @since 1.0.0-SNAPSHOT
     */
    boolean mayPlaceItem(@NotNull final ItemStack item);

    /**
     * Determines whether or not this slot has an item
     *
     * @return true if the slot has an item, otherwise false
     * @since 1.0.0-SNAPSHOT
     */
    boolean hasBukkitItem();

    /**
     * Sets the item in the slot via a player. This method is only triggered when a player places the item
     *
     * @param item the item to place
     * @since 1.0.0-SNAPSHOT
     */
    void setItemByPlayer(@NotNull final ItemStack item);

    /**
     * Sets an item in the slot.
     *
     * @param item the item to place
     * @since 1.0.0-SNAPSHOT
     */
    void setItem(@NotNull final ItemStack item);

    /**
     * Marks the slot as changed
     *
     * @since 1.0.0-SNAPSHOT
     */
    void setSlotChanged();

    /**
     * Determines whether or not a player can pick up an item
     *
     * @param player the player
     * @return true if the player may pickup an item, otherwise false
     * @since 1.0.0-SNAPSHOT
     */
    boolean mayPickupItem(@NotNull final HumanEntity player);

    /**
     * Determines whether or not the given player may modify this slot
     *
     * @param player the player
     * @return true if modification is allowed, otherwise false
     * @since 1.0.0-SNAPSHOT
     */
    boolean allowSlotModification(@NotNull final HumanEntity player);

    /**
     * Gets the CraftBukkit item from this slot
     *
     * @return the CraftBukkit item
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    ItemStack getBukkitItem();
}
