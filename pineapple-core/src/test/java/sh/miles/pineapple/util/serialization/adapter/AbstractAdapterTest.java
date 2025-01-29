package sh.miles.pineapple.util.serialization.adapter;

import org.junit.jupiter.api.Test;
import sh.miles.pineapple.BukkitTest;
import sh.miles.pineapple.collection.Pair;
import sh.miles.pineapple.util.serialization.SerializedElement;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractAdapterTest<A extends SerializedAdapter<T>, T> extends BukkitTest {

    private final A adapter;

    public AbstractAdapterTest(final A adapter) {
        this.adapter = adapter;
    }

    @Test
    public void test_ShouldDeserialize() {
        final var payload = payload();
        final T expected = payload.left();
        final SerializedElement serialized = payload.right();

        final T deserialized = assertDoesNotThrow(() -> adapter.deserialize(serialized, null));
        assertEquals(expected, deserialized);
    }

    @Test
    public void test_ShouldSerialize() {
        final var payload = payload();
        final SerializedElement expected = payload.right();
        final T serialize = payload.left();

        final SerializedElement serialized = assertDoesNotThrow(() -> adapter.serialize(serialize, null));
        assertEquals(expected, serialized);
    }

    public abstract Pair<T, SerializedElement> payload();
}
