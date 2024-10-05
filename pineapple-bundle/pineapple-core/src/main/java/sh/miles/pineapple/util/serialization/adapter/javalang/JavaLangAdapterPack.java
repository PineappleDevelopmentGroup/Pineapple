package sh.miles.pineapple.util.serialization.adapter.javalang;

import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapter;
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapterPack;

import java.util.function.Consumer;

/**
 * Pack of all java language adapters
 *
 * @since 1.0.0-SNAPSHOT
 */
public final class JavaLangAdapterPack implements SerializedAdapterPack {

    public static final JavaLangAdapterPack INSTANCE = new JavaLangAdapterPack();

    private JavaLangAdapterPack() {
    }

    @Override
    public void bootstrap(@NotNull final Consumer<SerializedAdapter<?>> registrar) {
        registrar.accept(new UUIDAdapter());
    }
}
