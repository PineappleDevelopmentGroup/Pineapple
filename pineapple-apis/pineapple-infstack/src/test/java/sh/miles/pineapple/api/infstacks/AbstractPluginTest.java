package sh.miles.pineapple.api.infstacks;

import org.junit.jupiter.api.BeforeEach;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.plugin.PluginMock;

public abstract class AbstractPluginTest {

    protected ServerMock server;
    protected PluginMock plugin;

    @BeforeEach
    public void setup() {
        this.server = MockBukkit.mock();
        this.plugin = MockBukkit.createMockPlugin();
    }

    @BeforeEach
    public void teardown() {
        MockBukkit.unmock();
    }

}
