package sh.miles.pineapple.nms.api.menu.scene;

/**
 * A Scene which provides data on an Anvil
 *
 * @since 1.0.0-SNAPSHOT
 */
public interface AnvilScene extends MenuScene {

    /**
     * @return the entered text within the text field
     * @since 1.0.0-SNAPSHOT
     */
    String getText();

    /**
     * Note: using this method renames the input item
     *
     * @param text the text to set within the text field
     * @since 1.0.0-SNAPSHOT
     */
    void setText(String text);

    /**
     * @return the experience cost to repair
     * @since 1.0.0-SNAPSHOT
     */
    int getRepairCost();

    /**
     * @param cost the experience to make repair cost
     * @since 1.0.0-SNAPSHOT
     */
    void setRepairCost(int cost);

    /**
     * @return the amount of items needed to repair
     * @since 1.0.0-SNAPSHOT
     */
    int getRepairItemCost();

    /**
     * @param amountCost sets amount of items needed to repair
     * @since 1.0.0-SNAPSHOT
     */
    void setRepairItemCost(int amountCost);
}
