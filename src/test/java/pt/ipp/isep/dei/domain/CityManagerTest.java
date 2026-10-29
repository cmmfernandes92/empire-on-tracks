package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

class CityManagerTest {

    @Test
    void getClosestCity_emptyList_returnsNull() {
        CityManager cm = new CityManager(Arrays.asList());
        assertNull(cm.getClosestCity(0, 0));
        assertNull(cm.getClosestCityName(0, 0));
    }

    @Test
    void getClosestCity_multipleCities_returnsClosest() {
        City c1 = new City("A", 0, 0, 1, "manual");
        City c2 = new City("B", 10, 10, 1, "manual");
        CityManager cm = new CityManager(Arrays.asList(c1, c2));
        assertEquals(c1, cm.getClosestCity(2, 1));
        assertEquals("B", cm.getClosestCityName(12, 12));
    }
}
