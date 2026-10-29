package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RouteTest {

    private Station station1 = new Station("S1", "Porto", 0, 0, 1);
    private Station station2 = new Station("S2", "Lisbon", 1, 1, 1);

    private Map<CargoType, Integer> cargo1 = new EnumMap<>(CargoType.class);
    private Map<CargoType, Integer> cargo2 = new EnumMap<>(CargoType.class);

    private PointOfRoute por1;
    private PointOfRoute por2;
    private Route route;

    {
        cargo1.put(CargoType.MAIL, 2);
        cargo2.put(CargoType.PASSENGERS, 3);

        por1 = new PointOfRoute(station1, cargo1, CargoMode.HALF);
        por2 = new PointOfRoute(station2, cargo2, CargoMode.FULL);

        route = new Route(List.of(por1, por2));
    }

    @Test
    void testConstructorWithValidList() {
        assertEquals(2, route.getNumberOfStops());
    }

    @Test
    void testConstructorThrowsOnNullList() {
        assertThrows(NullPointerException.class, () -> new Route(null));
    }

    @Test
    void testConstructorThrowsOnFewerThanTwoPoints() {
        assertThrows(IllegalArgumentException.class, () -> new Route(List.of(por1)));
    }

    @Test
    void testGetInitialStop() {
        assertEquals(por1, route.getInitialStop());
    }

    @Test
    void testGetFinalStop() {
        assertEquals(por2, route.getFinalStop());
    }

    @Test
    void testGetCargoes() {
        List<CargoType> cargoes = route.getCargoes();
        assertEquals(2, cargoes.size());
        assertTrue(cargoes.contains(CargoType.MAIL));
        assertTrue(cargoes.contains(CargoType.PASSENGERS));
    }

    @Test
    void testGetStations() {
        List<Station> stations = route.getStations();
        assertEquals(2, stations.size());
        assertEquals(station1, stations.get(0));
        assertEquals(station2, stations.get(1));
    }

    @Test
    void testGetPointsOfRoute() {
        List<PointOfRoute> points = route.getPointsOfRoute();
        assertEquals(2, points.size());
        assertEquals(por1, points.get(0));
        assertEquals(por2, points.get(1));
        assertThrows(UnsupportedOperationException.class, () -> points.add(por1));
    }

    @Test
    void testGetNumberOfStops() {
        assertEquals(2, route.getNumberOfStops());
    }

    @Test
    void testGetCarriageCountsPerStop() {
        List<Integer> counts = route.getCarriageCountsPerStop();
        assertEquals(List.of(1, 1), counts); // 1 cargo type per stop
    }

    @Test
    void testToStringIncludesBothPoints() {
        String result = route.toString();
        assertTrue(result.contains("Porto"));
        assertTrue(result.contains("Lisbon"));
        assertTrue(result.contains("->"));
    }
}
