package sh.miles.pineapple.nms.api;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Pineapple NMS adapts a bunch of useful stuffs from NMS so that we can use it. Don't tell MD_5 he won't be happy.
 *
 * @since 1.0.0-SNAPSHOT
 */
public interface PineappleNMS {

    /**
     * Gets the PineappleUnsafe class
     *
     * @return the PineappleUnsafe class
     * @since 1.0.0-SNAPSHOT
     */
    @ApiStatus.Internal
    @NotNull PineappleUnsafe getUnsafe();

}
