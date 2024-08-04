package sh.miles.pineapple.exception;

import org.junit.jupiter.api.Test;
import sh.miles.pineapple.function.Option;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class AnomalyTest {

    private static final Logger logger = Logger.getGlobal();

    static {
        logger.setLevel(Level.ALL);
    }

    @Test
    public void test_Anomaly_Should_Throw() {
        assertThrows(RuntimeException.class, () -> new Anomaly<Object>(logger)
                .run(() -> methodThatErrors(true))
                .hard(getClass(), "test_Anomaly_Should_Throw")
        );
    }

    @Test
    public void test_Anomaly_Should_NotThrow_EvenThough_Exception() {
        var option = assertDoesNotThrow(() -> new Anomaly<Object>(logger)
                .run(() -> methodThatErrors(true))
                .noThrowRuntimeException()
                .hard(getClass(), "test_Anomaly_Should_NotThrow_EvenThough_Exception")
        );
        assertEquals(Option.none(), option);
    }

    @Test
    public void test_Anomaly_Should_Get_Value() {
        var option = assertDoesNotThrow(() -> new Anomaly<Object>(logger)
                .run(() -> methodThatErrors(false))
                .hard(getClass(), "test_Anomaly_Should_Get_Value")
        );
        assertEquals(5, option.orThrow());
    }

    @Test
    public void test_Anomaly_OnComplete_ShouldRun() {
        var count = new AtomicInteger(0);
        assertDoesNotThrow(() -> new Anomaly<Object>(logger)
                .run(() -> methodThatErrors(true))
                .onFail(() -> count.set(count.get() + 1))
                .hard(getClass(), "test_Anomaly_OnComplete_ShouldRun")
        );
        assertEquals(1, count.get());
    }

    @Test
    public void test_mindlessRunner() {
        new Anomaly<>(logger)
                .message("Mindless Runner Failed")
                .run(() -> methodThatErrors(true))
                .soft(getClass(), "test_mindlessRunner");
    }

    public int methodThatErrors(boolean doThrow) throws RuntimeException {
        if (doThrow) {
            throw new RuntimeException("Some Test Error Occurred!");
        }
        return 5;
    }
}
