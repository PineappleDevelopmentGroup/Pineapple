package sh.miles.pineapple.util.serialization.adapter.mock;

import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.util.serialization.SerializedDeserializeContext;
import sh.miles.pineapple.util.serialization.SerializedElement;
import sh.miles.pineapple.util.serialization.SerializedObject;
import sh.miles.pineapple.util.serialization.SerializedSerializeContext;
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapter;
import sh.miles.pineapple.util.serialization.exception.SerializedAdaptationException;

import java.util.ArrayList;

import static sh.miles.pineapple.util.serialization.SerializedElement.object;

public class MalformedComplexObjectMockAdapter implements SerializedAdapter<ComplexObjectMock> {

    @NotNull
    @Override
    public SerializedElement serialize(@NotNull final ComplexObjectMock mock, @NotNull final SerializedSerializeContext context) throws SerializedAdaptationException {
        final SerializedObject object = object();
        object.add("name", mock.name);
        object.add("age", mock.age);
        object.add("money", mock.money);
        // SUPPOSED TO LOSE PROPERTIES ATTRIBUTE
        return object;
    }

    @NotNull
    @Override
    public ComplexObjectMock deserialize(@NotNull final SerializedElement element, @NotNull final SerializedDeserializeContext context) throws SerializedAdaptationException {
        final SerializedObject parent = element.getAsObject();
        final var name = parent.getPrimitiveOrNull("name").getAsString();
        final var age = parent.getPrimitiveOrNull("age").getAsInt();
        final var money = parent.getPrimitiveOrNull("money").getAsDouble();
        final var properties = new ArrayList<String>();
        if (parent.has("properties")) {
            parent.getArray("properties").orThrow().forEach((e) -> properties.add(e.getAsPrimitive().getAsString()));
        }

        final ComplexObjectMock mock = new ComplexObjectMock();
        mock.name = name;
        mock.age = age;
        mock.money = money;
        mock.properties = properties;
        return mock;
    }

    @Override
    public Class<?> getKey() {
        return ComplexObjectMock.class;
    }
}
