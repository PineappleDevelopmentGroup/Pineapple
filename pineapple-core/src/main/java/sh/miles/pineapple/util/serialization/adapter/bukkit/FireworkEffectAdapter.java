package sh.miles.pineapple.util.serialization.adapter.bukkit;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.util.serialization.SerializedArray;
import sh.miles.pineapple.util.serialization.SerializedDeserializeContext;
import sh.miles.pineapple.util.serialization.SerializedElement;
import sh.miles.pineapple.util.serialization.SerializedObject;
import sh.miles.pineapple.util.serialization.SerializedPrimitive;
import sh.miles.pineapple.util.serialization.SerializedSerializeContext;
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapter;
import sh.miles.pineapple.util.serialization.exception.SerializedAdaptationException;

import java.util.ArrayList;
import java.util.List;

final class FireworkEffectAdapter implements SerializedAdapter<FireworkEffect> {

    private static final String FLICKER = "flicker";
    private static final String TRAIL = "trail";
    private static final String TYPE = "type";
    private static final String COLORS = "colors";
    private static final String FADE_COLORS = "fade_colors";

    @NotNull
    @Override
    public FireworkEffect deserialize(@NotNull final SerializedElement element, @NotNull final SerializedDeserializeContext context) throws SerializedAdaptationException {
        final SerializedObject parent = element.getAsObject();
        final boolean flicker = parent.getPrimitive(FLICKER).map(SerializedPrimitive::getAsBoolean).orElse(false);
        final boolean trail = parent.getPrimitive(TRAIL).map(SerializedPrimitive::getAsBoolean).orElse(false);
        final FireworkEffect.Type type = parent.getPrimitive(TYPE)
                .map(SerializedPrimitive::getAsString)
                .map(String::toUpperCase)
                .map(FireworkEffect.Type::valueOf)
                .orThrow("Missing required field %s".formatted(TYPE));
        final List<Color> colors = parent.getArray(COLORS).map((array) ->
                array.stream().map((prim) -> context.deserialize(prim, Color.class)).toList()
        ).orElse(new ArrayList<>());
        final List<Color> fadeColors = parent.getArray(FADE_COLORS).map((array) ->
                array.stream().map((prim) -> context.deserialize(prim, Color.class)).toList()
        ).orElse(new ArrayList<>());
        return FireworkEffect.builder()
                .flicker(flicker)
                .trail(trail)
                .with(type)
                .withColor(colors)
                .withFade(fadeColors)
                .build();
    }

    @NotNull
    @Override
    public SerializedElement serialize(@NotNull final FireworkEffect obj, @NotNull final SerializedSerializeContext context) throws SerializedAdaptationException {
        final SerializedObject parent = SerializedElement.object();
        if (obj.hasFlicker()) {
            parent.add(FLICKER, true);
        }
        if (obj.hasTrail()) {
            parent.add(TRAIL, true);
        }
        parent.add(TYPE, obj.getType().name());
        parent.add(COLORS, withColors(obj.getColors(), context));
        parent.add(FADE_COLORS, withColors(obj.getFadeColors(), context));
        return parent;
    }

    private SerializedArray withColors(List<Color> colors, SerializedSerializeContext context) {
        final SerializedArray array = SerializedElement.array();
        for (final Color color : colors) {
            array.add(context.serialize(color, Color.class));
        }
        return array;
    }

    @Override
    public Class<?> getKey() {
        return FireworkEffect.class;
    }
}
