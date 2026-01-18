package sh.miles.pineapple.api.multiblock.util;

import java.util.UUID;

public record WorldMultiblockBounds3d(UUID uuid, int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
    public boolean overlaps(WorldMultiblockBounds3d other) {
        if (!uuid.equals(other.uuid)) return false;
        return minX <= other.maxX && maxX >= other.minX &&
                minY <= other.maxY && maxY >= other.minY &&
                minZ <= other.maxZ && maxZ >= other.minZ;
    }
}
