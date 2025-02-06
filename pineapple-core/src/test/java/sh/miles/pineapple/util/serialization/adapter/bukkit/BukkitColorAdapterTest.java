package sh.miles.pineapple.util.serialization.adapter.bukkit;

import org.bukkit.Color;
import sh.miles.pineapple.collection.Pair;
import sh.miles.pineapple.util.serialization.SerializedElement;
import sh.miles.pineapple.util.serialization.adapter.AbstractAdapterTest;

public class BukkitColorAdapterTest extends AbstractAdapterTest<BukkitColorAdapter, Color> {

    public BukkitColorAdapterTest() {
        super(new BukkitColorAdapter());
    }

    @Override
    public Pair<Color, SerializedElement> payload() {
        return Pair.of(Color.OLIVE, SerializedElement.primitive("#" + Integer.toHexString(Color.OLIVE.asRGB())));
    }
}
