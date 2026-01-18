package sh.miles.pineapple.util.serialization.adapter.spec;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.jspecify.annotations.NullMarked;
import sh.miles.pineapple.util.serialization.SerializedDeserializeContext;
import sh.miles.pineapple.util.serialization.SerializedElement;
import sh.miles.pineapple.util.serialization.SerializedObject;
import sh.miles.pineapple.util.serialization.SerializedPrimitive;
import sh.miles.pineapple.util.serialization.SerializedSerializeContext;
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapter;
import sh.miles.pineapple.util.serialization.exception.SerializedAdaptationException;
import sh.miles.pineapple.util.spec.SoundSpec;

@NullMarked
public class SoundSpecAdapter implements SerializedAdapter<SoundSpec> {

    private static final String SOUND = "sound";
    private static final String CATEGORY = "category";
    private static final String PITCH = "pitch";
    private static final String VOLUME = "volume";

    @Override
    public SoundSpec deserialize(final SerializedElement element, final SerializedDeserializeContext context) throws SerializedAdaptationException {
        final SerializedObject parent = element.getAsObject();
        final Sound sound = Registry.SOUNDS.getOrThrow(
            context.deserialize(parent.getPrimitive(SOUND).orThrow("Missing required field %s".formatted(SOUND)),
                NamespacedKey.class
            ));
        final SoundCategory category = SoundCategory.valueOf(
            parent.getPrimitive(CATEGORY).map(SerializedPrimitive::getAsString).map(String::toUpperCase)
                .orThrow("Missing required field %s".formatted(CATEGORY)));
        final double pitch = parent.getPrimitive(PITCH).map(SerializedPrimitive::getAsDouble).orElse(1.0);
        final double volume = parent.getPrimitive(VOLUME).map(SerializedPrimitive::getAsDouble).orElse(1.0);
        return new SoundSpec(sound, category, (float) pitch, (float) volume);
    }

    @Override
    public SerializedElement serialize(final SoundSpec spec, final SerializedSerializeContext context) throws SerializedAdaptationException {
        final SerializedObject parent = SerializedElement.object();
        parent.add(SOUND, context.serialize(Registry.SOUND_EVENT.getKeyOrThrow(spec.sound())));
        parent.add(CATEGORY, spec.category().name());
        parent.add(PITCH, spec.pitch());
        parent.add(VOLUME, spec.volume());
        return parent;
    }

    @Override
    public Class<?> getKey() {
        return SoundSpec.class;
    }
}
