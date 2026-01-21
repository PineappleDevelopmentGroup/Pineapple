package sh.miles.pineapple.api.multiblock;

import com.google.gson.Gson;
import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import sh.miles.pineapple.api.multiblock.MultiblockPattern.BlockRotationStrategy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

/**
 * Manages the lifecycle, storage, and retrieval of {@link MultiblockPatternGroup}s.
 * <p>
 * Implementations are responsible for handling the synchronization between in-memory pattern objects and their
 * persistent representations (e.g., JSON on disk).
 *
 * @since 1.0.0-SNAPSHOT
 */
@NullMarked
public final class MultiblockPatterns {

    private final HashMap<String, MultiblockPatternGroup> groups = new HashMap<>();
    private final HashMap<Material, List<MultiblockPatternGroup>> anchorIndecies = new HashMap<>();
    private final List<String> dirty = new ArrayList<>();
    private final Gson gson;
    private BlockRotationStrategy strategy = BlockRotationStrategy.BUKKIT;

    MultiblockPatterns(Gson gson) {
        this.gson = gson;
    }

    /**
     * Retrieves a loaded {@link MultiblockPatternGroup} by its identifier.
     *
     * @param id the unique identifier of the pattern
     * @return the pattern group, or {@code null} if not found
     * @since 1.0.0-SNAPSHOT
     */
    @Nullable
    public MultiblockPatternGroup get(String id) {
        return groups.get(id);
    }

    /**
     * Registers a new pattern into the manager.
     * <p>
     * Implementations should automatically generate the necessary rotations (creating a Group) and mark the pattern for
     * saving if a dirty-tracking system is in use. If a pattern with the same ID already exists, it should be
     * replaced.
     *
     * @param pattern the pattern to add
     * @since 1.0.0-SNAPSHOT
     */
    public void add(MultiblockPattern pattern) {
        add0(pattern);
        if (!dirty.contains(pattern.id())) {
            dirty.add(pattern.id());
        }
    }

    private void add0(MultiblockPattern pattern) {
        final MultiblockPatternGroup group = MultiblockPatternGroup.from(pattern, strategy);
        groups.put(pattern.id(), group);
        anchorIndecies.computeIfAbsent(pattern.anchor().getMaterial(), k -> new ArrayList<>()).add(group);
    }

    /**
     * Retrieves all pattern groups whose source anchor matches the provided material.
     * <p>
     * This is typically used for optimization, allowing listeners to only check patterns that match the block the user
     * just interacted with.
     *
     * @param material the material of the anchor
     * @return a list of matching pattern groups, or an empty list if none match
     * @since 1.0.0-SNAPSHOT
     */
    public List<MultiblockPatternGroup> getByAnchor(Material material) {
        return anchorIndecies.getOrDefault(material, Collections.emptyList());
    }

    /**
     * Removes a pattern from the manager.
     * <p>
     * Implementations should ensure this removal is reflected in persistence (e.g., deleting files) upon the next save
     * cycle.
     *
     * @param id the identifier of the pattern to remove
     * @since 1.0.0-SNAPSHOT
     */
    public void remove(String id) {
        final MultiblockPatternGroup removed = groups.remove(id);
        if (removed == null) {
            return;
        }

        final Material material = removed.source().anchor().getMaterial();
        List<MultiblockPatternGroup> list = anchorIndecies.get(material);
        if (list != null) {
            list.remove(removed);
            if (list.isEmpty()) {
                anchorIndecies.remove(material);
            }
        }

        if (!dirty.contains(id)) {
            dirty.add(id);
        }
    }

    /**
     * Loads multiblock patterns from the specified directory.
     *
     * @param dir the path to the directory containing pattern definitions
     * @throws RuntimeException if an I/O error occurs during reading
     * @since 1.0.0-SNAPSHOT
     */
    public void load(Path dir) {
        try (Stream<Path> entries = Files.list(dir)) {
            entries.forEach((file) -> {
                if (Files.isDirectory(file)) {
                    return;
                }

                try (var reader = Files.newBufferedReader(file)) {
                    final MultiblockPattern pattern = gson.fromJson(reader, MultiblockPattern.class);
                    add0(pattern);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to load multiblock pattern from " + file, e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Synchronizes the state of patterns to the provided folder.
     * <p>
     * This handles persisting new/modified patterns and deleting removed ones.
     *
     * @param folder the root folder where patterns are stored
     * @param unmark whether to clear the internal "dirty" state after saving
     * @throws RuntimeException if an I/O error occurs during writing or deletion
     * @since 1.0.0-SNAPSHOT
     */
    public void save(Path folder, boolean unmark) {
        try {
            if (Files.notExists(folder)) {
                Files.createDirectories(folder);
            }

            for (final String s : dirty) {
                final var group = groups.get(s);
                final Path target = folder.resolve(s + ".json");

                if (group == null) {
                    Files.deleteIfExists(target);
                    continue;
                }

                try (var writer = Files.newBufferedWriter(target)) {
                    gson.toJson(group.source(), MultiblockPattern.class, writer);
                }
            }
            if (unmark) dirty.clear();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save multiblock patterns", e);
        }
    }

    /**
     * Gets a list of all loaded pattern groups in this manager.
     *
     * @return a list of all groups
     * @since 1.0.0-SNAPSHOT
     */
    public List<MultiblockPatternGroup> groups() {
        return this.groups.values().stream().toList();
    }

    /**
     * Configures the rotation strategy used when generating groups from source patterns.
     * <p>
     * This allows injecting test strategies (e.g., to bypass Bukkit logic) or custom rotation behaviors.
     *
     * @param strategy the strategy to use
     * @since 1.0.0-SNAPSHOT
     */
    public void rotationStrategy(BlockRotationStrategy strategy) {
        this.strategy = strategy;
    }
}
