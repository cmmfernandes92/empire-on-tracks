package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

class CityTest {

    @Test
    void validateName_invalid_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> new City("MyCity123", 0, 0, 1, "manual").validateName());
    }

    @Test
    void validateHouseBlocks_zeroOrLess_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> new City("City", 0, 0, 0, "manual").validateHouseBlocks());
    }

    @Test
    void createHouseBlocksManually_valid() {
        City c = new City("Test", 0, 0, 2, "manual");
        c.createHouseBlocksManually(Arrays.asList(1, 2), Arrays.asList(3, 4));
        assertEquals(2, c.getHouseBlocks().size());
        assertTrue(c.getHouseBlocks().stream()
                .anyMatch(h -> h.getLocation().equals(new Location(1, 3))));
    }

    @Test
    void createHouseBlocksAutomatically_positiveCoordinates() {
        City c = new City("Auto", 5, 5, 5, "automatic");
        c.createHouseBlocksAutomatically();
        assertEquals(5, c.getHouseBlocks().size());
        // Ensure none are at center or negative/zero
        assertTrue(c.getHouseBlocks().stream()
                .noneMatch(h -> {
                    Location l = h.getLocation();
                    return l.getX() <= 0 || l.getY() <= 0 || l.equals(c.getCenter());
                }));
    }
}
