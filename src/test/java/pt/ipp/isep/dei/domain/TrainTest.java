package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TrainTest {

    private Train train;
    private Locomotive loco;
    private Carriage mail1, mail2, pax;
    private Station s1, s2;
    private Route route;

    @BeforeEach
    void setUp() {
        loco = new Locomotive("L1", "Electric", 500_000, true, 4000, 2.0, 120, 1900, 10.0, 10000);

        mail1 = new Carriage(CargoType.MAIL, 100);
        mail2 = new Carriage(CargoType.MAIL, 100);
        pax = new Carriage(CargoType.PASSENGERS, 100);

        s1 = new Station("S1", "Alpha", 0, 0, 1);
        s2 = new Station("S2", "Beta", 1, 1, 1);

        List<Carriage> carriages = new ArrayList<>(List.of(mail1, mail2, pax));
        List<String> cargoTypes = List.of("MAIL", "PASSENGERS");

        train = new Train("Express", "X123", loco, carriages, cargoTypes);

        java.util.Map<CargoType, Integer> c1 = new EnumMap<>(CargoType.class);
        c1.put(CargoType.MAIL, 2);

        java.util.Map<CargoType, Integer> c2 = new EnumMap<>(CargoType.class);
        c2.put(CargoType.PASSENGERS, 1);

        PointOfRoute p1 = new PointOfRoute(s1, c1, CargoMode.FULL);
        PointOfRoute p2 = new PointOfRoute(s2, c2, CargoMode.HALF);
        route = new Route(List.of(p1, p2));

        train.assignRoute(route);
    }

    @Test
    void testBasicGetters() {
        assertEquals("Express", train.getName());
        assertEquals("X123", train.getPlateNumber());
        assertEquals("Electric", train.getLocomotiveType());
        assertEquals(3, train.getCarriages().size());
        assertEquals(loco, train.getLocomotive());
        assertEquals(List.of("MAIL", "PASSENGERS"), train.getCurrentCargoes());
    }

    @Test
    void testHasRouteAndToString() {
        assertTrue(train.hasRoute());
        String str = train.toString();
        assertTrue(str.contains("Express"));
        assertTrue(str.contains("Electric"));
        assertTrue(str.contains("->"));
    }

    @Test
    void testAssignRouteResetsState() {
        assertEquals(0, train.getCurrentStopIndex());
        assertFalse(train.isInTransit());
        assertEquals(route, train.getAssignedRoute());
    }

    @Test
    void testStartTravelAndAdvance() {
        train.startTravelTo(s2, 100.0, true);
        assertTrue(train.isInTransit());

        double initial = train.getRemainingTravelMonths();
        assertTrue(initial > 0);

        train.advanceTravelByOneMonth(); // still traveling
        assertTrue(train.getRemainingTravelMonths() < initial);

        // force arrival
        while (!train.advanceTravelByOneMonth()) {}
        assertFalse(train.isInTransit());
        assertEquals(1, train.getCurrentStopIndex());
    }

    @Test
    void testAssignedCargoTypesForCurrentStop() {
        train.startTravelTo(s2, 100, true);
        assertEquals(List.of(), train.getAssignedCargoTypesForCurrentStop());

        while (!train.advanceTravelByOneMonth()) {}
        assertEquals(List.of(CargoType.PASSENGERS), train.getAssignedCargoTypesForCurrentStop());
    }

    @Test
    void testGetActiveCarriages() {
        train.startTravelTo(s2, 100, true);
        while (!train.advanceTravelByOneMonth()) {}
        List<Carriage> active = train.getActiveCarriagesForCurrentStop(CargoType.PASSENGERS);
        assertEquals(1, active.size());
    }

    @Test
    void testActiveCarriagesState() {
        train.startTravelTo(s2, 100, true);
        while (!train.advanceTravelByOneMonth()) {}

        assertEquals(1, train.activeCarriagesCount(CargoType.PASSENGERS));
        assertEquals(0, train.activeCarriagesLoadedCount(CargoType.PASSENGERS));
        assertFalse(train.activeCarriagesFull(CargoType.PASSENGERS));

        pax.loadCargo(100);
        assertEquals(1, train.activeCarriagesLoadedCount(CargoType.PASSENGERS));
        assertTrue(train.anyActiveCarriageLoaded(CargoType.PASSENGERS));
        assertTrue(train.activeCarriagesFull(CargoType.PASSENGERS));
    }

    @Test
    void testAvailableCapacityForCargo() {
        int available = train.getAvailableCapacityFor(CargoType.MAIL);
        assertEquals(200, available);

        train.loadCargo(CargoType.MAIL, 150);
        int after = train.getAvailableCapacityFor(CargoType.MAIL);
        assertEquals(50, after);
    }

    @Test
    void testLoadCargoReturnsAmountActuallyLoaded() {
        int loaded = train.loadCargo(CargoType.MAIL, 250);
        assertEquals(200, loaded); // total capacity = 200
    }

    @Test
    void testDistanceTracking() {
        train.addDistanceTraveled(42.0);
        assertEquals(42.0, train.consumeAndResetDistanceTraveledThisMonth(), 0.01);
        assertEquals(0.0, train.consumeAndResetDistanceTraveledThisMonth(), 0.01);
    }

    @Test
    void testCanTraverseRouteWithElectrification() {
        RailLine nonElectrified = new RailLine(s1, s2, false, false, 1);
        assertFalse(train.canTraverseRoute(route.getPointsOfRoute(), List.of(nonElectrified)));

        RailLine electrified = new RailLine(s1, s2, true, false, 1);
        assertTrue(train.canTraverseRoute(route.getPointsOfRoute(), List.of(electrified)));
    }
}
