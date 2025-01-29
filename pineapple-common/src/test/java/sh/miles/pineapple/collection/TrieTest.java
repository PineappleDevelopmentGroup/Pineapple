package sh.miles.pineapple.collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sh.miles.pineapple.collection.trie.Trie;

import static org.junit.jupiter.api.Assertions.*;

class TrieTest {

    private Trie trie;

    @BeforeEach
    public void setup() {
        trie = new Trie("one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten$");
    }

    @Test
    void test_Trie_Should_Contains_Call_Correctly() {
        assertFalse(trie.contains("eight$"));
        assertTrue(trie.contains("eight"));
        assertTrue(trie.contains("one"));
        assertFalse(trie.contains("twos"));
        assertFalse(trie.contains("ten"));
        assertTrue(trie.contains("ten$"));
    }

    @Test
    void test_Trie_Should_Contains_Partially_Call_Correctly() {
        assertFalse(trie.contains("z", true));
        assertTrue(trie.contains("eigh", true));
        assertTrue(trie.contains("eight", true));
        assertFalse(trie.contains("eight$", true));
    }
}
