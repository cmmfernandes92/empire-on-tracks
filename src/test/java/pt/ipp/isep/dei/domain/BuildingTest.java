package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class BuildingTest {

    private Building building;
    private Date date;

    @BeforeEach
    void setUp() {
        date = new Date();
        building = new Building("Telegraph", date, true);
    }

    @Test
    void testGetType() {
        assertEquals("Telegraph", building.getType());
    }

    @Test
    void testGetDateAdded() {
        assertEquals(date, building.getDateAdded());
    }

    @Test
    void testIsActiveTrue() {
        assertTrue(building.isActive());
    }

    @Test
    void testSetActiveFalse() {
        building.setActive(false);
        assertFalse(building.isActive());
    }

    @Test
    void testSetActiveTrue() {
        building.setActive(false);
        building.setActive(true);
        assertTrue(building.isActive());
    }

    @Test
    void testToStringFormat() {
        String expected = "Telegraph (Added: " + date + ", Active: true)";
        assertEquals(expected, building.toString());
    }
}
