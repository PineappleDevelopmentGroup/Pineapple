package sh.miles.pineapple.api.multiblock;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import sh.miles.pineapple.ByteUtils;
import sh.miles.pineapple.ByteUtils.Component3;
import sh.miles.pineapple.api.multiblock.event.MultiblockLoadEvent;
import sh.miles.pineapple.api.multiblock.event.MultiblockUnloadEvent;
import sh.miles.pineapple.util.PdcUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Manages the lifecycle, storage, and retrieval of {@link ActiveMultiblock} instances.
 * <p>
 * This service handles the mapping of in-memory multiblocks to their persistent storage within {@link Chunk}
 * PersistentDataContainers, as well as spatial queries for conflict detection.
 *
 * @since 1.0.0-SNAPSHOT
 */
@NullMarked
public final class ActiveMultiblocks {

    private static final NamespacedKey STORAGE_KEY = new NamespacedKey("stratus", "multiblock_data");
    private static final NamespacedKey ANCHORS_KEY = new NamespacedKey("stratus", "multiblock_anchors");
    private static final NamespacedKey PATTERN_KEY = new NamespacedKey("stratus", "multiblock_pattern");
    private static final NamespacedKey ROTATION_KEY = new NamespacedKey("stratus", "multiblock_rotation");

    private final Map<Location, ActiveMultiblock> activeMultiblocks = new HashMap<>();
    private final Map<UUID, Long2ObjectOpenHashMap<ActiveMultiblock>> lookup = new HashMap<>();

    /**
     * Retrieves an active multiblock at the specified anchor location.
     * <p>
     * This lookup should be O(1) or close to it.
     *
     * @param location the anchor location
     * @return the active multiblock, or null if none exists
     * @since 1.0.0-SNAPSHOT
     */
    @Nullable
    public ActiveMultiblock get(Location location) {
        final ActiveMultiblock anchor = activeMultiblocks.get(location);
        if (anchor != null) {
            return anchor;
        }

        World world = location.getWorld();
        if (world == null) return null;

        final Long2ObjectOpenHashMap<ActiveMultiblock> worldCache = lookup.get(world.getUID());
        if (worldCache == null) {
            return null;
        }

        long packed = ByteUtils.packX24Z24Y16(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        return worldCache.get(packed);
    }

    /**
     * Registers a new multiblock into memory.
     * <p>
     * This marks the multiblock as "dirty" (unsaved) and active in the world. Implementations should ensure spatial
     * indexing is updated immediately.
     *
     * @param multiblock the multiblock to register
     * @throws IllegalArgumentException if the multiblock conflicts with an existing one
     * @since 1.0.0-SNAPSHOT
     */
    public void register(ActiveMultiblock multiblock) {
        activeMultiblocks.put(multiblock.anchor(), multiblock);
        cacheConstituents(multiblock);

        multiblock.dirty = true;
        multiblock.onDisk = false;
    }

    /**
     * Destroys a multiblock, removing it from memory and scheduling its removal from disk.
     *
     * @param multiblock the multiblock to destroy
     * @since 1.0.0-SNAPSHOT
     */
    public void destroy(ActiveMultiblock multiblock) {
        activeMultiblocks.remove(multiblock.anchor());

        UUID worldUid = multiblock.anchor().getWorld().getUID();
        Long2ObjectOpenHashMap<ActiveMultiblock> worldCache = lookup.get(worldUid);
        if (worldCache != null) {
            for (final Vector constituent : multiblock.constituents()) {
                final long pack = ByteUtils.packX24Z24Y16(constituent.getBlockX(), constituent.getBlockY(), constituent.getBlockZ());
                worldCache.remove(pack);
            }

            if (worldCache.isEmpty()) {
                lookup.remove(worldUid);
            }
        }

        if (!(multiblock).onDisk) {
            return;
        }

        final Location loc = multiblock.anchor();
        PersistentDataContainer container = loc.getChunk().getPersistentDataContainer();
        container.remove(PdcUtils.locationKey("stratus", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
    }

    /**
     * Checks if the candidate multiblock conflicts with any currently active structures.
     * <p>
     * A conflict occurs if any constituent block of the candidate shares a coordinate with an existing multiblock.
     *
     * @param multiblock The candidate structure to check
     * @return true if it overlaps with an existing structure, false otherwise
     * @since 1.0.0-SNAPSHOT
     */
    public boolean conflicts(ActiveMultiblock multiblock) {
        World world = multiblock.anchor().getWorld();
        if (world == null) return false;

        final Long2ObjectOpenHashMap<ActiveMultiblock> worldCache = lookup.get(world.getUID());
        if (worldCache == null || worldCache.isEmpty()) {
            return false;
        }

        for (final Vector vector : multiblock.constituents()) {
            final long pack = ByteUtils.packX24Z24Y16(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());

            if (worldCache.containsKey(pack)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Loads all multiblocks stored within the specified chunk's persistent data container.
     * <p>
     * This is typically called by a ChunkLoadEvent listener. Implementations should fire
     * {@link MultiblockLoadEvent} for each loaded instance.
     *
     * @param manager the pattern manager used to resolve Pattern IDs found in disk storage
     * @param chunk   the chunk to load from
     * @since 1.0.0-SNAPSHOT
     */
    public void load(MultiblockPatterns manager, Chunk chunk) {
        PersistentDataContainer chunkPdc = chunk.getPersistentDataContainer();
        if (!chunkPdc.has(ANCHORS_KEY)) {
            return;
        }

        List<Long> anchors = chunkPdc.get(ANCHORS_KEY, PersistentDataType.LIST.longs());
        if (anchors == null) {
            return;
        }

        for (final Long anchor : anchors) {
            Component3 loc = ByteUtils.unpackX24Z24Y16(anchor);
            final NamespacedKey key = PdcUtils.locationKey("stratus", loc.c1(), loc.c3(), loc.c2());

            if (!chunkPdc.has(key, PersistentDataType.TAG_CONTAINER)) {
                continue;
            }

            final Location location = new Location(chunk.getWorld(), loc.c1(), loc.c3(), loc.c2());

            final PersistentDataContainer blockPdc = chunkPdc.get(key, PersistentDataType.TAG_CONTAINER);
            if (blockPdc != null) {
                final ActiveMultiblock loaded = loadActiveMultiblock(manager, location, blockPdc);
                this.activeMultiblocks.put(location, loaded);
                cacheConstituents(loaded);
            }
        }
    }

    private void cacheConstituents(ActiveMultiblock multiblock) {
        UUID worldUid = multiblock.anchor().getWorld().getUID();
        final Long2ObjectOpenHashMap<ActiveMultiblock> worldCache = lookup.computeIfAbsent(worldUid, k -> new Long2ObjectOpenHashMap<>());

        // Use the cached constituents from ActiveMultiblock
        for (final Vector vector : multiblock.constituents()) {
            final long pack = ByteUtils.packX24Z24Y16(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
            worldCache.put(pack, multiblock);
        }
    }


    /**
     * Saves all active multiblocks belonging to the specified chunk to its persistent data container.
     * <p>
     * This is typically called by a ChunkUnloadEvent or WorldSaveEvent listener. Implementations should fire
     * {@link MultiblockUnloadEvent}.
     *
     * @param manager the pattern manager (context for serialization)
     * @param chunk   the chunk to save
     * @since 1.0.0-SNAPSHOT
     */
    public void save(MultiblockPatterns manager, Chunk chunk) {
        PersistentDataContainer chunkPdc = chunk.getPersistentDataContainer();
        final List<Long> locs = new ArrayList<>();

        for (final ActiveMultiblock value : this.activeMultiblocks.values()) {
            final Location loc = value.anchor();
            final int cx = loc.getBlockX() >> 4;
            final int cz = loc.getBlockZ() >> 4;
            if (cx != chunk.getX() || cz != chunk.getZ()) {
                continue;
            }

            final NamespacedKey key = PdcUtils.locationKey("stratus", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
            locs.add(ByteUtils.packX24Z24Y16(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));

            if (!chunkPdc.has(key, PersistentDataType.TAG_CONTAINER)) {
                chunkPdc.set(key, PersistentDataType.TAG_CONTAINER, chunkPdc.getAdapterContext().newPersistentDataContainer());
            }

            final PersistentDataContainer blockPdc = chunkPdc.get(key, PersistentDataType.TAG_CONTAINER);
            if (blockPdc != null) {
                saveActiveMultiblock(value, blockPdc);
                chunkPdc.set(key, PersistentDataType.TAG_CONTAINER, blockPdc);
            }
        }

        chunkPdc.set(ANCHORS_KEY, PersistentDataType.LIST.longs(), locs);
    }

    private static ActiveMultiblock loadActiveMultiblock(MultiblockPatterns patterns, Location location, PersistentDataContainer container) {
        if (!container.has(PATTERN_KEY, PersistentDataType.STRING)) {
            throw new IllegalArgumentException("Failed to load multiblock at (%d, %d, %d) in %s, because its data is corrupted".formatted(location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getWorld().getName()));
        }

        final String patternId = container.get(PATTERN_KEY, PersistentDataType.STRING);
        if (patternId == null) throw new IllegalStateException("Pattern ID missing");
        final Integer rot = container.get(ROTATION_KEY, PersistentDataType.INTEGER);
        if (rot == null) throw new IllegalStateException("Pattern Rotation missing");

        final var pattern = patterns.get(patternId);
        if (pattern == null) throw new IllegalStateException("Saved pattern " + patternId + " no longer exists");
        final ActiveMultiblock multiblock = ActiveMultiblock.activeMultiblock(pattern.rotation(rot), location);
        multiblock.onDisk = true;
        multiblock.dirty = false;

        if (!container.has(STORAGE_KEY, PersistentDataType.TAG_CONTAINER)) {
            return multiblock;
        }

        PersistentDataContainer storage = container.get(STORAGE_KEY, PersistentDataType.TAG_CONTAINER);
        if (storage != null) {
            final MultiblockLoadEvent event = new MultiblockLoadEvent(multiblock, storage);
            Bukkit.getServer().getPluginManager().callEvent(event);
        }
        return multiblock;
    }

    private static void saveActiveMultiblock(ActiveMultiblock multiblock, PersistentDataContainer container) {
        if (!multiblock.onDisk) {
            container.set(PATTERN_KEY, PersistentDataType.STRING, multiblock.pattern().id());
            container.set(ROTATION_KEY, PersistentDataType.INTEGER, multiblock.pattern().rotation());
            container.set(STORAGE_KEY, PersistentDataType.TAG_CONTAINER, container.getAdapterContext().newPersistentDataContainer());
        }

        final PersistentDataContainer storage = container.get(STORAGE_KEY, PersistentDataType.TAG_CONTAINER);
        if (storage != null) {
            final MultiblockUnloadEvent event = new MultiblockUnloadEvent(multiblock, storage);
            Bukkit.getServer().getPluginManager().callEvent(event);
            container.set(STORAGE_KEY, PersistentDataType.TAG_CONTAINER, storage);
        }

        multiblock.onDisk = true;
        multiblock.dirty = false;
    }

}
