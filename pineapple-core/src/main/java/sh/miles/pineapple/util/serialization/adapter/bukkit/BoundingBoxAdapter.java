package sh.miles.pineapple.util.serialization.adapter.bukkit;

import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.util.serialization.SerializedDeserializeContext;
import sh.miles.pineapple.util.serialization.SerializedElement;
import sh.miles.pineapple.util.serialization.SerializedObject;
import sh.miles.pineapple.util.serialization.SerializedPrimitive;
import sh.miles.pineapple.util.serialization.SerializedSerializeContext;
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapter;
import sh.miles.pineapple.util.serialization.exception.SerializedAdaptationException;

final class BoundingBoxAdapter implements SerializedAdapter<BoundingBox> {

    private static final String MIN_X = "minX";
    private static final String MIN_Y = "minY";
    private static final String MIN_Z = "minZ";
    private static final String MAX_X = "maxX";
    private static final String MAX_Y = "maxY";
    private static final String MAX_Z = "maxZ";

    private static final String ERROR = "Missing required field for BoundingBox %s";

    @NotNull
    @Override
    public BoundingBox deserialize(@NotNull final SerializedElement element, @NotNull final SerializedDeserializeContext context) throws SerializedAdaptationException {
        final SerializedObject parent = element.getAsObject();
        final var minX = parent.getPrimitive(MIN_X).map(SerializedPrimitive::getAsDouble).orThrow(ERROR.formatted(MIN_X));
        final var minY = parent.getPrimitive(MIN_Y).map(SerializedPrimitive::getAsDouble).orThrow(ERROR.formatted(MIN_Y));
        final var minZ = parent.getPrimitive(MIN_Z).map(SerializedPrimitive::getAsDouble).orThrow(ERROR.formatted(MIN_Z));
        final var maxX = parent.getPrimitive(MAX_X).map(SerializedPrimitive::getAsDouble).orThrow(ERROR.formatted(MAX_X));
        final var maxY = parent.getPrimitive(MAX_Y).map(SerializedPrimitive::getAsDouble).orThrow(ERROR.formatted(MAX_Y));
        final var maxZ = parent.getPrimitive(MAX_Z).map(SerializedPrimitive::getAsDouble).orThrow(ERROR.formatted(MAX_Z));

        return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @NotNull
    @Override
    public SerializedElement serialize(@NotNull final BoundingBox box, @NotNull final SerializedSerializeContext context) throws SerializedAdaptationException {
        final SerializedObject parent = SerializedElement.object();
        parent.add(MIN_X, box.getMinX());
        parent.add(MIN_Y, box.getMinY());
        parent.add(MIN_Z, box.getMinZ());
        parent.add(MAX_X, box.getMaxX());
        parent.add(MAX_Y, box.getMaxY());
        parent.add(MAX_Z, box.getMaxZ());
        return parent;
    }

    @Override
    public Class<?> getKey() {
        return BoundingBox.class;
    }
}
