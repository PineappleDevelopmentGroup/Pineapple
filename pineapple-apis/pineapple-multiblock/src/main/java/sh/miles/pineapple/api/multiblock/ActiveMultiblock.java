package sh.miles.pineapple.api.multiblock;

import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import sh.miles.pineapple.api.multiblock.util.WorldMultiblockBounds3d;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a live, instantiated multiblock structure in the world.
 * <p>
 * This interface defines the contract for accessing runtime state (custom data), physical bounds, and the link to the
 * underlying {@link MultiblockPattern}.
 *
 * @since 1.0.0-SNAPSHOT
 */
@NullMarked
public final class ActiveMultiblock {

    private final MultiblockPattern pattern;
    private final Location anchor;
    private final WorldMultiblockBounds3d bounds;
    private final List<Vector> constituentLocations;
    private final Map<Key, Object> customData;
    boolean dirty;
    boolean onDisk;

    /**
     * Creates a new ActiveMultiblock instance.
     *
     * @param pattern the pattern definition
     * @param anchor  the anchor location (pivot point)
     * @since 1.0.0-SNAPSHOT
     */
    public ActiveMultiblock(MultiblockPattern pattern, Location anchor) {
        this.pattern = Objects.requireNonNull(pattern, "pattern cannot be null");
        this.anchor = Objects.requireNonNull(anchor, "anchor cannot be null");
        this.bounds = pattern.bounds().relative(anchor);
        this.constituentLocations = List.copyOf(
            pattern.structure(anchor.getBlockX(), anchor.getBlockY(), anchor.getBlockZ()));
        this.customData = new HashMap<>();
    }

    /**
     * Gets the pattern definition for this structure.
     *
     * @return the pattern
     * @since 1.0.0-SNAPSHOT
     */
    public MultiblockPattern pattern() {
        return this.pattern;
    }

    /**
     * Gets the anchor location (pivot point) of this structure.
     *
     * @return a clone of the anchor location
     * @since 1.0.0-SNAPSHOT
     */
    public Location anchor() {
        return this.anchor.clone();
    }

    /**
     * The WorldBounds of this multiblock instance.
     * <p>
     * These bounds represent the absolute world coordinates calculated from the pattern's relative bounds and the
     * anchor point.
     *
     * @return world bounds
     * @since 1.0.0-SNAPSHOT
     */
    public WorldMultiblockBounds3d bounds() {
        return this.bounds;
    }

    /**
     * A list of all coordinate constituents of this structure.
     * <p>
     * These are the absolute world vectors for every block that makes up this structure.
     *
     * @return the constituents
     * @since 1.0.0-SNAPSHOT
     */
    public List<Vector> constituents() {
        return this.constituentLocations;
    }

    /**
     * Retrieves runtime data attached to this structure.
     *
     * @param key      the data key
     * @param dataType the expected class of the data
     * @param <T>      the type of the data
     * @return the data, or null if not present
     * @throws IllegalArgumentException if the data exists but is not of the expected type
     * @since 1.0.0-SNAPSHOT
     */
    public @Nullable <T> T getData(final Key key, final Class<T> dataType) {
        final var data = customData.get(key);
        if (data == null) {
            return null;
        }

        if (!dataType.isInstance(data)) {
            throw new IllegalArgumentException("data of class " + data.getClass()
                .getName() + " is not compatible with requested type " + dataType.getName());
        }

        return (T) data;
    }

    /**
     * Attaches runtime data to this structure.
     * <p>
     * Calling this method should automatically mark the structure as "dirty" to ensure data persistence on the next
     * save cycle.
     *
     * @param key  the data key
     * @param data the data object
     * @since 1.0.0-SNAPSHOT
     */
    public void putData(final Key key, final Object data) {
        customData.put(key, data);
        markDirty();
    }

    /**
     * Removes runtime data from this structure.
     * <p>
     * Calling this method should automatically mark the structure as "dirty".
     *
     * @param key      the data key
     * @param dataType the expected class of the data
     * @param <T>      the type of the data
     * @return the removed data, or null if not present
     * @throws IllegalArgumentException if the data exists but is not of the expected type
     * @since 1.0.0-SNAPSHOT
     */
    public @Nullable <T> T removeData(final Key key, final Class<T> dataType) {
        final var data = customData.get(key);
        if (data == null) {
            return null;
        }

        if (!dataType.isInstance(data)) {
            throw new IllegalArgumentException("data of class " + data.getClass()
                .getName() + " is not compatible with requested type " + dataType.getName());
        }

        customData.remove(key);
        markDirty();
        return (T) data;
    }

    /**
     * Explicitly marks this multiblock as dirty.
     * <p>
     * This is useful if you retrieve a mutable object via {@link #getData(Key, Class)} and modify its internal state
     * without calling {@link #putData(Key, Object)}.
     *
     * @since 1.0.0-SNAPSHOT
     */
    public void markDirty() {
        this.dirty = true;
    }

    /**
     * Creates a new instance of an {@link ActiveMultiblock} backed by the current implementation.
     *
     * @param pattern The {@link MultiblockPattern} definition that dictates the structure and behavior of this
     *                multiblock. Must not be null.
     * @param anchor  The specific {@link Location} in the world serving as the reference point (usually the
     *                bottom-center or interaction point) for this instance.
     * @return A new, initialized {@link ActiveMultiblock} instance ready for validation or processing.
     * @since 1.0.0-SNAPSHOT
     */
    public static ActiveMultiblock activeMultiblock(MultiblockPattern pattern, Location anchor) {
        return new ActiveMultiblock(pattern, anchor);
    }
}
