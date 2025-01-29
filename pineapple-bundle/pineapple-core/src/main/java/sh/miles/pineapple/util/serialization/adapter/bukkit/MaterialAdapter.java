package sh.miles.pineapple.util.serialization.adapter.bukkit;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.util.serialization.SerializedDeserializeContext;
import sh.miles.pineapple.util.serialization.SerializedElement;
import sh.miles.pineapple.util.serialization.SerializedSerializeContext;
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapter;
import sh.miles.pineapple.util.serialization.exception.SerializedAdaptationException;

class MaterialAdapter implements SerializedAdapter<Material> {

    @NotNull
    @Override
    public SerializedElement serialize(@NotNull final Material obj, @NotNull final SerializedSerializeContext context) throws SerializedAdaptationException {
        return SerializedElement.primitive(obj.toString().toLowerCase());
    }

    @NotNull
    @Override
    public Material deserialize(@NotNull final SerializedElement element, @NotNull final SerializedDeserializeContext context) throws SerializedAdaptationException {
        if (element.isPrimitive()) {
            final String stringy = element.getAsPrimitive().getAsString();
            final Material material = Material.matchMaterial(stringy);
            if (material == null) {
                throw new SerializedAdaptationException("The material %s does not exist!".formatted(stringy));
            }
            return material;
        }
        throw new SerializedAdaptationException("Material's must be adapted from primitive strings");
    }

    @Override
    public Class<?> getKey() {
        return Material.class;
    }
}
