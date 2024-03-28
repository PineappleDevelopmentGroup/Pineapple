package sh.miles.pineapple.util.serialize.adapter;

import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.util.serialize.base.GenericSerializationContext;
import sh.miles.pineapple.util.serialize.base.GenericSerializer;
import sh.miles.pineapple.util.serialize.exception.GenericSerializerNotFoundException;

import java.util.HashMap;
import java.util.Map;

/**
 * Manager for all GenericAdapters and platforms
 *
 * @since 1.0.0-SNAPSHOT
 */
public class GenericAdapters implements GenericSerializationContext {

    public static final GenericAdapters INSTANCE = new GenericAdapters();

    // Map<ComplexTypes, Serializers>
    private final Map<TypeToken<?>, GenericSerializer<?, ?>> serializers;

    private GenericAdapters() {
        this.serializers = new HashMap<>();
    }

    /**
     * Registers the GenericSerializers
     *
     * @param serializers the serializers to register
     * @since 1.0.0-SNAPSHOT
     */
    public void register(GenericSerializer<?, ?>... serializers) {
        for (final GenericSerializer<?, ?> serializer : serializers) {
            this.serializers.put(serializer.getComplexType(), serializer);
        }
    }

    @NotNull
    @Override
    public <S, C> S serialize(@NotNull final C complex, @NotNull Class<C> complexType, final @NotNull Class<S> storedType) throws GenericSerializerNotFoundException {
        if (!serializers.containsKey(complexType)) {
            throw new GenericSerializerNotFoundException(complexType);
        }
        final GenericSerializer<S, C> serializer = (GenericSerializer<S, C>) this.serializers.get(complexType);
        return serializer.serialize(complex, this);
    }

    @NotNull
    @Override
    public <S, C> C deserialize(@NotNull final S stored, final @NotNull Class<S> storedType, final @NotNull Class<C> complexType) throws GenericSerializerNotFoundException {
        if (!serializers.containsKey(complexType)) {
            throw new GenericSerializerNotFoundException(complexType);
        }
        final GenericSerializer<S, C> serializer = (GenericSerializer<S, C>) this.serializers.get(complexType);
        return serializer.deserialize(stored, this);
    }
}
