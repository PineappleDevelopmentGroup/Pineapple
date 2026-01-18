package sh.miles.pineapple.api.multiblock;

import org.bukkit.block.data.BlockData;
import org.bukkit.block.structure.StructureRotation;
import org.jspecify.annotations.NullMarked;
import sh.miles.pineapple.api.multiblock.MultiblockPattern.BlockRotationStrategy;
import sh.miles.pineapple.api.multiblock.util.MultiblockBounds3d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a collection containing a source {@link MultiblockPattern} and its pre-computed cardinal rotations (0, 90,
 * 180, 270 degrees).
 * <p>
 * This group is primarily used to match structures in the world regardless of their orientation, or to efficiently
 * query properties across all valid states without recalculating rotation logic at runtime.
 *
 * @since 1.0.0-SNAPSHOT
 */
@NullMarked
public final class MultiblockPatternGroup {

    private final MultiblockPattern source;
    private final MultiblockPattern[] rotations;
    private final Set<BlockData> anchors;

    private MultiblockPatternGroup(final MultiblockPattern[] rotations) {
        this.rotations = Arrays.copyOf(rotations, rotations.length);
        this.source = rotations[0];
        this.anchors = Arrays.stream(rotations).map(MultiblockPattern::anchor).collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Gets the source of the pre-computed types.
     *
     * @return the source pattern
     * @since 1.0.0-SNAPSHOT
     */
    public MultiblockPattern source() {
        return this.source;
    }

    /**
     * Gets the rotation at the specified index no greater than the maximum ordinal in {@link StructureRotation}.
     *
     * @param rotation the rotation index
     * @return the pattern at that rotation index
     * @throws IndexOutOfBoundsException if the index is invalid
     * @since 1.0.0-SNAPSHOT
     */
    public MultiblockPattern rotation(int rotation) {
        return this.rotations[rotation];
    }

    /**
     * Gets a specific pattern from a rotation.
     *
     * @param rotation the rotation
     * @return the pattern corresponding to the rotation
     * @since 1.0.0-SNAPSHOT
     */
    public MultiblockPattern rotation(StructureRotation rotation) {
        return this.rotations[rotation.ordinal()];
    }

    /**
     * Gets a list of all patterns in this group.
     *
     * @return all patterns
     * @since 1.0.0-SNAPSHOT
     */
    public List<MultiblockPattern> patterns() {
        return Arrays.asList(this.rotations);
    }

    /**
     * Gets the unique identifier of the source pattern.
     *
     * @return the id string
     * @since 1.0.0-SNAPSHOT
     */
    public String id() {
        return source.id();
    }

    /**
     * Gets the set of all unique anchor block data states across all rotations.
     * <p>
     * For example, if the anchor is a stair block, this set will typically contain the stairs facing North, East,
     * South, and West.
     *
     * @return an unmodifiable set of anchor block data
     * @since 1.0.0-SNAPSHOT
     */
    public Set<BlockData> anchor() {
        return this.anchors;
    }

    /**
     * Gets the bounds of the source (un-rotated) pattern.
     * <p>
     * Note: Rotated variants may have different absolute bounds depending on the shape of the structure, but this
     * returns the canonical bounds.
     *
     * @return the 3d bounds of the source pattern
     * @since 1.0.0-SNAPSHOT
     */
    public MultiblockBounds3d bounds() {
        return this.source.bounds();
    }

    /**
     * The source origin's x offset, typically zero.
     *
     * @return x
     * @since 1.0.0-SNAPSHOT
     */
    public int originX() {
        return this.source.originX();
    }

    /**
     * The source origin's y offset, typically zero.
     *
     * @return y
     * @since 1.0.0-SNAPSHOT
     */
    public int originY() {
        return this.source.originY();
    }

    /**
     * The source origin's z offset, typically zero.
     *
     * @return z
     * @since 1.0.0-SNAPSHOT
     */
    public int originZ() {
        return this.source.originZ();
    }

    /**
     * Retrieves the block data at the specified offset for all 4 cardinal rotations.
     * <p>
     * The list order corresponds to the ordinal order of {@link StructureRotation}:
     * <ol>
     * <li>{@link StructureRotation#NONE}</li>
     * <li>{@link StructureRotation#CLOCKWISE_90}</li>
     * <li>{@link StructureRotation#CLOCKWISE_180}</li>
     * <li>{@link StructureRotation#COUNTERCLOCKWISE_90}</li>
     * </ol>
     *
     * @param offX the x offset relative to the origin
     * @param offY the y offset relative to the origin
     * @param offZ the z offset relative to the origin
     * @return a list containing the block data at this offset for each rotation
     * @since 1.0.0-SNAPSHOT
     */
    public List<BlockData> blockDataAt(int offX, int offY, int offZ) {
        final List<BlockData> list = new ArrayList<>(4);
        for (final MultiblockPattern rotation : rotations) {
            list.add(rotation.blockDataAt(offX, offY, offZ));
        }

        return list;
    }

    /**
     * Creates a new {@link MultiblockPatternGroup} with the bukkit rotation strategy
     *
     * @param pattern the pattern to create a group from
     * @return a new {@link MultiblockPatternGroup}
     * @since 1.0.0-SNAPSHOT
     */
    public static MultiblockPatternGroup from(MultiblockPattern pattern) {
        return from(pattern, BlockRotationStrategy.BUKKIT);
    }

    /**
     * Creates a new {@link MultiblockPattern} group given a strategy
     *
     * @param pattern  the pattern
     * @param strategy the rotation strategy, typically only needed with MockBukkit
     * @return a new {@link MultiblockPatternGroup}
     * @since 1.0.0-SNAPSHOT
     */
    public static MultiblockPatternGroup from(MultiblockPattern pattern, BlockRotationStrategy strategy) {
        MultiblockPattern[] patterns = new MultiblockPattern[4];
        StructureRotation[] rots = StructureRotation.values();
        for (int i = 0; i < rots.length; i++) {
            patterns[i] = pattern.rotate(rots[i], strategy);
        }

        return new MultiblockPatternGroup(patterns);
    }

}
