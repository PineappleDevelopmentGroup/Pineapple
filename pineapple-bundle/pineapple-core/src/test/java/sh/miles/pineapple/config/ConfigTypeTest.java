package sh.miles.pineapple.config;

import org.junit.jupiter.api.Test;
import sh.miles.pineapple.ReflectionUtils;
import sh.miles.pineapple.collection.Pair;

import java.lang.invoke.MethodHandle;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigTypeTest {

    private static final MethodHandle splitTypeName = ReflectionUtils.getMethod(ConfigType.class, "splitTypeName", new Class[]{String.class, int.class, int.class});

    final List<Pair<String, Integer>> pair = List.of(Pair.of("24", 1));

    @Test
    void test_Should_SplitByName() throws NoSuchFieldException {
        final String typeName = getClass().getDeclaredField("pair").getGenericType().getTypeName();
        System.out.println(typeName);
        int ind = typeName.indexOf('<');
        List<ConfigType<?>> types = (List<ConfigType<?>>) assertDoesNotThrow(() -> splitTypeName.invoke(typeName, ind + 1, typeName.length() - 1));
        System.out.println(types);
    }
}
