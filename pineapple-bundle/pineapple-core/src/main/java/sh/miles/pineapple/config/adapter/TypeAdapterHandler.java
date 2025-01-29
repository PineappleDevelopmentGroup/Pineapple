package sh.miles.pineapple.config.adapter;

import org.jetbrains.annotations.ApiStatus;
import sh.miles.pineapple.collection.Pair;
import sh.miles.pineapple.collection.WeightedRandom;
import sh.miles.pineapple.collection.registry.WriteableRegistry;
import sh.miles.pineapple.config.ConfigType;
import sh.miles.pineapple.config.adapter.base.ConfigSerializable;
import sh.miles.pineapple.config.adapter.base.TypeAdapter;

import java.util.Collection;
import java.util.Map;

@ApiStatus.Internal
public class TypeAdapterHandler extends WriteableRegistry<TypeAdapter<?, ?>, ConfigType<?>> {

    /**
     * Create a new TypeAdapterHandler
     */
    public TypeAdapterHandler() {
        register(new BooleanAdapter<>(boolean.class));
        register(new BooleanAdapter<>(Boolean.class));

        register(new DoubleAdapter<>(double.class));
        register(new DoubleAdapter<>(Double.class));

        register(new FloatAdapter<>(float.class));
        register(new FloatAdapter<>(Float.class));

        register(new IntegerAdapter<>(int.class));
        register(new IntegerAdapter<>(Integer.class));

        register(new LongAdapter<>(long.class));
        register(new LongAdapter<>(Long.class));

        register(new StringAdapter());
    }

    /**
     * Create a generic based TypeAdapter from ConfigType
     *
     * @param type the type
     * @return the TypeAdapter
     */
    public TypeAdapter<?, ?> create(ConfigType<?> type) {
        if (Map.class.isAssignableFrom(type.getType())) {
            return new MapAdapter<>(type);
        }

        if (ConfigSerializable.class.isAssignableFrom(type.getType())) {
            return new ConfigSerializableAdapter<>(type);
        }

        if (WeightedRandom.class.isAssignableFrom(type.getType())) {
            return new WeightedRandomAdapter<>(type);
        }

        if (Enum.class.isAssignableFrom(type.getType())) {
            return new EnumAdapter<>(type.getType());
        }

        if (Collection.class.isAssignableFrom(type.getType())) {
            return new CollectionAdapter<>(type);
        }

        if (Pair.class.isAssignableFrom(type.getType())) {
            return new PairAdapter<>(type);
        }

        throw new IllegalArgumentException("The given type of " + type.getType() + " could not be registered as it is not supported!");
    }
}
