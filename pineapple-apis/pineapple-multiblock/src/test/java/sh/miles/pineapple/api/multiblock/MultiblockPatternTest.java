package sh.miles.pineapple.api.multiblock;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.util.Vector;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.world.WorldMock;
import sh.miles.pineapple.api.multiblock.MultiblockPattern.BlockRotationStrategy;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MultiblockPatternTest {

    @BeforeEach
    void setUp() {
        MockBukkit.mock();
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Nested
    @DisplayName("Builder Validation")
    class BuilderTests {

        @Test
        @DisplayName("Build succeeds with valid inputs")
        void validBuild() {
            BlockData stone = Material.STONE.createBlockData();
            MultiblockPattern pattern = MultiblockPattern.builder()
                .id("test_valid")
                .anchor(stone)
                .paletteAppend('s', stone)
                .structureAppend(0, 0, 0, 's')
                .build();

            assertNotNull(pattern);
            assertEquals("test_valid", pattern.id());
            assertEquals(stone, pattern.anchor());
        }

        @Test
        @DisplayName("Build fails if ID is missing")
        void missingId() {
            BlockData stone = Material.STONE.createBlockData();
            var builder = MultiblockPattern.builder()
                .anchor(stone)
                .paletteAppend('s', stone)
                .structureAppend(0, 0, 0, 's');

            assertThrows(NullPointerException.class, builder::build);
        }

        @Test
        @DisplayName("Build fails if Anchor is missing")
        void missingAnchor() {
            BlockData stone = Material.STONE.createBlockData();
            var builder = MultiblockPattern.builder()
                .id("test_no_anchor")
                .paletteAppend('s', stone)
                .structureAppend(0, 0, 0, 's');

            assertThrows(NullPointerException.class, builder::build);
        }

        @Test
        @DisplayName("Build fails if structure does not have block at (0,0,0)")
        void missingOriginBlock() {
            BlockData stone = Material.STONE.createBlockData();
            var builder = MultiblockPattern.builder()
                .id("test_missing_origin")
                .anchor(stone)
                .paletteAppend('s', stone)
                .structureAppend(1, 0, 0, 's'); // No block at 0,0,0

            assertThrows(IllegalStateException.class, builder::build, "Should require block at 0,0,0");
        }

        @Test
        @DisplayName("Build fails if using undefined palette key")
        void undefinedPalette() {
            var builder = MultiblockPattern.builder();
            assertThrows(IllegalArgumentException.class, () -> builder.structureAppend(0, 0, 0, 'z'));
        }
    }

    @Nested
    @DisplayName("Data Retrieval")
    class DataTests {

        @Test
        @DisplayName("blockDataAt returns correct data or Air")
        void lookup() {
            BlockData stone = Material.STONE.createBlockData();
            BlockData dirt = Material.DIRT.createBlockData();

            MultiblockPattern pattern = MultiblockPattern.builder()
                .id("lookup")
                .anchor(stone)
                .paletteAppend('s', stone)
                .paletteAppend('d', dirt)
                .structureAppend(0, 0, 0, 's')
                .structureAppend(1, 1, 1, 'd')
                .build();

            // Match
            assertEquals(stone, pattern.blockDataAt(0, 0, 0));
            assertEquals(dirt, pattern.blockDataAt(1, 1, 1));
            assertTrue(pattern.isBlockDataAt(dirt, 1, 1, 1));

            // No match (Default Air)
            assertEquals(Material.AIR, pattern.blockDataAt(0, 50, 0).getMaterial());
        }

        @Test
        @DisplayName("Bounds are calculated correctly")
        void bounds() {
            BlockData stone = Material.STONE.createBlockData();
            MultiblockPattern pattern = MultiblockPattern.builder()
                .id("bounds")
                .anchor(stone)
                .paletteAppend('x', stone)
                .structureAppend(0, 0, 0, 'x')
                .structureAppend(-2, -5, -2, 'x')
                .structureAppend(2, 5, 2, 'x')
                .build();

            assertEquals(-2, pattern.bounds().minX());
            assertEquals(-5, pattern.bounds().minY());
            assertEquals(2, pattern.bounds().maxX());
            assertEquals(5, pattern.bounds().maxY());
        }
    }

    @Nested
    @DisplayName("Rotation Logic")
    class RotationTests {

        // Strategy 1: For Directional blocks (Stairs) - explicitly handles metadata
        private final BlockRotationStrategy directionalStrategy = (data, rot) -> {
            if (data instanceof Directional d && rot == StructureRotation.CLOCKWISE_90) {
                if (d.getFacing() == BlockFace.NORTH) d.setFacing(BlockFace.EAST);
            }
        };

        // Strategy 2: Safe No-Op - prevents MockBukkit crashes on non-directional blocks
        private final BlockRotationStrategy noOpStrategy = (data, rot) -> {
            // Do nothing. Stone doesn't rotate, and we don't want MockBukkit to crash.
        };

        @Test
        @DisplayName("Rotate 90 Degrees Clockwise transforms coordinates correctly")
        void rotate90() {
            // Setup: Line along X axis: (0,0,0) -> (1,0,0)
            // Anchor is North Facing stairs
            Directional stairsNorth = (Directional) Material.STONE_STAIRS.createBlockData();
            stairsNorth.setFacing(BlockFace.NORTH);
            BlockData stone = Material.STONE.createBlockData();

            MultiblockPattern original = MultiblockPattern.builder()
                .id("rot_test")
                .anchor(stairsNorth)
                .paletteAppend('a', stairsNorth)
                .paletteAppend('s', stone)
                .structureAppend(0, 0, 0, 'a') // Anchor
                .structureAppend(1, 0, 0, 's') // 1 block East
                .build();

            // Act: Rotate 90 CW using the Directional strategy
            MultiblockPattern rotated = original.rotate(StructureRotation.CLOCKWISE_90, directionalStrategy);

            // Assertions
            assertEquals("rot_test", rotated.id());

            Directional rotatedAnchor = (Directional) rotated.anchor();
            assertEquals(BlockFace.EAST, rotatedAnchor.getFacing(), "Anchor should rotate North -> East");

            // (1,0,0) -> (0,0,1)
            assertEquals(stone, rotated.blockDataAt(0, 0, 1), "Stone should move from X=1 to Z=1");
            assertEquals(Material.AIR, rotated.blockDataAt(1, 0, 0).getMaterial(), "Old position should be empty");
        }

        @Test
        @DisplayName("Rotate 180 Degrees transforms coordinates correctly")
        void rotate180() {
            BlockData stone = Material.STONE.createBlockData();
            MultiblockPattern original = MultiblockPattern.builder()
                .id("180_test")
                .anchor(stone)
                .paletteAppend('s', stone)
                .structureAppend(0, 0, 0, 's')
                .structureAppend(2, 0, 2, 's')
                .build();

            // Act: Rotate 180
            // FIX: Use noOpStrategy instead of BUKKIT to prevent MockBukkit crash
            MultiblockPattern rotated = original.rotate(StructureRotation.CLOCKWISE_180, noOpStrategy);

            // Math Check: (x, z) -> (-x, -z)
            // (2, 2) -> (-2, -2)
            assertEquals(stone, rotated.blockDataAt(-2, 0, -2));
        }
    }

    @Nested
    @DisplayName("World Interaction")
    class WorldTests {

        @Test
        @DisplayName("matchInWorld returns true for valid structure")
        void matchSuccess() {
            WorldMock world = MockBukkit.getMock().addSimpleWorld("match_world");
            BlockData stone = Material.STONE.createBlockData();

            // Pattern: Anchor(0,0,0) and Top(0,1,0)
            MultiblockPattern pattern = MultiblockPattern.builder()
                .id("match_success")
                .anchor(stone)
                .paletteAppend('s', stone)
                .structureAppend(0, 0, 0, 's')
                .structureAppend(0, 1, 0, 's')
                .build();

            // Set World Blocks
            world.setBlockData(10, 64, 10, stone); // Origin
            world.setBlockData(10, 65, 10, stone); // Origin + 1Y

            assertTrue(pattern.matchInWorld(world, 10, 64, 10));
        }

        @Test
        @DisplayName("matchInWorld returns false for mismatch")
        void matchFail() {
            WorldMock world = MockBukkit.getMock().addSimpleWorld("match_fail");
            BlockData stone = Material.STONE.createBlockData();
            BlockData dirt = Material.DIRT.createBlockData();

            MultiblockPattern pattern = MultiblockPattern.builder()
                .id("match_fail")
                .anchor(stone)
                .paletteAppend('s', stone)
                .structureAppend(0, 0, 0, 's')
                .structureAppend(1, 0, 0, 's')
                .build();

            // Set World Blocks: Origin is Stone, but neighbor is Dirt
            world.setBlockData(0, 64, 0, stone);
            world.setBlockData(1, 64, 0, dirt);

            assertFalse(pattern.matchInWorld(world, 0, 64, 0));
        }

        @Test
        @DisplayName("Structure retrieval outputs correct vectors")
        void structureList() {
            BlockData stone = Material.STONE.createBlockData();
            MultiblockPattern pattern = MultiblockPattern.builder()
                .id("struct_list")
                .anchor(stone)
                .paletteAppend('s', stone)
                .structureAppend(0, 0, 0, 's')
                .structureAppend(5, 5, 5, 's')
                .build();

            List<Vector> vectors = pattern.structure();
            assertEquals(2, vectors.size());

            // We check contains because map ordering isn't strictly guaranteed by the List contract
            // (though Long2ByteOpenHashMap iteration order is deterministic, better safe)
            assertTrue(vectors.contains(new Vector(0,0,0)));
            assertTrue(vectors.contains(new Vector(5,5,5)));
        }
    }
}