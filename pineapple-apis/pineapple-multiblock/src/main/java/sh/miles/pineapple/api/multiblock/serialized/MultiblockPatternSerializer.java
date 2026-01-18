package sh.miles.pineapple.api.multiblock.serialized;

import it.unimi.dsi.fastutil.chars.Char2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockType;
import org.bukkit.block.data.BlockData;
import org.jspecify.annotations.NullMarked;
import sh.miles.pineapple.api.multiblock.MultiblockPattern;
import sh.miles.pineapple.api.multiblock.MultiblockPattern.Builder;
import sh.miles.pineapple.api.multiblock.util.MultiblockBounds3d;
import sh.miles.pineapple.util.serialization.SerializedArray;
import sh.miles.pineapple.util.serialization.SerializedDeserializeContext;
import sh.miles.pineapple.util.serialization.SerializedElement;
import sh.miles.pineapple.util.serialization.SerializedObject;
import sh.miles.pineapple.util.serialization.SerializedSerializeContext;
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapter;
import sh.miles.pineapple.util.serialization.exception.SerializedAdaptationException;

import java.util.HashMap;
import java.util.Map;

import static sh.miles.pineapple.util.serialization.SerializedElement.array;
import static sh.miles.pineapple.util.serialization.SerializedElement.object;

@NullMarked
public class MultiblockPatternSerializer implements SerializedAdapter<MultiblockPattern> {

    private static final String ID = "id";
    private static final String ANCHOR = "anchor";
    private static final String ORIGIN = "origin";
    private static final String LAYERS = "layers";
    private static final String PIVOT = "pivot";
    private static final String PALETTE = "palette";
    private static final String X = "x";
    private static final String Y = "y";
    private static final String Z = "z";

    @Override
    public SerializedElement serialize(MultiblockPattern src, SerializedSerializeContext context) throws SerializedAdaptationException {
        final SerializedObject parent = object();
        parent.add(ID, src.id());
        parent.add(ANCHOR, src.anchor().getAsString());

        if (src.originX() != 0 || src.originY() != 0 || src.originZ() != 0) {
            final SerializedObject pivot = object();
            pivot.add(X, src.originX());
            pivot.add(Y, src.originY());
            pivot.add(Z, src.originZ());
            parent.add(PIVOT, pivot);
        }

        MultiblockBounds3d bounds = src.bounds();
        if (!bounds.valid()) {
            throw new SerializedAdaptationException("unable to serialize invalid bounds");
        }

        int offsetX = -bounds.minX();
        int offsetY = -bounds.minY();
        int offsetZ = -bounds.minZ();

        final SerializedObject origin = object();
        origin.add(X, offsetX);
        origin.add(Y, offsetY);
        origin.add(Z, offsetZ);
        parent.add(ORIGIN, origin);

        final Char2ObjectLinkedOpenHashMap<BlockData> paletteMap = src.exportPalette('A');
        final Map<BlockData, Character> reversePalette = new HashMap<>();
        final SerializedObject palette = object();
        palette.add(" ", "minecraft:air");

        for (final Char2ObjectMap.Entry<BlockData> entry : paletteMap.char2ObjectEntrySet()) {
            palette.add(String.valueOf(entry.getCharKey()), entry.getValue().getAsString());
            reversePalette.put(entry.getValue(), entry.getCharKey());
        }
        parent.add(PALETTE, palette);

        SerializedArray layers = array();
        BlockData airData = BlockType.AIR.createBlockData();

        for (int y = bounds.minY(); y <= bounds.maxY(); y++) {
            SerializedArray layer = array();
            for (int z = bounds.minZ(); z <= bounds.maxZ(); z++) {
                StringBuilder row = new StringBuilder();
                for (int x = bounds.minX(); x <= bounds.maxX(); x++) {
                    BlockData data = src.blockDataAt(x, y, z);
                    if (data == null || data.getMaterial().isAir() || data.equals(airData)) {
                        row.append(' ');
                    } else {
                        row.append(reversePalette.getOrDefault(data, '?'));
                    }
                }
                layer.add(row.toString());
            }
            layers.add(layer);
        }

        parent.add(LAYERS, layers);
        return parent;
    }

    @Override
    public MultiblockPattern deserialize(SerializedElement element, SerializedDeserializeContext context) throws SerializedAdaptationException {
        final var parent = element.getAsObject();
        final Builder builder = MultiblockPattern.builder();

        final var originS = parent.getObject(ORIGIN)
            .orThrow("MultiblockPattern must have \"%s\" field".formatted(ORIGIN));

        final int offsetX = originS.getPrimitive(X).orThrow("origin x must exist").getAsInt();
        final int offsetY = originS.getPrimitive(Y).orThrow("origin y must exist").getAsInt();
        final int offsetZ = originS.getPrimitive(Z).orThrow("origin z must exist").getAsInt();

        builder
            .id(parent.getPrimitive(ID).orThrow("MultiblockPattern must have \"%s\" field".formatted(ID)).getAsString())
            .anchor(Bukkit.getServer().createBlockData(
                parent.getPrimitive(ANCHOR).orThrow("MultiblockPattern must have \"%s\" field".formatted(ANCHOR))
                    .getAsString()
            ));

        if (parent.has(PIVOT)) {
            final var pivot = parent.getObject(PIVOT).orThrow();
            builder.origin(
                pivot.getPrimitive(X).orThrow("pivot x must exist").getAsInt(),
                pivot.getPrimitive(Y).orThrow("pivot y must exist").getAsInt(),
                pivot.getPrimitive(Z).orThrow("pivot z must exist").getAsInt(),
                0
            );
        } else {
            builder.origin(0, 0, 0, 0);
        }

        final var maps = parent.getObject(PALETTE)
            .orThrow("MultiblockPattern must have \"%s\" field".formatted(PALETTE));
        for (final String key : maps.keySet()) {
            if (key.length() != 1) {
                throw new SerializedAdaptationException("Palette key smust be only a single character");
            }
            builder.paletteAppend(key.charAt(0), Bukkit.getServer().createBlockData(
                    maps.getPrimitive(key).orThrow("key must map to string block data").getAsString()
                )
            );
        }

        final SerializedArray layers = parent.getArray(LAYERS).orThrow("Layers must be an array");
        boolean isEmpty = true;

        for (int y = 0; y < layers.size(); y++) {
            final SerializedArray row = layers.get(y).getAsArray();
            for (int z = 0; z < row.size(); z++) {
                final String rowString = row.get(z).getAsPrimitive().getAsString();
                for (int x = 0; x < rowString.length(); x++) {
                    final char key = rowString.charAt(x);
                    if (key == ' ') continue;

                    builder.structureAppend(x - offsetX, y - offsetY, z - offsetZ, key);
                    isEmpty = false;
                }
            }
        }

        if (isEmpty) {
            throw new IllegalStateException("Structure definition is empty or contains only air");
        }

        return builder.build();
    }

    @Override
    public Class<?> getKey() {
        return MultiblockPattern.class;
    }
}
