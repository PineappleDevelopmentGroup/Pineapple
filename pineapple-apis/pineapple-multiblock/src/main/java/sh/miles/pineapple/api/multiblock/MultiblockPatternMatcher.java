package sh.miles.pineapple.api.multiblock;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import sh.miles.pineapple.api.multiblock.util.MultiblockBounds3d;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Handles the logic for detecting multiblock structures in the world.
 * <p>
 * Implementations of this interface are responsible for efficient spatial queries to determine if a set of blocks in
 * the world matches a known {@link MultiblockPattern}.
 *
 * @since 1.0.0-SNAPSHOT
 */
@NullMarked
public final class MultiblockPatternMatcher {

    private final MultiblockPatterns manager;

    public MultiblockPatternMatcher(final MultiblockPatterns manager) {
        this.manager = manager;
    }

    /**
     * Attempts to find a matching multiblock structure anchored at the given block.
     * <p>
     * Implementations should perform necessary checks (such as bounding box validation, chunk loading safety, and block
     * state matching) to verify the structure.
     *
     * @param anchor the block acting as the anchor (usually the interaction point)
     * @return a Result containing the pattern group and specific rotation found, or null if none
     * @since 1.0.0-SNAPSHOT
     */
    @Nullable
    public Result match(Block anchor) {
        final Material anchorType = anchor.getType();
        final List<MultiblockPatternGroup> potentialMatches = manager.getByAnchor(anchorType);
        if (potentialMatches.isEmpty()) {
            return null;
        }

        final World world = anchor.getWorld();
        final int originX = anchor.getX();
        final int originY = anchor.getY();
        final int originZ = anchor.getZ();

        for (final MultiblockPatternGroup group : potentialMatches) {
            MultiblockPattern match = checkMatch(world, originX, originY, originZ, group);
            if (match != null) {
                return new Result(group, match);
            }
        }

        return null;
    }

    @Nullable
    private MultiblockPattern checkMatch(World world, int originX, int originY, int originZ, MultiblockPatternGroup group) {
        if (!group.anchor().contains(world.getBlockData(originX, originY, originZ))) {
            return null;
        }

        final List<MultiblockPattern> candidates = new ArrayList<>(group.patterns());

        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE, maxZ = Integer.MIN_VALUE;

        for (MultiblockPattern p : candidates) {
            MultiblockBounds3d b = p.bounds();
            if (b.minX() < minX) {
                minX = b.minX();
            }
            if (b.minY() < minY) {
                minY = b.minY();
            }
            if (b.minZ() < minZ) {
                minZ = b.minZ();
            }
            if (b.maxX() > maxX) {
                maxX = b.maxX();
            }
            if (b.maxY() > maxY) {
                maxY = b.maxY();
            }
            if (b.maxZ() > maxZ) {
                maxZ = b.maxZ();
            }
        }

        int wMinX = originX + minX;
        int wMinZ = originZ + minZ;
        int wMaxX = originX + maxX;
        int wMaxZ = originZ + maxZ;

        if (!world.isChunkLoaded(wMinX >> 4, wMinZ >> 4) || !world.isChunkLoaded(wMaxX >> 4, wMaxZ >> 4)) {
            return null;
        }

        int worldX, worldZ, worldY;
        BlockData worldBlock;

        for (int x = minX; x <= maxX; x++) {
            worldX = originX + x;
            for (int z = minZ; z <= maxZ; z++) {
                worldZ = originZ + z;
                for (int y = minY; y <= maxY; y++) {
                    worldY = originY + y;

                    worldBlock = null;

                    Iterator<MultiblockPattern> iterator = candidates.iterator();
                    while (iterator.hasNext()) {
                        MultiblockPattern pattern = iterator.next();

                        if (!pattern.bounds().contains(x, y, z)) {
                            continue;
                        }

                        if (worldBlock == null) {
                            worldBlock = world.getBlockData(worldX, worldY, worldZ);
                        }

                        if (!pattern.blockDataAt(x, y, z).matches(worldBlock)) {
                            iterator.remove();
                        }
                    }

                    if (candidates.isEmpty()) {
                        return null;
                    }
                }
            }
        }

        return candidates.isEmpty() ? null : candidates.getFirst();
    }

    /**
     * Represents the result of a successful multiblock match.
     * <p>
     * Implementations can be simple records (e.g. {@code record SimpleResult(...) implements Result}).
     *
     * @param group            the group definition
     * @param specificRotation the rotation within that gruop
     * @since 1.0.0-SNAPSHOT
     */
    @NullMarked
    public record Result(MultiblockPatternGroup group, MultiblockPattern specificRotation) {
    }
}
