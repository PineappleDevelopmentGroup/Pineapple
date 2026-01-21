package sh.miles.pineapple;

import org.jspecify.annotations.NullMarked;

/**
 * miscellaneous byte utilities for general use
 *
 * @since 1.0.0-SNAPSHOT
 */
@NullMarked
public final class ByteUtils {

    private ByteUtils() {
    }

    /**
     * Packs an x,z in the format x 16 bits and z 16 bits
     *
     * @param x x
     * @param z z
     * @return the packed value
     * @since 1.0.0-SNAPSHOT
     */
    public static int packX16Z16(int x, int z) {
        return ((x & 0xFFFF) << 16) | (z & 0xFFFF);
    }

    /**
     * Packs an x,y and in the format x 24 bits, z 24 bits then y 16 bits
     *
     * @param x x
     * @param y y
     * @param z z
     * @return the packed long
     * @since 1.0.0-SNAPSHOT
     */
    public static long packX24Z24Y16(int x, int y, int z) {
        long out = 0L;
        out |= ((long) x & 0xFFFFFFL) << 40; // Bitmask
        out |= ((long) z & 0xFFFFFFL) << 16;
        out |= ((long) y & 0xFFFFL);
        return out;
    }

    /**
     * Unpacks the long data packed into the {@link #packX24Z24Y16(int, int, int)} format into a Component3 of format x,
     * z, y
     *
     * @param data the packed long
     * @return the component 3 object
     * @since 1.0.0-SNAPSHOT
     */
    public static Component3 unpackX24Z24Y16(long data) {
        int x, y, z = 0;
        x = (int) (data >> 40);
        z = (int) ((data >> 16) & 0xFFFFFFL);
        y = (int) (data & 0xFFFFL);

        if ((z & 0x800000) != 0) {
            z |= 0xFF000000;
        }

        if ((y & 0x8000) != 0) {
            y |= 0xFFFF0000;
        }

        return new Component3(x, z, y);
    }

    public record Component3(int c1, int c2, int c3) {
        public static Component3 PSEUDO_EMPTY = new Component3(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    public record Component2(int c1, int c2) {
        public static Component2 PSEUDO_EMPTY = new Component2(Integer.MIN_VALUE, Integer.MIN_VALUE);
    }
}
