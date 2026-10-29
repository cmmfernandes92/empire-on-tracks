package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HouseBlockTest {

    @Test
    void constructorAndGetLocation() {
        HouseBlock hb = new HouseBlock(2, 8);
        Location loc = hb.getLocation();

        assertNotNull(loc);
        assertEquals(2, loc.getX());
        assertEquals(8, loc.getY());
    }
}
