package sh.miles.pineapple.util.serialization.adapter.bukkit;

import com.google.common.primitives.Ints;
import org.bukkit.Color;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.util.serialization.SerializedDeserializeContext;
import sh.miles.pineapple.util.serialization.SerializedElement;
import sh.miles.pineapple.util.serialization.SerializedSerializeContext;
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapter;
import sh.miles.pineapple.util.serialization.exception.SerializedAdaptationException;

class BukkitColorAdapter implements SerializedAdapter<Color> {

    @NotNull
    @Override
    public SerializedElement serialize(@NotNull final Color obj, @NotNull final SerializedSerializeContext context) throws SerializedAdaptationException {
        return SerializedElement.primitive("#" + Integer.toHexString(obj.asRGB()));
    }

    @NotNull
    @Override
    public Color deserialize(@NotNull final SerializedElement element, @NotNull final SerializedDeserializeContext context) throws SerializedAdaptationException {
        return Color.fromRGB(Ints.tryParse(element.getAsPrimitive().getAsString().replace("#", ""), 16));
    }

    @Override
    public Class<?> getKey() {
        return Color.class;
    }
}
