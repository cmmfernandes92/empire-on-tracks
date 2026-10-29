package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SizeTest {

    @Test
    void testValidConstructorAndGetters() {
        Size size = new Size(10, 20);
        assertEquals(10, size.getWidth());
        assertEquals(20, size.getHeight());
    }

    @Test
    void testToStringFormat() {
        Size size = new Size(5, 8);
        assertEquals("5x8", size.toString());
    }

    @Test
    void testEqualsSameValues() {
        Size size1 = new Size(7, 9);
        Size size2 = new Size(7, 9);
        assertEquals(size1, size2);
    }

    @Test
    void testEqualsDifferentValues() {
        Size size1 = new Size(7, 9);
        Size size2 = new Size(8, 9);
        assertNotEquals(size1, size2);
    }

    @Test
    void testEqualsWithNullOrOtherType() {
        Size size = new Size(5, 5);
        assertNotEquals(null, size);
        assertNotEquals("5x5", size);
    }

    @Test
    void testHashCodeConsistency() {
        Size size1 = new Size(4, 6);
        Size size2 = new Size(4, 6);
        assertEquals(size1.hashCode(), size2.hashCode());
    }

    @Test
    void testConstructorThrowsForZeroOrNegative() {
        assertThrows(IllegalArgumentException.class, () -> new Size(0, 10));
        assertThrows(IllegalArgumentException.class, () -> new Size(10, 0));
        assertThrows(IllegalArgumentException.class, () -> new Size(-5, 10));
        assertThrows(IllegalArgumentException.class, () -> new Size(10, -5));
    }
}
