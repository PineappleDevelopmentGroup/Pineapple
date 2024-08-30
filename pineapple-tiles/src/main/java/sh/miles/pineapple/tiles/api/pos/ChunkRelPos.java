package sh.miles.pineapple.tiles.api.pos;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a relative position within a chunk.
 * <p>
 * Chunk relative positions start at 0 and end at 15. The y value of a relative position stays unchanged.
 *
 * @param data the combined relative position data
 * @since 1.0.0-SNAPSHOT
 */
public record ChunkRelPos(long data) {
    /**
     * An easier to use constructor for creating a ChunkRelPos object
     *
     * @param relx the relative x location, between 0 and 15
     * @param y    the y location
     * @param relz the relative z location, between 0 and 15
     * @since 1.0.0-SNAPSHOT
     */
    public ChunkRelPos(final int relx, final int y, final int relz) {
        this((byte) relx, y, (byte) relz);
    }

    /**
     * An easier to use constructor for creating a ChunkRelPos object
     *
     * @param relx the relative x location, between 0 and 15
     * @param y    the y location
     * @param relz the relative z location, between 0 and 15
     * @since 1.0.0-SNAPSHOT
     */
    public ChunkRelPos(final byte relx, final int y, final int relz) {
        this((((long) relx << 56) & 0xFF0000000000000L) | (((long) relz << 48) & 0x00FF000000000000L) | (((long) y) & 0x0000000FFFFFFFFL));
    }

    /**
     * formats the data of this ChunkRelPos into a string
     *
     * @return the string
     */
    @NotNull
    public String asLongString() {
        return "" + this.data;
    }

    @Override
    public String toString() {
        return "ChunkRelPos[relx=%d,y=%d,relz=%d]".formatted((data >> 56) & 0xFF, (int) data, (data >> 48) & 0xFF);
    }

    @NotNull
    public static ChunkRelPos fromLocation(@NotNull final Location location) {
        return new ChunkRelPos(location.getBlockX() & 0xF, location.getBlockY(), location.getBlockZ() & 0xF);
    }
}
