package sh.miles.pineapple.exception;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

public class PrettyExceptionTest {

    @Test
    public void test_Should_Throw_Pretty_Error() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        assertThrows(RuntimeException.class, () ->
                new PrettyException("A Test Exception", new IllegalStateException("This test is illegal!"), () -> atomicBoolean.set(true))
                        .throwing()
        );
        assertTrue(atomicBoolean.get());
    }

}
