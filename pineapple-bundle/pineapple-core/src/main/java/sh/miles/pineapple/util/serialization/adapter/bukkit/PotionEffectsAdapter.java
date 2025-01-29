package sh.miles.pineapple.util.serialization.adapter.bukkit;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.function.Option.Some;
import sh.miles.pineapple.util.serialization.SerializedDeserializeContext;
import sh.miles.pineapple.util.serialization.SerializedElement;
import sh.miles.pineapple.util.serialization.SerializedObject;
import sh.miles.pineapple.util.serialization.SerializedPrimitive;
import sh.miles.pineapple.util.serialization.SerializedSerializeContext;
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapter;
import sh.miles.pineapple.util.serialization.exception.SerializedAdaptationException;

class PotionEffectsAdapter implements SerializedAdapter<PotionEffect> {

    static final String TYPE = "type";
    static final String DURATION = "duration";
    static final String AMPLIFIER = "amplifier";
    static final String PARTICLES = "particles";
    static final String ICON = "icon";

    @NotNull
    @Override
    public SerializedElement serialize(@NotNull final PotionEffect effect, @NotNull final SerializedSerializeContext context) throws SerializedAdaptationException {
        final SerializedObject object = SerializedElement.object();
        object.add(TYPE, context.serialize(effect.getType()));
        object.add(DURATION, effect.getDuration());
        object.add(AMPLIFIER, effect.getAmplifier());
        object.add(PARTICLES, effect.hasParticles());
        object.add(ICON, effect.hasIcon());
        return object;
    }

    @NotNull
    @Override
    public PotionEffect deserialize(@NotNull final SerializedElement element, @NotNull final SerializedDeserializeContext context) throws SerializedAdaptationException {
        final SerializedObject object = element.getAsObject();
        final PotionEffectType type = context.deserialize(object.get(TYPE).orThrow(), PotionEffectType.class);
        final int duration = ((object.getPrimitive(DURATION)) instanceof Some<SerializedPrimitive> some) ? some.some().getAsInt() : 0;
        final int amplifier = ((object.getPrimitive(AMPLIFIER)) instanceof Some<SerializedPrimitive> some) ? some.some().getAsInt() : 0;
        final boolean particles = (object.getPrimitive(PARTICLES)) instanceof Some<SerializedPrimitive> some && some.some().getAsBoolean();
        final boolean icon = (object.getPrimitive(ICON) instanceof Some<SerializedPrimitive> some && some.some().getAsBoolean());
        return new PotionEffect(type, duration, amplifier, particles, icon);
    }

    @Override
    public Class<?> getKey() {
        return PotionEffect.class;
    }
}
