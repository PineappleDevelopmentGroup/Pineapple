package sh.miles.pineapple.collection;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NonNullArrayTest {

    @Test
    void test_ShouldNotHaveNulls() {
        var list = new NonNullArray<>(10, () -> "default");
        for (final String s : list) {
            assertNotNull(s);
        }
    }

    @Test
    void test_ShouldGrow() {
        var list = new NonNullArray<>(1, () -> "null");
        list.add("hello");
        var newList = list.grow(1);
        newList.add("world");
        assertEquals(2, newList.size());
        assertEquals("hello", newList.get(0));
        assertEquals("world", newList.get(1));
    }

    @Test
    void test_ShouldShrink() {
        var list = new NonNullArray<>(2, () -> "null");
        list.add("hello");
        list.add("world");
        assertEquals("hello world", list.get(0) + " " + list.get(1));
        var newList = list.shrink(1);
        assertEquals(1, newList.size());
        assertEquals("hello", newList.get(0));
    }

    @Test
    void test_ShouldContains() {
        var list = new NonNullArray<>(2, () -> "null");
        assertTrue(list.contains("null"));
        list.add("fire");
        list.add("water");
        assertFalse(list.contains("null"));
        assertTrue(list.containsAll(List.of("fire", "water")));
    }

    @Test
    void test_ShouldAddAll() {
        var list = new NonNullArray<>(2, () -> "null");
        list.addAll(List.of("hello", "world"));
        assertEquals("hello", list.get(0));
        assertEquals("world", list.get(1));
    }

}
