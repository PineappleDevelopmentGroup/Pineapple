package sh.miles.pineapple.util.serialization.adapter.bukkit;

import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.util.serialization.SerializedDeserializeContext;
import sh.miles.pineapple.util.serialization.SerializedElement;
import sh.miles.pineapple.util.serialization.SerializedSerializeContext;
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapter;
import sh.miles.pineapple.util.serialization.exception.SerializedAdaptationException;

class ArmorTrimAdapter implements SerializedAdapter<ArmorTrim> {

    static final String TRIM_MATERIAL = "material";
    static final String TRIM_PATTERN = "pattern";

    @NotNull
    @Override
    public SerializedElement serialize(@NotNull final ArmorTrim trim, @NotNull final SerializedSerializeContext context) throws SerializedAdaptationException {
        final var object = SerializedElement.object();
        object.add(TRIM_MATERIAL, context.serialize(trim.getMaterial()));
        object.add(TRIM_PATTERN, context.serialize(trim.getPattern()));
        return object;
    }

    @NotNull
    @Override
    public ArmorTrim deserialize(@NotNull final SerializedElement element, @NotNull final SerializedDeserializeContext context) throws SerializedAdaptationException {
        final var object = SerializedElement.object();
        final TrimMaterial material = context.deserialize(object.get(TRIM_MATERIAL).orThrow(), TrimMaterial.class);
        final TrimPattern pattern = context.deserialize(object.get(TRIM_PATTERN).orThrow(), TrimPattern.class);
        return new ArmorTrim(material, pattern);
    }

    @Override
    public Class<?> getKey() {
        return ArmorTrim.class;
    }
}
