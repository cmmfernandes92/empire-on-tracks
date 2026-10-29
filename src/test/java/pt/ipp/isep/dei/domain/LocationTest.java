package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LocationTest {

    @Test
    void equalsAndHashCode_sameCoordinates_shouldBeEqual() {
        Location a = new Location(5, 7);
        Location b = new Location(5, 7);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void equals_differentCoordinates_shouldNotBeEqual() {
        Location a = new Location(0, 0);
        Location b = new Location(1, 0);

        assertNotEquals(a, b);
    }

    @Test
    void toString_formatsCorrectly() {
        Location loc = new Location(3, 4);
        assertEquals("Location{x=3, y=4}", loc.toString());
    }
}
