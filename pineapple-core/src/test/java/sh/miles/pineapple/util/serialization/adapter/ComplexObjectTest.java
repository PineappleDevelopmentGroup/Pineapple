package sh.miles.pineapple.util.serialization.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sh.miles.pineapple.util.serialization.Serialized;
import sh.miles.pineapple.util.serialization.SerializedArray;
import sh.miles.pineapple.util.serialization.SerializedObject;
import sh.miles.pineapple.util.serialization.adapter.mock.ComplexObjectMock;
import sh.miles.pineapple.util.serialization.adapter.mock.ComplexObjectMockAdapter;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static sh.miles.pineapple.util.serialization.SerializedElement.array;
import static sh.miles.pineapple.util.serialization.SerializedElement.object;

public class ComplexObjectTest {

    @BeforeEach
    void setup() {
        SerializedAdapterRegistry.INSTANCE.register(new ComplexObjectMockAdapter());
    }

    @Test
    void test_Should_Serialize_Complex_Object() {
        final SerializedObject expected = object();
        expected.add("name", "Joe");
        expected.add("age", 32);
        expected.add("money", 5050.50);
        final SerializedArray temp = array();
        temp.add("North");
        temp.add("South");
        expected.add("properties", temp);

        final ComplexObjectMock toSerialize = new ComplexObjectMock();
        toSerialize.name = "Joe";
        toSerialize.age = 32;
        toSerialize.money = 5050.50;
        toSerialize.properties = List.of("North", "South");

        final var serialized = Serialized.INSTANCE.serialize(toSerialize, ComplexObjectMock.class);
        assertEquals(expected, serialized);
    }


    @Test
    void test_Should_Deserialize_Complex_Object() {

        final ComplexObjectMock expected = new ComplexObjectMock();
        expected.name = "Joe";
        expected.age = 32;
        expected.money = 5050.50;
        expected.properties = List.of("North", "South");

        final SerializedObject toDeserialize = object();
        toDeserialize.add("name", "Joe");
        toDeserialize.add("age", 32);
        toDeserialize.add("money", 5050.50);
        final SerializedArray temp = array();
        temp.add("North");
        temp.add("South");
        toDeserialize.add("properties", temp);

        final var deserialized = Serialized.INSTANCE.deserialize(toDeserialize, ComplexObjectMock.class);
        assertEquals(expected, deserialized);
    }
}
