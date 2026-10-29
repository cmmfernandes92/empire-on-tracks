package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MapTest {

    private Map defaultMap;
    private Map smallMap;

    @BeforeEach
    void setUp() {
        defaultMap = new Map(); // 10x10 named "name"
        smallMap = new Map("test", 3, 5);
    }

    @Test
    void testDefaultConstructor() {
        assertEquals("name", defaultMap.getName());
        assertEquals(10, defaultMap.getWidth());
        assertEquals(10, defaultMap.getHeight());
    }

    @Test
    void testParameterizedConstructor() {
        assertEquals("test", smallMap.getName());
        assertEquals(3, smallMap.getWidth());
        assertEquals(5, smallMap.getHeight());
    }

    @Test
    void testConstructorInvalidDimensions() {
        assertThrows(IllegalArgumentException.class, () -> new Map("x", 0, 5));
        assertThrows(IllegalArgumentException.class, () -> new Map("x", 5, -1));
    }

    @Test
    void testIsPositive() {
        assertTrue(Map.isPositive(1));
        assertFalse(Map.isPositive(0));
        assertFalse(Map.isPositive(-3));
    }

    @Test
    void testEmptyByDefault() {
        assertTrue(defaultMap.isEmpty(0, 0));
        assertTrue(defaultMap.isEmpty(9, 9));
        assertFalse(defaultMap.isEmpty(-1, 0));
        assertFalse(defaultMap.isEmpty(0, 10));
    }

    @Test
    void testCreateIndustryAndIsEmpty() {
        Location loc = new Location(2, 3);
        PrimaryIndustry industry = new PrimaryIndustry("Ind", 100, 10, 5, loc, CargoType.WHEAT);
        assertTrue(defaultMap.createIndustry(industry, loc));
        assertFalse(defaultMap.isEmpty(2, 3));
        assertEquals(loc, industry.getLocation());
        // second placement fails
        assertFalse(defaultMap.createIndustry(industry, loc));
    }

    @Test
    void testValidateCoordinates() {
        List<Location> valid = List.of(new Location(0, 0), new Location(2, 2));
        List<Location> invalid = List.of(new Location(-1, 0), new Location(0, 10));
        assertTrue(smallMap.validateCoordinates(valid));
        assertFalse(smallMap.validateCoordinates(invalid));
    }

    @Test
    void testGetWidthHeight() {
        assertEquals(3, smallMap.getWidth());
        assertEquals(5, smallMap.getHeight());
    }

    @Test
    void testGetIndustriesEmpty() {
        assertTrue(defaultMap.getIndustries().isEmpty());
    }

    @Test
    void testGetRailLinesInitiallyEmpty() {
        assertTrue(defaultMap.getRailLines().isEmpty());
    }


    @Test
    void testNameAccessors() {
        defaultMap.setName("NewName");
        assertEquals("NewName", defaultMap.getName());
    }
}