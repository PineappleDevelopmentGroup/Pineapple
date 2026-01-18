package sh.miles.pineapple;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sh.miles.pineapple.ByteUtils.Component3;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ByteUtilsTest {

    @Test
    void test_packX24Z24Y16() {
        int x = 0x00AA_BBCC;
        int z = 0x0011_2233;
        int y = 0xDD_EE;
        final long result = ByteUtils.packX24Z24Y16(x, y, z);
        final long expected = 0xAABBCC112233DDEEL;
        assertEquals(expected, result, "The bits were not packed into the correct positions. Check Z-Shift amount");
    }

    @Test
    void test_MaskingAndOverflow_packX24Z24Y16() {
        int x = 0xFFAA_AAAA;
        int z = 0xFFBB_BBBB;
        int y = 0xFFFF_CCCC;
        final long result = ByteUtils.packX24Z24Y16(x, y, z);
        final long expected = 0xAAAAAABBBBBBCCCCL;
        assertEquals(expected, result, "The bitmasks do not work correctly and bled into neighboring bits");
    }

    @Test
    @DisplayName("Unpack Zero returns all zeros")
    void testUnpackZero() {
        assertEquals(0, ByteUtils.unpackX24Z24Y16(0L).c1());
        assertEquals(0, ByteUtils.unpackX24Z24Y16(0L).c2());
        assertEquals(0, ByteUtils.unpackX24Z24Y16(0L).c3());
    }

    @Test
    @DisplayName("Unpack handles large 24-bit Z values")
    void testUnpackLargeZ() {
        // 0xFFFFFF is the maximum unsigned value for 24 bits
        // However, if we treat it as signed, it is -1.
        // Let's test a large POSITIVE 24-bit integer: 0x7FFFFF (8,388,607)
        int maxPositive24Bit = 0x7FFFFF;

        long packed = ((long) maxPositive24Bit << 16);
        Component3 result = ByteUtils.unpackX24Z24Y16(packed);

        assertEquals(maxPositive24Bit, result.c2(), "Should preserve large 24-bit Z values");
    }

    @Test
    @DisplayName("Unpack handles negative coordinates (Sign Extension)")
    void testUnpackNegatives() {
        int x = -50;
        int z = -500;
        int y = -10;

        // Manually pack to ensure input is correct
        // We mask inputs to their bit sizes to simulate clean packing
        long packed = ((long) x << 40)
                | ((long) (z & 0xFFFFFF) << 16)
                | (y & 0xFFFF);

        Component3 result = ByteUtils.unpackX24Z24Y16(packed);

        assertEquals(x, result.c1(), "X (40-bit shift) should be -50");
        assertEquals(z, result.c2(), "Z (24-bit) should be -500");
        assertEquals(y, result.c3(), "Y (16-bit) should be -10");
    }

    @Test
    @DisplayName("Round Trip Consistency")
    void testRoundTrip() {
        // If you have the pack method available, this is the best test
        int x = 12345;
        int z = 67890; // Larger than 16-bit (requires > 0xFFFF)
        int y = 200;

        long packed = ByteUtils.packX24Z24Y16(x, y, z); // Note: verify your pack args order!
        Component3 result = ByteUtils.unpackX24Z24Y16(packed);

        assertEquals(x, result.c1());
        assertEquals(z, result.c2());
        assertEquals(y, result.c3());
    }
}
