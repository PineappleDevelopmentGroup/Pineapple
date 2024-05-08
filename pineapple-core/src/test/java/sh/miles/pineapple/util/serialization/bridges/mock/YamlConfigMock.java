package sh.miles.pineapple.util.serialization.bridges.mock;

import sh.miles.pineapple.config.annotation.ConfigPath;
import sh.miles.pineapple.util.serialization.adapter.mock.ComplexObjectMock;

public class YamlConfigMock {

    @ConfigPath("test")
    public static ComplexObjectMock objectMock = ComplexObjectMock.basic();

}
