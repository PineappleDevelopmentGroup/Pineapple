package sh.miles.pineapple.util.serialization.adapter;

import org.junit.jupiter.api.Test;
import sh.miles.pineapple.util.serialization.Serialized;
import sh.miles.pineapple.util.serialization.SerializedElement;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UUIDAdapterTest {

    private static final UUIDAdapter adapter = new UUIDAdapter();

    @Test
    public void test_Should_Go_Into_SerializedPrimitive() {
        final UUID initial = UUID.randomUUID();
        final SerializedElement element = adapter.serialize(initial, Serialized.INSTANCE);
        assertNotNull(element, "the element was null");
        assertTrue(element.isPrimitive());
        final UUID deserialized = adapter.deserialize(element, Serialized.INSTANCE);
        assertEquals(initial, deserialized);
    }
}
