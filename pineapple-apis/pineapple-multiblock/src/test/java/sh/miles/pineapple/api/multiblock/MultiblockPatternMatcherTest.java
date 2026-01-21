package sh.miles.pineapple.api.multiblock;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.structure.StructureRotation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.world.WorldMock;
import sh.miles.pineapple.api.multiblock.MultiblockPattern.BlockRotationStrategy;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MultiblockPatternMatcherTest {

    private ServerMock server;
    private WorldMock world;
    private MultiblockPatterns manager;
    private MultiblockPatternMatcher matcher;

    // Safe rotation strategy for MockBukkit (only rotates Directional blocks safely)
    private final BlockRotationStrategy testStrategy = (data, rotation) -> {
        if (data instanceof Directional d && rotation == StructureRotation.CLOCKWISE_90) {
            if (d.getFacing() == BlockFace.NORTH) {
                d.setFacing(BlockFace.EAST);
            }
        }
    };

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        world = server.addSimpleWorld("test_world");
        ensureChunksLoaded(); // Pre-load test area

        manager = mock(MultiblockPatterns.class);
        matcher = new MultiblockPatternMatcher(manager);
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    private void ensureChunksLoaded() {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                world.getChunkAt(x, z);
            }
        }
    }

    @Test
    @DisplayName("Match returns null if no patterns are registered for the anchor")
    void testNoPatternsForAnchor() {
        world.setBlockData(0, 64, 0, Material.DIAMOND_BLOCK.createBlockData());

        // Mock manager returns empty list
        when(manager.getByAnchor(any())).thenReturn(Collections.emptyList());

        Block anchor = world.getBlockAt(0, 64, 0);
        assertNull(matcher.match(anchor), "Should fail fast if registry is empty for this block type");
    }

    @Test
    @DisplayName("Simple Match: Detects a 2-block structure")
    void testSimpleMatch() {
        // Pattern: Stone(Anchor) -> Stone(East)
        BlockData stone = Material.STONE.createBlockData();
        MultiblockPattern pattern = MultiblockPattern.builder()
            .id("simple_line")
            .anchor(stone)
            .paletteAppend('s', stone)
            .structureAppend(0, 0, 0, 's')
            .structureAppend(1, 0, 0, 's')
            .build();

        registerPattern(pattern);

        // World Setup
        world.setBlockData(10, 64, 10, stone); // Anchor
        world.setBlockData(11, 64, 10, stone); // East Neighbor

        // Act
        var result = matcher.match(world.getBlockAt(10, 64, 10));

        // Assert
        assertNotNull(result);
        assertEquals("simple_line", result.group().id());
        assertEquals(StructureRotation.NONE.ordinal(), result.specificRotation().rotation());
    }

    @Test
    @DisplayName("Rotated Match: Detects structure rotated 90 degrees")
    void testRotatedMatch() {
        // Pattern: Stairs(North) -> Stone(North of Anchor, Z-1)
        Directional stairs = (Directional) Material.STONE_STAIRS.createBlockData();
        stairs.setFacing(BlockFace.NORTH);
        BlockData stone = Material.STONE.createBlockData();

        MultiblockPattern pattern = MultiblockPattern.builder()
            .id("rotated_test")
            .anchor(stairs)
            .paletteAppend('a', stairs)
            .paletteAppend('s', stone)
            .structureAppend(0, 0, 0, 'a')
            .structureAppend(0, 0, -1, 's') // North relative
            .build();

        registerPattern(pattern);

        // World Setup: Rotated 90 Degrees Clockwise
        // Anchor becomes Stairs(East)
        // Stone moves from (0,0,-1)[North] to (1,0,0)[East]
        Directional worldStairs = (Directional) Material.STONE_STAIRS.createBlockData();
        worldStairs.setFacing(BlockFace.EAST);

        world.setBlockData(10, 64, 10, worldStairs); // Anchor
        world.setBlockData(11, 64, 10, stone);       // East relative

        // Act
        var result = matcher.match(world.getBlockAt(10, 64, 10));

        // Assert
        assertNotNull(result, "Should match the 90-degree variation");
        assertEquals("rotated_test", result.group().id());
        assertEquals(StructureRotation.CLOCKWISE_90.ordinal(), result.specificRotation().rotation());
    }

    @Test
    @DisplayName("Mismatch: Returns null if structure is incomplete")
    void testPartialMatchFailure() {
        BlockData stone = Material.STONE.createBlockData();
        MultiblockPattern pattern = MultiblockPattern.builder()
            .id("incomplete_test")
            .anchor(stone)
            .paletteAppend('s', stone)
            .structureAppend(0, 0, 0, 's')
            .structureAppend(0, 1, 0, 's') // Requires block above
            .build();

        registerPattern(pattern);

        // World Setup: Only the anchor exists
        world.setBlockData(0, 64, 0, stone);
        world.setBlockData(0, 65, 0, Material.AIR.createBlockData()); // Missing top block

        var result = matcher.match(world.getBlockAt(0, 64, 0));

        assertNull(result, "Should fail if structure blocks are missing");
    }

    @Test
    @DisplayName("Safety: Returns null if structure extends into unloaded chunk")
    void testUnloadedChunkSafety() {
        BlockData stone = Material.STONE.createBlockData();

        // Pattern extends 20 blocks out (crossing chunk boundary)
        MultiblockPattern pattern = MultiblockPattern.builder()
            .id("chunk_safety")
            .anchor(stone)
            .paletteAppend('s', stone)
            .structureAppend(0, 0, 0, 's')
            .structureAppend(20, 0, 0, 's')
            .build();

        registerPattern(pattern);

        // Setup world at 0,0. Target block is at 20,0 (Chunk 1,0).
        world.setBlockData(0, 64, 0, stone);

        // Explicitly unload the neighbor chunk
        world.unloadChunk(1, 0);
        assertFalse(world.isChunkLoaded(1, 0));

        // Act
        var result = matcher.match(world.getBlockAt(0, 64, 0));

        // Assert
        assertNull(result, "Should abort matching if target chunk is unloaded");
    }

    @Test
    @DisplayName("Candidate Elimination: Filters out incorrect rotations in shared volume")
    void testCandidateElimination() {
        // Concept: Two variations of a pattern.
        // V1 (North): Stone at (0,0,-1)
        // V2 (East):  Stone at (1,0,0)
        // V3 (South): Stone at (0,0,1)
        // V4 (West):  Stone at (-1,0,0)

        // If we place blocks for V2 (East), the matcher iterates X/Y/Z covering ALL variations.
        // It must eliminate V1, V3, and V4 because their expected blocks (North/South/West) are AIR.

        BlockData anchor = Material.OBSIDIAN.createBlockData();
        BlockData tip = Material.GLOWSTONE.createBlockData();

        MultiblockPattern pattern = MultiblockPattern.builder()
            .id("elimination_test")
            .anchor(anchor)
            .paletteAppend('a', anchor)
            .paletteAppend('t', tip)
            .structureAppend(0, 0, 0, 'a')
            .structureAppend(0, 0, -1, 't') // Defined pointing North
            .build();

        registerPattern(pattern);

        // World Setup: Matches East Rotation (Clockwise 90)
        // Anchor at 0,0. Tip at 1,0.
        world.setBlockData(0, 64, 0, anchor);
        world.setBlockData(1, 64, 0, tip);

        // Ensure other directions are AIR
        world.setBlockData(0, 64, -1, Material.AIR.createBlockData()); // North
        world.setBlockData(-1, 64, 0, Material.AIR.createBlockData()); // West
        world.setBlockData(0, 64, 1, Material.AIR.createBlockData());  // South

        // Act
        var result = matcher.match(world.getBlockAt(0, 64, 0));

        // Assert
        assertNotNull(result);
        assertEquals(StructureRotation.CLOCKWISE_90.ordinal(), result.specificRotation().rotation());
    }

    private void registerPattern(MultiblockPattern pattern) {
        // Wrap the pattern in a group using the safe test strategy
        MultiblockPatternGroup group = MultiblockPatternGroup.from(pattern, testStrategy);

        // Mock the manager to return this group for the anchor type
        when(manager.getByAnchor(pattern.anchor().getMaterial()))
            .thenReturn(List.of(group));
    }

    // Helper to verify chunk state in MockBukkit (wrapper for readability)
    private void assertFalse(boolean condition) {
        if (condition) throw new AssertionError("Expected condition to be false");
    }
}