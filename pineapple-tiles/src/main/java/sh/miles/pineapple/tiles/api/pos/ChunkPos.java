package sh.miles.pineapple.tiles.api.pos;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.UUID;

/**
 * Represents a chunk position within the world
 *
 * @param data the chunk position data in long array form, world uuid sig bits, world uuid least sig bit, x and z
 *             location
 * @since 1.0.0-SNAPSHOT
 */
public record ChunkPos(@NotNull long[] data) {

    /**
     * Creates a ChunkPos from a easy set of parameters
     *
     * @param world the world
     * @param x     the chunk x
     * @param z     the chunk z
     */
    public ChunkPos(@NotNull final UUID world, final int x, final int z) {
        this(new long[]{world.getMostSignificantBits(), world.getLeastSignificantBits(), (((long) x) << 32) | z & 0xFFFFFFFFL});
    }

    /**
     * Converts this given ChunkPos into a bukkit Chunk
     *
     * @return the bukkit chunk
     */
    @NotNull
    public Chunk toChunk() {
        final int x = (int) (data[2] >> 32);
        final int z = (int) data[2];
        return Bukkit.getWorld(new UUID(data[0], data[1])).getChunkAt(x, z);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof final ChunkPos chunkPos)) {
            return false;
        }
        return Arrays.equals(data, chunkPos.data);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }

    /**
     * Creates a ChunkPos from the given chunk
     *
     * @param chunk the chunk to use
     * @return the resulting ChunkPos
     */
    @NotNull
    public static ChunkPos fromChunk(@NotNull final Chunk chunk) {
        return new ChunkPos(chunk.getWorld().getUID(), chunk.getX(), chunk.getZ());
    }
}
