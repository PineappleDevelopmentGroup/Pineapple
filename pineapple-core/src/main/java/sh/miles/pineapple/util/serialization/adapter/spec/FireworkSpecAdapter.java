package sh.miles.pineapple.util.serialization.adapter.spec;

import org.bukkit.FireworkEffect;
import org.jspecify.annotations.NullMarked;
import sh.miles.pineapple.util.serialization.SerializedArray;
import sh.miles.pineapple.util.serialization.SerializedDeserializeContext;
import sh.miles.pineapple.util.serialization.SerializedElement;
import sh.miles.pineapple.util.serialization.SerializedObject;
import sh.miles.pineapple.util.serialization.SerializedPrimitive;
import sh.miles.pineapple.util.serialization.SerializedSerializeContext;
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapter;
import sh.miles.pineapple.util.serialization.exception.SerializedAdaptationException;
import sh.miles.pineapple.util.spec.FireworkSpec;

import java.util.List;

@NullMarked
final class FireworkSpecAdapter implements SerializedAdapter<FireworkSpec> {

    private static final String EFFECTS = "effects";
    private static final String POWER = "power";

    @Override
    public FireworkSpec deserialize(final SerializedElement element, final SerializedDeserializeContext context) throws SerializedAdaptationException {
        final SerializedObject parent = element.getAsObject();
        final int power = parent.getPrimitive(POWER).map(SerializedPrimitive::getAsInt)
            .orThrow("Missing required field %s".formatted(POWER));
        final List<FireworkEffect> effects = parent.getArray(EFFECTS).map((array) ->
            array.stream().map((e) -> context.deserialize(e, FireworkEffect.class)).toList()
        ).orThrow("Missing required field %s".formatted(POWER));
        return new FireworkSpec(effects, power);
    }

    @Override
    public SerializedElement serialize(final FireworkSpec spec, final SerializedSerializeContext context) throws SerializedAdaptationException {
        final SerializedObject parent = SerializedElement.object();
        parent.add(POWER, spec.power());
        final SerializedArray array = SerializedElement.array();
        for (final FireworkEffect effect : spec.effects()) {
            array.add(context.serialize(effect, FireworkEffect.class));
        }
        parent.add(EFFECTS, array);
        return parent;
    }

    @Override
    public Class<?> getKey() {
        return FireworkSpec.class;
    }
}
