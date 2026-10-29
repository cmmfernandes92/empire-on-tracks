package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LocomotiveTest {

    private final Locomotive loco = new Locomotive(
            "L001", "Electric", 500000, true,
            4000, 1.8, 160.0, 1920, 3.2, 10000
    );

    @Test
    void testConstructorAndGetters() {
        assertEquals("L001", loco.getId());
        assertEquals("Electric", loco.getType());
        assertEquals(500000, loco.getPrice());
        assertTrue(loco.requiresElectrification());
        assertEquals(4000, loco.getPower());
        assertEquals(1.8, loco.getAcceleration());
        assertEquals(160.0, loco.getTopSpeed());
        assertEquals(1920, loco.getStartYear());
        assertEquals(3.2, loco.getFuelCostPerKm());
        assertEquals(10000, loco.getMaintenancePerYear());
        assertEquals(0, loco.getCapacity());
    }

    @Test
    void testDefaultRouteIsEmpty() {
        assertNotNull(loco.getRoute());
        assertTrue(loco.getRoute().isEmpty());
        assertNull(loco.getCurrentStation());
    }

    @Test
    void testSetAndGetRoute() {
        Station s1 = new Station("ST1", "Alpha", 0, 0, 1);
        Station s2 = new Station("ST2", "Beta", 1, 1, 1);
        loco.setRoute(List.of(s1, s2));

        List<Station> route = loco.getRoute();
        assertEquals(2, route.size());
        assertEquals(s1, loco.getCurrentStation());
    }

    @Test
    void testMoveWrapsAround() {
        Station s1 = new Station("ST1", "Alpha", 0, 0, 1);
        Station s2 = new Station("ST2", "Beta", 1, 1, 1);
        loco.setRoute(List.of(s1, s2));

        assertEquals(s1, loco.getCurrentStation());
        loco.move();
        assertEquals(s2, loco.getCurrentStation());
        loco.move();
        assertEquals(s1, loco.getCurrentStation()); // Wrapped
    }

    @Test
    void testMoveOnEmptyRouteDoesNothing() {
        loco.setRoute(List.of());
        assertDoesNotThrow(() -> loco.move());
        assertNull(loco.getCurrentStation());
    }

    @Test
    void testAvailabilitySetterAndGetter() {
        Period period = new Period(1900, 1950);
        loco.setAvailability(period);
        assertEquals(period, loco.getAvailability());
    }

    @Test
    void testIsElectricTrueCaseInsensitive() {
        Locomotive l1 = new Locomotive("ID", "electric", 1, true, 1, 1, 1, 1, 1, 1);
        assertTrue(l1.isElectric());

        Locomotive l2 = new Locomotive("ID2", "Electric", 1, true, 1, 1, 1, 1, 1, 1);
        assertTrue(l2.isElectric());
    }

    @Test
    void testIsElectricFalse() {
        Locomotive diesel = new Locomotive("ID3", "Diesel", 1, false, 1, 1, 1, 1, 1, 1);
        assertFalse(diesel.isElectric());
    }

    @Test
    void testToStringContainsIdAndType() {
        String str = loco.toString();
        assertTrue(str.contains("L001"));
        assertTrue(str.contains("Electric"));
    }

    @Test
    void testNullChecksInConstructor() {
        assertThrows(NullPointerException.class, () -> new Locomotive(null, "Diesel", 1, false, 1, 1, 1, 1, 1, 1));
        assertThrows(NullPointerException.class, () -> new Locomotive("id", null, 1, false, 1, 1, 1, 1, 1, 1));
    }
}
