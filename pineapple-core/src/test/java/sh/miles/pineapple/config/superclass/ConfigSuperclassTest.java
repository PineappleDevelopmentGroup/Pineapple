package sh.miles.pineapple.config.superclass;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sh.miles.pineapple.BukkitTest;
import sh.miles.pineapple.PineappleLib;
import sh.miles.pineapple.ReflectionUtils;
import sh.miles.pineapple.config.ConfigField;
import sh.miles.pineapple.config.type.Configuration;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ConfigSuperclassTest extends BukkitTest {

    private ConfigSubclassMock instanceMock;
    private Configuration configuration;
    private File file;


    @BeforeEach
    @Override
    public void setup() {
        super.setup();
        file = new File(plugin.getDataFolder(), "config.yml");
        PineappleLib.initialize(super.plugin, false);
        this.instanceMock = new ConfigSubclassMock();
        this.configuration = assertDoesNotThrow(() -> PineappleLib.getConfigurationManager()
                .createConfiguration(file, ConfigSubclassMock.class,
                        instanceMock
                ).save(false)
                .load());
    }

    @AfterEach
    @Override
    public void teardown() {
        super.teardown();
        PineappleLib.cleanup();
    }

    @Test
    public void test_Field_Loading() {
        List<ConfigField> fields = ReflectionUtils.getField(configuration, "fields", List.class);
        Assertions.assertEquals(4, fields.size());
    }
}