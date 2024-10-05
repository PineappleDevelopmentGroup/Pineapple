package sh.miles.pineapple.util.serialization.adapter.javalang;

import sh.miles.pineapple.collection.Pair;
import sh.miles.pineapple.util.serialization.SerializedElement;
import sh.miles.pineapple.util.serialization.adapter.AbstractAdapterTest;

import java.util.UUID;

public class UUIDAdapterTest extends AbstractAdapterTest<UUIDAdapter, UUID> {

    private static final UUIDAdapter adapter = new UUIDAdapter();

    public UUIDAdapterTest() {
        super(new UUIDAdapter());
    }

    @Override
    public Pair<UUID, SerializedElement> payload() {
        final UUID random = UUID.randomUUID();
        return Pair.of(random, SerializedElement.primitive(random.toString()));
    }
}
