package sh.miles.pineapple.gui.slot;

/**
 * Marks a slot that can be deployed using the deploy method.
 *
 * @since 1.0.0-SNAPSHOT
 */
public interface Deployable {

    /**
     * Deploys the stored ItemStack to the slot.
     *
     * @since 1.0.0-SNAPSHOT
     */
    void deploy();
}
