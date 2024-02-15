package sh.miles.pineapple.nms.api.packet;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Handles packet related NMS Api
 *
 * @since 1.0.0-SNAPSHOT
 */
public interface PineapplePackets {

    /**
     * Broadcasts the totem effect to the entity and surrounding entities
     *
     * @param entity the entity to trigger the effect
     */
    void broadcastTotemEffect(@NotNull final LivingEntity entity);

}
