package sh.miles.pineapple.collection.set;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Represents a set which can have a policy which determines whether it acts as a blacklist or whitelist.
 *
 * @param <E> The element
 * @since 1.0.0-SNAPSHOT
 */
public class PolicySet<E> implements Set<E> {

    private final Set<E> set;
    private final SetPolicy policy;

    /**
     * Creates a new PolicySet with the given policy.
     *
     * @param policy the set policy
     */
    public PolicySet(@NotNull final SetPolicy policy) {
        this(new HashSet<>(), policy);
    }

    /**
     * Creates a new PolicySet with the given data and policy
     *
     * @param set    the set policy
     * @param policy the policy
     */
    public PolicySet(@NotNull final Set<E> set, @NotNull final SetPolicy policy) {
        if (set == null) {
            throw new IllegalArgumentException("The provided set must not be null");
        }

        if (policy == null) {
            throw new IllegalArgumentException("The given policy must not be null");
        }

        this.set = set;
        this.policy = policy;
    }

    @Override
    public int size() {
        return this.set.size();
    }

    @Override
    public boolean isEmpty() {
        return this.set.isEmpty();
    }

    @Override
    public boolean contains(final Object o) {
        return policy.check(this.set.contains(o));
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return this.set.iterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return this.set.toArray();
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull final T[] a) {
        return this.set.toArray(a);
    }

    @Override
    public boolean add(final E e) {
        return this.set.add(e);
    }

    @Override
    public boolean remove(final Object o) {
        return this.set.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull final Collection<?> c) {
        for (final Object o : c) {
            if (!this.policy.check(this.set.contains(o))) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(@NotNull final Collection<? extends E> c) {
        return this.set.addAll(c);
    }

    @Override
    public boolean retainAll(@NotNull final Collection<?> c) {
        return this.set.retainAll(c);
    }

    @Override
    public boolean removeAll(@NotNull final Collection<?> c) {
        return this.set.removeAll(c);
    }

    @Override
    public void clear() {
        this.set.clear();
    }

    /**
     * Represents the policy a {@link PolicySet} can have
     *
     * @since 1.0.0-SNAPSHOT
     */
    public enum SetPolicy {
        /**
         * If the set should act as a whitelist
         *
         * @since 1.0.0-SNAPSHOT
         */
        WHITELIST {
            @Override
            public boolean check(final boolean check) {
                return check;
            }
        },
        /**
         * If the set should act as a blacklist
         *
         * @since 1.0.0-SNAPSHOT
         */
        BLACKLIST {
            @Override
            public boolean check(final boolean check) {
                return !check;
            }
        };

        /**
         * A implemented method, which determines the check to be done on the boolean
         *
         * @param check the check to be performance
         * @return the resulting boolean after the check
         * @since 1.0.0-SNAPSHOT
         */
        public abstract boolean check(final boolean check);

        /**
         * Determines set policy from a boolean
         *
         * @param isBlacklist is a blacklist
         * @return the policy corresponding to the boolean
         * @since 1.0.0-SNAPSHOT
         */
        public static SetPolicy fromBoolean(final boolean isBlacklist) {
            return isBlacklist ? SetPolicy.BLACKLIST : SetPolicy.WHITELIST;
        }
    }

}
