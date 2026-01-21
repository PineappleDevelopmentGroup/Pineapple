package sh.miles.pineapple.api.multiblock;

import it.unimi.dsi.fastutil.chars.Char2ByteOpenHashMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ByteMap;
import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
import org.bukkit.World;
import org.bukkit.block.BlockType;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.util.Vector;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import sh.miles.pineapple.ByteUtils;
import sh.miles.pineapple.ByteUtils.Component3;
import sh.miles.pineapple.api.multiblock.util.MultiblockBounds3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A MultiBlock pattern is an in-memory pattern for a multiblock structure.
 * <p>
 * Manages paletting and various other utility methods.
 *
 * @since 1.0.0-SNAPSHOT
 */
@NullMarked
public final class MultiblockPattern {

    private final String id;
    private final BlockData[] palette;
    private final BlockData anchor;
    // packed coordinate -> palette index
    private final Long2ByteOpenHashMap structure;
    private final MultiblockBounds3d bounds;
    private final int originX, originY, originZ, rotation;

    private MultiblockPattern(String id, BlockData[] palette, BlockData anchor, Long2ByteOpenHashMap structure, MultiblockBounds3d bounds, int x, int y, int z, int rotation) {
        this.id = id;
        this.palette = palette;
        this.anchor = anchor;
        this.structure = structure;
        this.bounds = bounds;
        this.originX = x;
        this.originY = y;
        this.originZ = z;
        this.rotation = rotation;
    }

    /**
     * Gets the unique identifier for this pattern.
     *
     * @return the id
     * @since 1.0.0-SNAPSHOT
     */
    public String id() {
        return this.id;
    }

    /**
     * The anchor block of this multiblock.
     *
     * @return the anchor
     * @since 1.0.0-SNAPSHOT
     */
    public BlockData anchor() {
        return this.anchor;
    }

    /**
     * The minimum and maximum offset bounds of this pattern.
     *
     * @return the 3d bounds
     * @since 1.0.0-SNAPSHOT
     */
    public MultiblockBounds3d bounds() {
        return this.bounds;
    }

    /**
     * The origin's x offset, typically zero.
     *
     * @return x
     * @since 1.0.0-SNAPSHOT
     */
    public int originX() {
        return this.originX;
    }

    /**
     * The origin's y offset, typically zero.
     *
     * @return y
     * @since 1.0.0-SNAPSHOT
     */
    public int originY() {
        return this.originY;
    }

    /**
     * The origin's z offset, typically zero.
     *
     * @return z
     * @since 1.0.0-SNAPSHOT
     */
    public int originZ() {
        return this.originZ;
    }

    /**
     * Gets the rotation ordinal of this pattern, typically 0 for stand alone patterns.
     *
     * @return rotation ordinal for {@link StructureRotation}
     * @since 1.0.0-SNAPSHOT
     */
    public int rotation() {
        return this.rotation;
    }

    /**
     * Gets the block data at the given offsets.
     *
     * @param offX the x off
     * @param offY the y off
     * @param offZ the z off
     * @return block data at that offset
     * @since 1.0.0-SNAPSHOT
     */
    public BlockData blockDataAt(int offX, int offY, int offZ) {
        long key = ByteUtils.packX24Z24Y16(offX, offY, offZ);
        byte id = structure.getOrDefault(key, (byte) -1);
        return id == -1 ? BlockType.AIR.createBlockData() : this.palette[id];
    }

    /**
     * Checks if the provided block data is at the offsets.
     *
     * @param check the block data to check
     * @param offX  the x off
     * @param offY  the y off
     * @param offZ  the z off
     * @return true if this block data is at this offset
     * @since 1.0.0-SNAPSHOT
     */
    public boolean isBlockDataAt(BlockData check, int offX, int offY, int offZ) {
        return check.equals(blockDataAt(offX, offY, offZ));
    }

    /**
     * Gets the raw structure output in form of a list of vector offsets of all components of the structure.
     *
     * @return the structure via a list of vector offsets
     * @since 1.0.0-SNAPSHOT
     */
    public List<Vector> structure() {
        return structure(0, 0, 0);
    }

    /**
     * Gets the raw structure output in form of a list of vector offsets of all components of the structure.
     *
     * @param originX the origin X of this structure
     * @param originY the origin Y of this structure
     * @param originZ the origin Z of this structure
     * @return the structure via a list of vector offsets
     * @since 1.0.0-SNAPSHOT
     */
    public List<Vector> structure(int originX, int originY, int originZ) {
        final List<Vector> out = new ArrayList<>(this.structure.size());
        for (final long packed : structure.keySet()) {
            final Component3 unpack = ByteUtils.unpackX24Z24Y16(packed);
            out.add(new Vector(unpack.c1() + originX, unpack.c3() + originY, unpack.c2() + originZ));
        }

        return out;
    }

    /**
     * Matches this pattern within the world.
     *
     * @param world   the world
     * @param originX the originX
     * @param originY the originY
     * @param originZ the originZ
     * @return true if this pattern is matched, otherwise false.
     * @since 1.0.0-SNAPSHOT
     */
    public boolean matchInWorld(World world, int originX, int originY, int originZ) {
        final var iterator = this.structure.long2ByteEntrySet().stream().iterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            long pack = entry.getLongKey();
            byte palette = entry.getByteValue();

            Component3 component = ByteUtils.unpackX24Z24Y16(pack);
            int relativeX = component.c1();
            int relativeY = component.c3();
            int relativeZ = component.c2();

            int worldX = originX + relativeX;
            int worldY = originY + relativeY;
            int worldZ = originZ + relativeZ;

            BlockData required = this.palette[palette];
            BlockData found = world.getBlockData(worldX, worldY, worldZ);

            if (!required.matches(found)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Creates a mapping between a starting character that progresses through the alphabet and the internal bit map of
     * this pattern.
     *
     * @param starting the starting character
     * @return this map
     * @since 1.0.0-SNAPSHOT
     */
    public Char2ObjectLinkedOpenHashMap<BlockData> exportPalette(char starting) {
        final Char2ObjectLinkedOpenHashMap<BlockData> map = new Char2ObjectLinkedOpenHashMap<>();
        for (final BlockData data : this.palette) {
            map.put(starting++, data);
        }
        return map;
    }

    /**
     * Creates a new MultiblockPattern rotated by the provided rotation.
     *
     * @param rotation the rotation to apply
     * @return a new independent pattern instance
     * @since 1.0.0-SNAPSHOT
     */
    public MultiblockPattern rotate(StructureRotation rotation) {
        return rotate(rotation, BlockRotationStrategy.BUKKIT);
    }

    /**
     * Creates a new MultiblockPattern rotated by the provided rotation.
     *
     * @param rotation the rotation to apply
     * @param strategy strategy used to rotate blocks
     * @return a new independent pattern instance
     * @since 1.0.0-SNAPSHOT
     */
    public MultiblockPattern rotate(StructureRotation rotation, BlockRotationStrategy strategy) {
        if (rotation == StructureRotation.NONE) {
            return this;
        }

        final Builder newBuilder = new Builder();
        // REVERTED: ID stays the same, allowing this to act as the Template ID
        newBuilder.id(this.id);

        BlockData rotatedAnchor = this.anchor.clone();
        strategy.rotate(rotatedAnchor, rotation);
        newBuilder.anchor(rotatedAnchor);

        int[] rotatedOrigin = rotateCoords(this.originX, this.originZ, rotation);
        newBuilder.origin(rotatedOrigin[0], this.originY, rotatedOrigin[1], rotation.ordinal());

        char[] indexToCharMap = new char[this.palette.length];

        for (int i = 0; i < this.palette.length; i++) {
            BlockData originalData = this.palette[i];
            if (originalData.getMaterial().isAir()) {
                continue;
            }

            BlockData rotatedData = originalData.clone();
            strategy.rotate(rotatedData, rotation);
            char newKey = (char) ('!' + i);

            newBuilder.paletteAppend(newKey, rotatedData);
            indexToCharMap[i] = newKey;
        }

        for (Long2ByteMap.Entry entry : this.structure.long2ByteEntrySet()) {
            long packedKey = entry.getLongKey();
            byte paletteIndex = entry.getByteValue();

            Component3 c3 = ByteUtils.unpackX24Z24Y16(packedKey);
            int x = c3.c1();
            int y = c3.c3();
            int z = c3.c2();

            int[] rotatedCoords = rotateCoords(x, z, rotation);
            int newX = rotatedCoords[0];
            int newZ = rotatedCoords[1];

            char key = indexToCharMap[paletteIndex];

            newBuilder.structureAppend(newX, y, newZ, key);
        }

        return newBuilder.build();
    }

    private int[] rotateCoords(int x, int z, StructureRotation rotation) {
        return switch (rotation) {
            case NONE -> new int[]{x, z};
            case CLOCKWISE_90 -> new int[]{-z, x};
            case CLOCKWISE_180 -> new int[]{-x, -z};
            case COUNTERCLOCKWISE_90 -> new int[]{z, -x};
        };
    }

    /**
     * Creates a new MultiblockPattern Builder
     *
     * @return a newly created builder
     * @since 1.0.0-SNAPSHOT
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * A Builder for {@link MultiblockPattern}.
     *
     * @since 1.0.0-SNAPSHOT
     */
    @NullMarked
    public static class Builder {

        private final Char2ObjectOpenHashMap<BlockData> palette = new Char2ObjectOpenHashMap<>();
        private final Long2ByteOpenHashMap structure = new Long2ByteOpenHashMap();
        private final Char2ByteOpenHashMap paletteMap = new Char2ByteOpenHashMap();
        private byte paletteId = 0;
        @Nullable
        private String id;
        @Nullable
        private BlockData anchor;
        private MultiblockBounds3d bounds3d = new MultiblockBounds3d(Integer.MAX_VALUE, Integer.MIN_VALUE,
            Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE
        );
        int x, y, z, rotation;

        public Builder() {
        }

        /**
         * Sets the identifier for the pattern.
         *
         * @param id the unique identifier
         * @return this builder
         * @since 1.0.0-SNAPSHOT
         */
        public Builder id(String id) {
            this.id = id;
            return this;
        }

        /**
         * Appends a block data to the palette with a specific character key.
         * <p>
         * If the provided block data is AIR, it is ignored (as air is the default background).
         *
         * @param key  the character key (e.g. used in configuration files)
         * @param data the block data
         * @return this builder
         * @since 1.0.0-SNAPSHOT
         */
        public Builder paletteAppend(char key, BlockData data) {
            if (data.getMaterial().isAir()) {
                return this;
            }
            palette.put(key, data);
            paletteMap.put(key, this.paletteId++);
            return this;
        }

        /**
         * Sets the anchor block for the structure.
         * <p>
         * The anchor represents the (0,0,0) center point of the structure.
         *
         * @param anchor the anchor block data
         * @return this builder
         * @since 1.0.0-SNAPSHOT
         */
        public Builder anchor(BlockData anchor) {
            this.anchor = anchor;
            return this;
        }

        /**
         * Adds a block to the structure at the specific offset using a palette key.
         *
         * @param offX       x offset relative to anchor
         * @param offY       y offset relative to anchor
         * @param offZ       z offset relative to anchor
         * @param paletteKey the character key defined in {@link #paletteAppend(char, BlockData)}
         * @return this builder
         * @throws IllegalArgumentException if the palette key has not been previously defined
         * @since 1.0.0-SNAPSHOT
         */
        public Builder structureAppend(int offX, int offY, int offZ, char paletteKey) {
            if (!this.paletteMap.containsKey(paletteKey)) {
                throw new IllegalArgumentException(
                    "Must define palette character before using it. No such character \"" + paletteKey + "\" is defined.");
            }
            this.bounds3d = bounds3d.adapt(offX, offY, offZ);
            structure.put(ByteUtils.packX24Z24Y16(offX, offY, offZ), this.paletteMap.get(paletteKey));
            return this;
        }

        /**
         * Sets the origin and rotation metadata.
         * <p>
         * This is typically used when reconstructing a pattern that has been rotated or shifted from a template.
         *
         * @param x        origin x
         * @param y        origin y
         * @param z        origin z
         * @param rotation rotation ordinal
         * @return this builder
         * @since 1.0.0-SNAPSHOT
         */
        public Builder origin(int x, int y, int z, int rotation) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.rotation = rotation;
            return this;
        }

        /**
         * Builds the {@link MultiblockPattern}.
         *
         * @return the built pattern
         * @throws NullPointerException  if id or anchor is null
         * @throws IllegalStateException if the structure does not contain a block at the anchor position (0,0,0)
         * @since 1.0.0-SNAPSHOT
         */
        public MultiblockPattern build() {
            Objects.requireNonNull(id, "No id provided to MultiblockPattern");
            Objects.requireNonNull(anchor, "No anchor provided to MultiblockPattern");

            long anchorKey = 0L;
            if (!structure.containsKey(anchorKey)) {
                throw new IllegalStateException(
                    "Multiblock structure must contain a block at the anchor position (0,0,0)");
            }

            final BlockData[] paletteArray = new BlockData[palette.size()];
            for (final Char2ObjectMap.Entry<BlockData> entry : palette.char2ObjectEntrySet()) {
                paletteArray[paletteMap.get(entry.getCharKey())] = entry.getValue();
            }

            return new MultiblockPattern(
                id,
                paletteArray,
                anchor,
                structure,
                bounds3d,
                x,
                y,
                z,
                rotation
            );
        }
    }

    /**
     * Strategy implementation to rotate blocks.
     * <p>
     * Exists because of Mockbukkit limitations.
     *
     * @since 1.0.0-SNAPSHOT
     */
    @FunctionalInterface
    public interface BlockRotationStrategy {
        /**
         * The default Bukkit strategy.
         */
        BlockRotationStrategy BUKKIT = BlockData::rotate;

        /**
         * Rotates the given block data.
         *
         * @param data the data to rotate
         * @param rot  the rotation to apply
         */
        void rotate(BlockData data, StructureRotation rot);
    }
}
