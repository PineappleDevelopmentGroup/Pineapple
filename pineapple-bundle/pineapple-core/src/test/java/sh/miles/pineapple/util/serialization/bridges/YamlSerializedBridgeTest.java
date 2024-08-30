package sh.miles.pineapple.util.serialization.bridges;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sh.miles.pineapple.BukkitTest;
import sh.miles.pineapple.PineappleLib;
import sh.miles.pineapple.ReflectionUtils;
import sh.miles.pineapple.collection.registry.AbstractRegistry;
import sh.miles.pineapple.config.ConfigManager;
import sh.miles.pineapple.config.ConfigType;
import sh.miles.pineapple.config.ConfigWrapper;
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapterRegistry;
import sh.miles.pineapple.util.serialization.adapter.mock.ComplexObjectMock;
import sh.miles.pineapple.util.serialization.adapter.mock.ComplexObjectMockAdapter;
import sh.miles.pineapple.util.serialization.adapter.mock.MalformedComplexObjectMockAdapter;
import sh.miles.pineapple.util.serialization.bridges.mock.YamlConfigMock;
import sh.miles.pineapple.util.serialization.bridges.yaml.YamlSerializedBridge;

import java.io.File;
import java.lang.invoke.MethodHandle;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class YamlSerializedBridgeTest extends BukkitTest {

    private static final MethodHandle SERIALIZED_ADAPTER_BRIDGES = ReflectionUtils.getFieldAsSetter(SerializedAdapterRegistry.class, "bridges").bindTo(SerializedAdapterRegistry.INSTANCE);
    private static final MethodHandle REGISTRY_SET_ACCESS = ReflectionUtils.getFieldAsSetter(AbstractRegistry.class, "registry").bindTo(SerializedAdapterRegistry.INSTANCE);

    private ConfigManager configManager;


    @BeforeEach
    public void setup() {
        super.setup();
        PineappleLib.initialize(super.plugin, false);
        this.configManager = PineappleLib.getConfigurationManager();
        SerializedAdapterRegistry.INSTANCE.registerBridge(new YamlSerializedBridge(configManager));
    }

    @Test
    void test_ShouldRegisterAdapter() {
        SerializedAdapterRegistry.INSTANCE.register(new ComplexObjectMockAdapter());
        assertNotNull(configManager.getTypeAdapter(ConfigType.create(ComplexObjectMock.class)));
    }

    @Test
    void test_ShouldNotHaveAdapter() {
        assertThrows(IllegalArgumentException.class, () -> configManager.getTypeAdapter(ConfigType.create(ComplexObjectMock.class)));
    }

    @Test
    void test_ShouldDeserializeSerializeCorrectly() {
        test_ShouldRegisterAdapter();
        ConfigWrapper wrapper = assertDoesNotThrow(() -> PineappleLib.getConfigurationManager().createDefault(new File(plugin.getDataFolder(), "config.yml"), YamlConfigMock.class));
        wrapper = wrapper.save(false);
        wrapper.load();
        assertEquals(ComplexObjectMock.basic(), YamlConfigMock.objectMock);
    }

    @Test
    void test_ShouldFailWithMalformed() {
        SerializedAdapterRegistry.INSTANCE.register(new MalformedComplexObjectMockAdapter());
        assertNotNull(configManager.getTypeAdapter(ConfigType.create(ComplexObjectMock.class)));
        ConfigWrapper wrapper = assertDoesNotThrow(() -> PineappleLib.getConfigurationManager().createDefault(new File(plugin.getDataFolder(), "config.yml"), YamlConfigMock.class));
        wrapper = wrapper.save(false);
        wrapper.load();
        assertNotEquals(ComplexObjectMock.basic(), YamlConfigMock.objectMock);
    }

    @AfterEach
    public void teardown() {
        super.teardown();
        configManager = null;
        try {
            SERIALIZED_ADAPTER_BRIDGES.invoke(new HashSet<>());
            REGISTRY_SET_ACCESS.invoke(new HashMap<>());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}
