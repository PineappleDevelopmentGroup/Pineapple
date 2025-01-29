package sh.miles.pineapple.util.serialization.adapter.spec;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.util.serialization.SerializedDeserializeContext;
import sh.miles.pineapple.util.serialization.SerializedElement;
import sh.miles.pineapple.util.serialization.SerializedObject;
import sh.miles.pineapple.util.serialization.SerializedPrimitive;
import sh.miles.pineapple.util.serialization.SerializedSerializeContext;
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapter;
import sh.miles.pineapple.util.serialization.exception.SerializedAdaptationException;
import sh.miles.pineapple.util.spec.SoundSpec;

public class SoundSpecAdapter implements SerializedAdapter<SoundSpec> {

    private static final String SOUND = "sound";
    private static final String CATEGORY = "category";
    private static final String PITCH = "pitch";
    private static final String VOLUME = "volume";

    @NotNull
    @Override
    public SoundSpec deserialize(@NotNull final SerializedElement element, @NotNull final SerializedDeserializeContext context) throws SerializedAdaptationException {
        final SerializedObject parent = element.getAsObject();
        final Sound sound = Registry.SOUNDS.get(NamespacedKey.minecraft(parent.getPrimitive(SOUND).map(SerializedPrimitive::getAsString).orThrow("Missing required field %s".formatted(SOUND))));
        final SoundCategory category = SoundCategory.valueOf(parent.getPrimitive(CATEGORY).map(SerializedPrimitive::getAsString).map(String::toUpperCase).orThrow("Missing required field %s".formatted(CATEGORY)));
        final double pitch = parent.getPrimitive(PITCH).map(SerializedPrimitive::getAsDouble).orElse(1.0);
        final double volume = parent.getPrimitive(VOLUME).map(SerializedPrimitive::getAsDouble).orElse(1.0);
        return new SoundSpec(sound, category, (float) pitch, (float) volume);
    }

    @NotNull
    @Override
    public SerializedElement serialize(@NotNull final SoundSpec spec, @NotNull final SerializedSerializeContext context) throws SerializedAdaptationException {
        final SerializedObject parent = SerializedElement.object();
        parent.add(SOUND, spec.sound().getKey().toString());
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
