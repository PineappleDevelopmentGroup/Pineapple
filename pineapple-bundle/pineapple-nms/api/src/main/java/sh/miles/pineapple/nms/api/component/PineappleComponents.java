package sh.miles.pineapple.nms.api.component;

import org.jetbrains.annotations.NotNull;

/**
 * All Pineapple compatibility for components
 *
 * @since 1.0.0-SNAPSHOT
 */
public interface PineappleComponents {

    /**
     * Gets the {@link ItemComponents} wrapper for Items
     *
     * @return the components
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    ItemComponents forItems();

}
