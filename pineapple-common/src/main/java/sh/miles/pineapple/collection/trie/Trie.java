package sh.miles.pineapple.collection.trie;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * The classic Trie implementation which works well with mass string comparison
 *
 * @since 1.0.0-SNAPSHOT
 */
public class Trie {

    private static final char END = '$';

    private final TrieNode root;

    public Trie() {
        this.root = new TrieNode();
    }

    /**
     * Creates a new Trie
     *
     * @param input the trie inputs
     */
    public Trie(Collection<String> input) {
        this();
        for (final String s : input) {
            insert(s);
        }
    }

    /**
     * Creates a new Trie
     *
     * @param input the trie inputs
     * @since 1.0.0-SNAPSHOT
     */
    public Trie(String... input) {
        this();
        for (final String s : input) {
            insert(s);
        }
    }

    /**
     * Inserts a string into the Trie
     *
     * @param string the string
     * @since 1.0.0-SNAPSHOT
     */
    public void insert(String string) {
        string = string + END;
        TrieNode current = root;
        for (int i = 0; i < string.length(); i++) {
            char at = string.charAt(i);
            if (current.hasNoEdge(at)) {
                current = current.insertEdge(at, new TrieNode());
                continue;
            }

            current = current.getChild(at);
        }
    }

    /**
     * Checks if the given string is within the Trie
     *
     * @param string the string to check
     * @return true if the string is in the trie
     * @since 1.0.0-SNAPSHOT
     */
    public boolean contains(String string) {
        string = string + END;
        TrieNode current = root;
        for (int i = 0; i < string.length(); i++) {
            char at = string.charAt(i);
            if (current.hasNoEdge(at)) {
                return false;
            }

            current = current.getChild(at);
        }
        return current.hasNoEdges();
    }

    /**
     * Checks if the given string is within the Trie
     *
     * @param string  the string to check
     * @param partial if true the Trie will only check if the suffix of the string is inside and pays no mind to any
     *                other details.
     * @return true if the string is in the trie
     * @since 1.0.0-SNAPSHOT
     */
    public boolean contains(String string, boolean partial) {
        if (!partial) {
            return contains(string);
        }

        TrieNode current = root;
        for (int i = 0; i < string.length(); i++) {
            char at = string.charAt(i);
            if (current.hasNoEdge(at)) {
                return false;
            }

            current = current.getChild(at);
        }
        return !current.hasNoEdges();
    }

    private static class TrieNode {
        public final Map<Character, TrieNode> nodes = new HashMap<>();

        public TrieNode insertEdge(char character, @NotNull final TrieNode child) {
            nodes.put(character, child);
            return child;
        }

        public TrieNode getChild(char character) {
            return nodes.get(character);
        }

        public boolean hasNoEdge(char character) {
            return !nodes.containsKey(character);
        }

        public boolean hasNoEdges() {
            return nodes.isEmpty();
        }
    }

}
