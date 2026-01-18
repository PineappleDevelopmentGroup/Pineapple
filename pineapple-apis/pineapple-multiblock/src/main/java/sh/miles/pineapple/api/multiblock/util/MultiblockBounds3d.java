package sh.miles.pineapple.api.multiblock.util;

import org.bukkit.Location;

public record MultiblockBounds3d(int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {

    /**
     * Converts these relative bounds into absolute world bounds based on the provided origin location.
     *
     * @param origin the location representing (0,0,0) relative to these bounds
     * @return a new WorldBounds3d instance
     * @since 1.0.0-SNAPSHOT
     */
    public WorldMultiblockBounds3d relative(Location origin) {
        if (origin.getWorld() == null) {
            throw new IllegalArgumentException("Cannot create WorldBounds3d from a location without a world.");
        }

        final int x = origin.getBlockX();
        final int y = origin.getBlockY();
        final int z = origin.getBlockZ();

        return new WorldMultiblockBounds3d(
            origin.getWorld().getUID(),
            x + this.minX,
            x + this.maxX,
            y + this.minY,
            y + this.maxY,
            z + this.minZ,
            z + this.maxZ
        );
    }

    public MultiblockBounds3d adapt(int x, int y, int z) {
        int minX = this.minX;
        int minY = this.minY;
        int minZ = this.minZ;
        int maxX = this.maxX;
        int maxY = this.maxY;
        int maxZ = this.maxZ;

        if (x < minX) {
            minX = x;
        }
        if (x > maxX) {
            maxX = x;
        }
        if (y < minY) {
            minY = y;
        }
        if (y > maxY) {
            maxY = y;
        }
        if (z < minZ) {
            minZ = z;
        }
        if (z > maxZ) {
            maxZ = z;
        }

        return new MultiblockBounds3d(minX, maxX, minY, maxY, minZ, maxZ);
    }

    public boolean contains(int x, int y, int z) {
        return (x <= maxX && x >= minX) && (y <= maxY && y >= minY) && (z <= maxZ && z >= minZ);
    }

    public boolean valid() {
        return minX <= maxX && minY <= maxY && minZ <= maxZ;
    }
}
