package sh.miles.pineapple.util.serialization.adapter.mock;

import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.util.serialization.SerializedDeserializeContext;
import sh.miles.pineapple.util.serialization.SerializedElement;
import sh.miles.pineapple.util.serialization.SerializedSerializeContext;
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapter;
import sh.miles.pineapple.util.serialization.exception.SerializedAdaptationException;

import java.util.ArrayList;
import java.util.List;

public class ComplexObjectMockAdapter implements SerializedAdapter<ComplexObjectMock> {

    @NotNull
    @Override
    public SerializedElement serialize(@NotNull final ComplexObjectMock obj, @NotNull final SerializedSerializeContext context) throws SerializedAdaptationException {
        final var object = SerializedElement.object();
        object.add("name", obj.name);
        object.add("age", obj.age);
        object.add("money", obj.money);

        final var propertiesArray = SerializedElement.array();
        obj.properties.forEach(propertiesArray::add);
        object.add("properties", propertiesArray);
        return object;
    }

    @NotNull
    @Override
    public ComplexObjectMock deserialize(@NotNull final SerializedElement element, @NotNull final SerializedDeserializeContext context) throws SerializedAdaptationException {
        final var object = element.getAsObject();
        final var name = object.getPrimitiveOrNull("name").getAsString();
        final var age = object.getPrimitiveOrNull("age").getAsInt();
        final var money = object.getPrimitiveOrNull("money").getAsDouble();
        final var propertiesArray = object.getArrayOrNull("properties");

        final List<String> properties = new ArrayList<>();
        propertiesArray.forEach((e) -> properties.add(e.getAsPrimitive().getAsString()));

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
