package sh.miles.pineapple.util.serialization.bridges;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapterFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sh.miles.pineapple.ReflectionUtils;
import sh.miles.pineapple.collection.registry.AbstractRegistry;
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapterRegistry;
import sh.miles.pineapple.util.serialization.adapter.mock.ComplexObjectMock;
import sh.miles.pineapple.util.serialization.adapter.mock.ComplexObjectMockAdapter;
import sh.miles.pineapple.util.serialization.adapter.mock.MalformedComplexObjectMockAdapter;
import sh.miles.pineapple.util.serialization.bridges.gson.GsonSerializedBridge;

import java.lang.invoke.MethodHandle;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GsonSerializedBridgeTest {

    private static final MethodHandle GSON_BUILDER_FACTORIES_FIELD = ReflectionUtils.getFieldAsGetter(GsonBuilder.class, "factories");
    private static final MethodHandle SERIALIZED_ADAPTER_BRIDGES = ReflectionUtils.getFieldAsSetter(SerializedAdapterRegistry.class, "bridges").bindTo(SerializedAdapterRegistry.INSTANCE);
    private static final MethodHandle REGISTRY_SET_ACCESS = ReflectionUtils.getFieldAsSetter(AbstractRegistry.class, "registry").bindTo(SerializedAdapterRegistry.INSTANCE);

    private GsonBuilder builder;

    @BeforeEach
    void setup() {
        builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        final GsonSerializedBridge bridge = new GsonSerializedBridge(builder);
        SerializedAdapterRegistry.INSTANCE.registerBridge(bridge);
    }

    @Test
    void test_GsonRegisteredAdapter() throws Throwable {
        SerializedAdapterRegistry.INSTANCE.register(new ComplexObjectMockAdapter());
        final List<TypeAdapterFactory> adapterFactories = (List<TypeAdapterFactory>) GSON_BUILDER_FACTORIES_FIELD.bindTo(builder).invoke();
        assertEquals(SerializedAdapterRegistry.INSTANCE.keys().size(), adapterFactories.size());
    }

    @Test
    void test_GsonNonRegisteredAdapter() throws Throwable {
        final List<TypeAdapterFactory> adapterFactories = (List<TypeAdapterFactory>) GSON_BUILDER_FACTORIES_FIELD.bindTo(builder).invoke();
        assertEquals(SerializedAdapterRegistry.INSTANCE.keys().size(), adapterFactories.size());
    }

    @Test
    void test_Should_Deserialize_Correctly() throws Throwable {
        test_GsonRegisteredAdapter();
        final Gson gson = builder.create();

        final ComplexObjectMock mock = ComplexObjectMock.basic();
        final JsonObject actual = gson.toJsonTree(mock).getAsJsonObject();

        final JsonObject expected = new JsonObject();
        expected.addProperty("name", "Joe");
        expected.addProperty("age", 53);
        expected.addProperty("money", 69420.15);
        final JsonArray array = new JsonArray();
        array.add("East");
        array.add("West");
        expected.add("properties", array);
        assertEquals(expected, actual);
    }

    @Test
    void test_Should_FailWithMalformedAdapter() throws Throwable {
        SerializedAdapterRegistry.INSTANCE.register(new MalformedComplexObjectMockAdapter());
        final List<TypeAdapterFactory> adapterFactories = (List<TypeAdapterFactory>) GSON_BUILDER_FACTORIES_FIELD.bindTo(builder).invoke();
        assertEquals(SerializedAdapterRegistry.INSTANCE.keys().size(), adapterFactories.size());

        final Gson gson = builder.create();
        final var unexpected = ComplexObjectMock.basic();
        final JsonElement tree = gson.toJsonTree(unexpected, ComplexObjectMock.class);
        final var actual = gson.fromJson(tree, ComplexObjectMock.class);

        assertNotEquals(unexpected, actual);
    }

    @AfterEach
    void teardown() throws Throwable {
        builder = null;
        SERIALIZED_ADAPTER_BRIDGES.invoke(new HashSet<>());
        REGISTRY_SET_ACCESS.invoke(new HashMap<>());
    }

}
