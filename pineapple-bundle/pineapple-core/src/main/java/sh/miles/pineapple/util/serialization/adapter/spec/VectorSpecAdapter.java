package sh.miles.pineapple.util.serialization.adapter.spec;

import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.util.serialization.SerializedDeserializeContext;
import sh.miles.pineapple.util.serialization.SerializedElement;
import sh.miles.pineapple.util.serialization.SerializedObject;
import sh.miles.pineapple.util.serialization.SerializedPrimitive;
import sh.miles.pineapple.util.serialization.SerializedSerializeContext;
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapter;
import sh.miles.pineapple.util.serialization.exception.SerializedAdaptationException;
import sh.miles.pineapple.util.spec.VectorSpec;

final class VectorSpecAdapter implements SerializedAdapter<VectorSpec> {

    private static final String X = "x";
    private static final String Y = "y";
    private static final String Z = "z";

    @NotNull
    @Override
    public VectorSpec deserialize(@NotNull final SerializedElement element, @NotNull final SerializedDeserializeContext context) throws SerializedAdaptationException {
        final SerializedObject parent = element.getAsObject();
        final var x = parent.getPrimitive(X).map(SerializedPrimitive::getAsDouble).orElse(0.0);
        final var y = parent.getPrimitive(Y).map(SerializedPrimitive::getAsDouble).orElse(0.0);
        final var z = parent.getPrimitive(Z).map(SerializedPrimitive::getAsDouble).orElse(0.0);
        return new VectorSpec(x, y, z);
    }

    @NotNull
    @Override
    public SerializedElement serialize(@NotNull final VectorSpec spec, @NotNull final SerializedSerializeContext context) throws SerializedAdaptationException {
        final SerializedObject parent = SerializedElement.object();
        parent.add(X, spec.x());
        parent.add(Y, spec.y());
        parent.add(Z, spec.z());
        return parent;
    }

    @Override
    public Class<?> getKey() {
        return VectorSpec.class;
    }
}
