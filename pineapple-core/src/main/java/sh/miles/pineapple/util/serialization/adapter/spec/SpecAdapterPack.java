package sh.miles.pineapple.util.serialization.adapter.spec;

import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapter;
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapterPack;

import java.util.function.Consumer;

/**
 * Pack of all Spec Adapters
 *
 * @since 1.0.0-SNAPSHOT
 */
public final class SpecAdapterPack implements SerializedAdapterPack {

    public static final SpecAdapterPack INSTANCE = new SpecAdapterPack();

    private SpecAdapterPack() {
    }


    @Override
    public void bootstrap(@NotNull final Consumer<SerializedAdapter<?>> registrar) {
        registrar.accept(new FireworkSpecAdapter());
        registrar.accept(new HologramSpecAdapter());
        registrar.accept(new ItemSpecAdapter());
        registrar.accept(new SoundSpecAdapter());
        registrar.accept(new VectorSpecAdapter());
    }
}
