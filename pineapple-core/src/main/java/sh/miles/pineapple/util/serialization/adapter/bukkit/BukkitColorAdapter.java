package sh.miles.pineapple.util.serialization.adapter.bukkit;

import com.google.common.primitives.Ints;
import org.bukkit.Color;
import sh.miles.pineapple.util.serialization.SerializedDeserializeContext;
import sh.miles.pineapple.util.serialization.SerializedElement;
import sh.miles.pineapple.util.serialization.SerializedSerializeContext;
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapter;
import sh.miles.pineapple.util.serialization.exception.SerializedAdaptationException;

class BukkitColorAdapter implements SerializedAdapter<Color> {

    @Override
    public SerializedElement serialize(final Color obj, final SerializedSerializeContext context) throws SerializedAdaptationException {
        return SerializedElement.primitive("#" + Integer.toHexString(obj.asRGB()));
    }


    @Override
    public Color deserialize(final SerializedElement element, final SerializedDeserializeContext context) throws SerializedAdaptationException {
        return Color.fromRGB(Ints.tryParse(element.getAsPrimitive().getAsString().replace("#", ""), 16));
    }

    @Override
    public Class<?> getKey() {
        return Color.class;
    }
}
