package sh.miles.crown.infstacks;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.MockPlugin;
import be.seeseemelk.mockbukkit.ServerMock;
import org.junit.jupiter.api.BeforeEach;

public abstract class AbstractPluginTest {

    protected ServerMock server;
    protected MockPlugin plugin;

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
