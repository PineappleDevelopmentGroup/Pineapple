package sh.miles.crown.infstacks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InfStackConstantsTest {

    @Test
    public void test_ValidNamespacedKey() {
        assertDoesNotThrow(() -> InfStackUtils.STACK_SIZE_KEY);
        assertNotNull(InfStackUtils.STACK_SIZE_KEY);
    }

}
