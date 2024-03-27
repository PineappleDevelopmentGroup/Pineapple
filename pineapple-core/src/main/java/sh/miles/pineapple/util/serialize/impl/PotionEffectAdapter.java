package sh.miles.pineapple.util.serialize.impl;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.util.serialize.GenericSerializer;
import sh.miles.pineapple.util.serialize.exception.FieldNotFoundException;

import java.util.HashMap;
import java.util.Map;

class PotionEffectAdapter implements GenericSerializer<PotionEffect> {

    public static final String TYPE_KEY = "type";
    public static final String DURATION_KEY = "duration";
    public static final String AMPLIFIER_KEY = "amplifier";
    public static final String AMBIENT_KEY = "ambient";
    public static final String PARTICLES_KEY = "particles";
    public static final String ICON_KEY = "icon";

    @NotNull
    @Override
    public Map<String, Object> serialize(final PotionEffect target) {
        final Map<String, Object> map = new HashMap<>();
        map.put(TYPE_KEY, target.getType().getKey().getKey().toString());
        map.put(DURATION_KEY, target.getDuration());
        map.put(AMPLIFIER_KEY, target.getAmplifier());
        if (target.isAmbient()) {
            map.put(AMBIENT_KEY, target.isAmbient());
        }
        if (target.hasParticles()) {
            map.put(PARTICLES_KEY, target.hasParticles());
        }
        if (target.hasIcon()) {
            map.put(ICON_KEY, target.hasIcon());
        }
        return map;
    }

    @NotNull
    @Override
    public PotionEffect deserialize(@NotNull final Map<String, Object> map) throws FieldNotFoundException {
        final PotionEffectType potionEffectType = Registry.EFFECT.get(NamespacedKey.fromString((String) map.get(TYPE_KEY)));
        final int duration = (int) map.get(DURATION_KEY);
        final int amplifier = (int) map.get(AMPLIFIER_KEY);

        boolean ambient = true;
        if (map.containsKey(AMBIENT_KEY)) {
            ambient = (boolean) map.get(AMBIENT_KEY);
        }
        boolean particles = true;
        if (map.containsKey(PARTICLES_KEY)) {
            particles = (boolean) map.get(PARTICLES_KEY);
        }
        boolean icon = true;
        if (map.containsKey(ICON_KEY)) {
            icon = (boolean) map.get(ICON_KEY);
        }
        return new PotionEffect(potionEffectType, duration, amplifier, ambient, particles, icon);
    }

    @NotNull
    @Override
    public Class<PotionEffect> getTypeClass() {
        return PotionEffect.class;
    }
}
