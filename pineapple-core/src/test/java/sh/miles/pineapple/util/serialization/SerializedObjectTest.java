package sh.miles.pineapple.util.serialization;

import org.junit.jupiter.api.Test;

public class SerializedObjectTest {

    @Test
    void test_ShouldPrintOut() {
        SerializedObject object = new SerializedObject();
        object.add("key", "value");
        final SerializedArray array = new SerializedArray();
        array.add(1);
        array.add(2);
        array.add(3);
        object.add("list", array);
        final SerializedObject nested = new SerializedObject();
        nested.add("test", "answer");
        nested.add("other_test", "other answer");
        object.add("nested", nested);

        System.out.println(object);
    }

}
