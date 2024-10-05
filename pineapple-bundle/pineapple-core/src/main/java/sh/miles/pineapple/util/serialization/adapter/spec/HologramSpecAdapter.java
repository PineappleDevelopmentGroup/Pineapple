package sh.miles.pineapple.util.serialization.adapter.spec;

import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.chat.PineappleChat;
import sh.miles.pineapple.chat.PineappleComponent;
import sh.miles.pineapple.util.serialization.SerializedArray;
import sh.miles.pineapple.util.serialization.SerializedDeserializeContext;
import sh.miles.pineapple.util.serialization.SerializedElement;
import sh.miles.pineapple.util.serialization.SerializedObject;
import sh.miles.pineapple.util.serialization.SerializedSerializeContext;
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapter;
import sh.miles.pineapple.util.serialization.exception.SerializedAdaptationException;
import sh.miles.pineapple.util.spec.HologramSpec;
import sh.miles.pineapple.util.spec.VectorSpec;

import java.util.Arrays;

final class HologramSpecAdapter implements SerializedAdapter<HologramSpec> {

    private static final String TEXT = "text";
    private static final String VECTOR = "relative_position";

    @NotNull
    @Override
    public HologramSpec deserialize(@NotNull final SerializedElement element, @NotNull final SerializedDeserializeContext context) throws SerializedAdaptationException {
        final SerializedObject parent = element.getAsObject();
        final PineappleComponent text = PineappleChat.component(parent.getArray(TEXT).orElse(SerializedElement.array())
                .stream()
                .map((e) -> e.getAsPrimitive().getAsString())
                .reduce("", (first, second) -> first + "\n" + second));
        final VectorSpec vector = parent.getObject(VECTOR)
                .map((raw) -> context.deserialize(raw, VectorSpec.class))
                .orElse(new VectorSpec(0.0, 0.0, 0.0));
        return new HologramSpec(text, vector);
    }

    @NotNull
    @Override
    public SerializedElement serialize(@NotNull final HologramSpec spec, @NotNull final SerializedSerializeContext context) throws SerializedAdaptationException {
        final SerializedObject parent = SerializedElement.object();
        final SerializedArray text = spec.hologramText().getSource().transform((s) -> Arrays.stream(s.split("\n"))).collect(
                SerializedElement::array,
                SerializedArray::add,
                (array1, array2) -> array2.forEach(array1::add)
        );
        final SerializedElement vector = context.serialize(spec.offset(), VectorSpec.class);
        parent.add(TEXT, text);
        parent.add(VECTOR, vector);
        return parent;
    }

    @Override
    public Class<?> getKey() {
        return HologramSpec.class;
    }
}
