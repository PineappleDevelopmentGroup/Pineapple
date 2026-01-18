package sh.miles.pineapple.api.multiblock;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.structure.StructureRotation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import sh.miles.pineapple.api.multiblock.MultiblockPattern.BlockRotationStrategy;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MultiblockPatternGroupTest {

    // Strategy: Only rotate Directional blocks to simulate state changes
    private final BlockRotationStrategy testStrategy = (data, rotation) -> {
        if (data instanceof Directional d && rotation == StructureRotation.CLOCKWISE_90) {
            if (d.getFacing() == BlockFace.NORTH) {
                d.setFacing(BlockFace.EAST);
            }
        }
    };

    @BeforeEach
    void setUp() {
        MockBukkit.mock();
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Creation: Group initializes correctly from a source pattern")
    void testCreation() {
        BlockData anchor = Material.OBSIDIAN.createBlockData();
        MultiblockPattern source = MultiblockPattern.builder()
            .id("group_create_test")
            .anchor(anchor)
            .paletteAppend('a', anchor)
            .structureAppend(0, 0, 0, 'a') // FIX: Must exist at 0,0,0
            .origin(1, 2, 3, 0)
            .build();

        MultiblockPatternGroup group = MultiblockPatternGroup.from(source, testStrategy);

        assertNotNull(group);
        assertEquals("group_create_test", group.id());
        assertEquals(source.bounds(), group.bounds());

        assertEquals(1, group.originX());
        assertEquals(2, group.originY());
        assertEquals(3, group.originZ());

        assertEquals(source, group.source());
    }

    @Test
    @DisplayName("Accessors: Rotations are indexed correctly by Ordinal and Enum")
    void testRotationAccessors() {
        BlockData anchor = Material.STONE.createBlockData();
        MultiblockPattern source = MultiblockPattern.builder()
            .id("rot_access_test")
            .anchor(anchor)
            .paletteAppend('a', anchor)
            .structureAppend(0, 0, 0, 'a') // FIX
            .build();

        MultiblockPatternGroup group = MultiblockPatternGroup.from(source, testStrategy);

        // Integer index access
        assertEquals(StructureRotation.NONE.ordinal(), group.rotation(0).rotation());
        assertEquals(StructureRotation.CLOCKWISE_90.ordinal(), group.rotation(1).rotation());
        assertEquals(StructureRotation.CLOCKWISE_180.ordinal(), group.rotation(2).rotation());
        assertEquals(StructureRotation.COUNTERCLOCKWISE_90.ordinal(), group.rotation(3).rotation());

        // Enum access
        assertSame(group.rotation(0), group.rotation(StructureRotation.NONE));
        assertSame(group.rotation(1), group.rotation(StructureRotation.CLOCKWISE_90));

        assertThrows(IndexOutOfBoundsException.class, () -> group.rotation(4));
    }

    @Test
    @DisplayName("List: Returns all 4 cardinal variations")
    void testPatternsList() {
        BlockData anchor = Material.STONE.createBlockData();
        MultiblockPattern source = MultiblockPattern.builder()
            .id("list_test")
            .anchor(anchor)
            .paletteAppend('a', anchor)
            .structureAppend(0, 0, 0, 'a') // FIX
            .build();

        MultiblockPatternGroup group = MultiblockPatternGroup.from(source, testStrategy);
        List<MultiblockPattern> list = group.patterns();

        assertEquals(4, list.size());
        assertEquals(StructureRotation.NONE.ordinal(), list.get(0).rotation());
        assertEquals(StructureRotation.COUNTERCLOCKWISE_90.ordinal(), list.get(3).rotation());
    }

    @Test
    @DisplayName("Anchors: Aggregates unique anchor states from all rotations")
    void testAnchors() {
        Directional anchor = (Directional) Material.STONE_STAIRS.createBlockData();
        anchor.setFacing(BlockFace.NORTH);

        MultiblockPattern source = MultiblockPattern.builder()
            .id("anchor_group_test")
            .anchor(anchor)
            .paletteAppend('a', anchor)
            .structureAppend(0, 0, 0, 'a') // FIX
            .build();

        MultiblockPatternGroup group = MultiblockPatternGroup.from(source, testStrategy);
        Set<BlockData> anchors = group.anchor();

        assertNotNull(anchors);
        // Expecting 2: North (Original) and East (Rotated by strategy)
        assertEquals(2, anchors.size(), "Should contain exactly 2 unique rotated states (North, East)");
    }

    @Test
    @DisplayName("BlockDataAt: Returns distinct results for rotated coordinates")
    void testBlockDataRetrieval() {
        // Setup:
        // (0,0,0) = Bedrock (Anchor)
        // (1,0,0) = Stone
        BlockData bedrock = Material.BEDROCK.createBlockData();
        BlockData stone = Material.STONE.createBlockData();

        MultiblockPattern source = MultiblockPattern.builder()
            .id("rotation_check")
            .anchor(bedrock)
            .paletteAppend('b', bedrock)
            .structureAppend(0, 0, 0, 'b') // FIX: Anchor required
            .paletteAppend('s', stone)
            .structureAppend(1, 0, 0, 's') // The block we are testing
            .build();

        MultiblockPatternGroup group = MultiblockPatternGroup.from(source, testStrategy);

        // Act: Query (1, 0, 0)
        List<BlockData> results = group.blockDataAt(1, 0, 0);

        assertEquals(4, results.size());

        // 0 (NONE): Stone is at (1,0,0)
        assertEquals(stone, results.get(0));

        // 1 (CW 90): Stone moves to (0,0,1). (1,0,0) is now AIR.
        assertEquals(Material.AIR, results.get(1).getMaterial());

        // 2 (CW 180): Stone moves to (-1,0,0). (1,0,0) is AIR.
        assertEquals(Material.AIR, results.get(2).getMaterial());
    }
}