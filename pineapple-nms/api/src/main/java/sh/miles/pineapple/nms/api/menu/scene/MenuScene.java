package sh.miles.pineapple.nms.api.menu.scene;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

/**
 * Similar in function to {@link InventoryView}, all children provide useful functions for changing data that should
 * otherwise be managed within the InventoryView
 *
 * @since 1.0.0-SNAPSHOT
 */
public interface MenuScene {

    /**
     * @return the base component title of the scene
     * @since 1.0.0-SNAPSHOT
     */
    BaseComponent getTitle();

    /**
     * Note: Using this will cause de-sync with Bukkit's getTitle method
     *
     * @param title sets the component title of the scene
     * @since 1.0.0-SNAPSHOT
     */
    void setTitle(BaseComponent title);

    /**
     * Gets the bukkit view this ContainerScene is based upon
     *
     * @return the inventory view
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    InventoryView getBukkitView();

    /**
     * Opens the scene for the viewer
     *
     * @since 1.0.0-SNAPSHOT
     */
    void open();
}
