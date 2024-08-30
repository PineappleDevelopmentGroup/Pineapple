package sh.miles.pineapple.nms.impl.v1_21_R1.packet;

import org.bukkit.craftbukkit.v1_21_R1.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.nms.api.packet.PineapplePackets;

public class PineapplePacketsImpl implements PineapplePackets {

    @Override
    public void broadcastTotemEffect(@NotNull final LivingEntity entity) {
        final var livingEntity = ((CraftLivingEntity) entity).getHandle();
        livingEntity.level().broadcastEntityEvent(livingEntity, (byte) 35);
    }
}
