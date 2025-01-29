package sh.miles.pineapple.util.serialization.adapter.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.ReflectionUtils;
import sh.miles.pineapple.util.serialization.SerializedDeserializeContext;
import sh.miles.pineapple.util.serialization.SerializedElement;
import sh.miles.pineapple.util.serialization.SerializedSerializeContext;
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapter;
import sh.miles.pineapple.util.serialization.exception.SerializedAdaptationException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

class BukkitRegistryAdapter<R extends Keyed> implements SerializedAdapter<R> {

    private final Class<R> registryClass;
    private final Registry<R> registry;

    public BukkitRegistryAdapter(Class<R> registryClass) {
        this.registryClass = registryClass;
        this.registry = Bukkit.getRegistry(registryClass);
    }

    @NotNull
    @Override
    public SerializedElement serialize(@NotNull final R obj, @NotNull final SerializedSerializeContext context) throws SerializedAdaptationException {
        return SerializedElement.primitive(obj.getKey().toString());
    }

    @NotNull
    @Override
    public R deserialize(@NotNull final SerializedElement element, @NotNull final SerializedDeserializeContext context) throws SerializedAdaptationException {
        if (element.isPrimitive()) {
            final NamespacedKey key = context.deserialize(element, NamespacedKey.class);
            final R result = this.registry.get(key);
            if (result == null) {
                throw new SerializedAdaptationException("The registry for the type %s does not have an entry with the name %s".formatted(this.registryClass.getSimpleName(), key));
            }
            return result;
        }
        throw new SerializedAdaptationException("%s's must be adapted from primitive strings".formatted(this.registryClass.getSimpleName()));
    }

    @Override
    public Class<?> getKey() {
        return this.registryClass;
    }

    static List<BukkitRegistryAdapter<?>> getRegistryAdapters() {
        final List<BukkitRegistryAdapter<?>> list = new ArrayList<>();

        try {
            for (final Field field : Registry.class.getDeclaredFields()) {
                list.add(new BukkitRegistryAdapter(ReflectionUtils.getParameterizedTypes(field).get(0)));
            }
        } catch (Exception ignored) { // while not usually advised we need to catch an exception here for Unit Test
            ignored.printStackTrace();
        }

        return list;
    }
}
