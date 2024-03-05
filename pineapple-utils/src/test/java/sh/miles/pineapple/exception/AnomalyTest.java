package sh.miles.pineapple.exception;

import org.junit.jupiter.api.Test;
import sh.miles.pineapple.function.Option;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class AnomalyTest {

    private static final Logger logger = Logger.getGlobal();

    @Test
    public void test_Anomaly_Should_Throw() {
        assertThrows(RuntimeException.class, () -> new Anomaly<Object>(logger)
                .run(() -> methodThatErrors(true))
                .log(getClass(), "test_Anomaly_Should_Throw")
                .hard()
        );
    }

    @Test
    public void test_Anomaly_Should_NotThrow_EvenThough_Exception() {
        var option = assertDoesNotThrow(() -> new Anomaly<Object>(logger)
                .run(() -> methodThatErrors(true))
                .log(getClass(), "test_Anomaly_Should_NotThrow_EvenThough_Exception")
                .noThrowException()
                .hard()
        );
        assertEquals(Option.none(), option);
    }

    @Test
    public void test_Anomaly_Should_Get_Value() {
        var option = assertDoesNotThrow(() -> new Anomaly<Object>(logger)
                .run(() -> methodThatErrors(false))
                .log(getClass(), "test_Anomaly_Should_Get_Value")
                .hard()
        );
        assertEquals(5, option.orThrow());
    }

    @Test
    public void test_Anomaly_OnComplete_ShouldRun() {
        var count = new AtomicInteger(0);
        assertThrows(RuntimeException.class, () -> new Anomaly<Object>(logger)
                .run(() -> methodThatErrors(true))
                .log(getClass(), "test_Anomaly_OnComplete_ShouldRun")
                .onError(() -> count.set(count.get() + 1))
                .hard()
        );
        assertEquals(1, count.get());
    }

    @Test
    public void test_mindlessRunner() {
        new Anomaly<>(logger)
                .message("An Error Occurred from the method that errors! Surprise Surprise!")
                .log(getClass(), "test_mindlessRunner")
                .run(() -> methodThatErrors(true))
                .hard();
    }

    public int methodThatErrors(boolean doThrow) throws RuntimeException {
        if (doThrow) {
            throw new RuntimeException("Some Test Error Occurred!");
        }
        return 5;
    }
}
