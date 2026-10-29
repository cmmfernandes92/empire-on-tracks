package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PointOfRouteTest {

    Station station = new Station("ST1", "Station A", 0, 0, 1);
    Map<CargoType, Integer> cargoMap = new EnumMap<>(CargoType.class);

    {
        cargoMap.put(CargoType.MAIL, 2);
        cargoMap.put(CargoType.PASSENGERS, 3);
    }

    PointOfRoute point = new PointOfRoute(station, cargoMap, CargoMode.HALF);

    @Test
    void testConstructorAndGetters() {
        assertEquals(station, point.getStation());
        assertEquals(List.of(CargoType.MAIL, CargoType.PASSENGERS), point.getCargoes());
        assertEquals(2, point.getCargoCarriages().size());
        assertEquals(2, point.getCargoCarriages().get(CargoType.MAIL));
        assertEquals(3, point.getCargoCarriages().get(CargoType.PASSENGERS));
        assertEquals(CargoMode.HALF, point.getCargoMode());
    }

    @Test
    void testConstructorThrowsOnNulls() {
        assertThrows(NullPointerException.class, () -> new PointOfRoute(null, cargoMap, CargoMode.FULL));
        assertThrows(NullPointerException.class, () -> new PointOfRoute(station, null, CargoMode.FULL));
        assertThrows(NullPointerException.class, () -> new PointOfRoute(station, cargoMap, null));
    }

    @Test
    void testConstructorThrowsOnEmptyCargoMap() {
        assertThrows(IllegalArgumentException.class,
                () -> new PointOfRoute(station, Map.of(), CargoMode.AVAILABLE));
    }

    @Test
    void testToStringIncludesCargoModeAndStationName() {
        String output = point.toString();
        assertTrue(output.contains("Station A"));
        assertTrue(output.contains("MAIL: 2"));
        assertTrue(output.contains("PASSENGERS: 3"));
        assertTrue(output.contains("HALF"));
    }

    @Test
    void testEqualsAndHashCode() {
        Map<CargoType, Integer> cargoMap2 = new EnumMap<>(CargoType.class);
        cargoMap2.put(CargoType.MAIL, 2);
        cargoMap2.put(CargoType.PASSENGERS, 3);
        PointOfRoute same = new PointOfRoute(new Station("ST1", "Station A", 0, 0, 1), cargoMap2, CargoMode.HALF);
        assertEquals(point, same);
        assertEquals(point.hashCode(), same.hashCode());

        PointOfRoute differentMode = new PointOfRoute(station, cargoMap2, CargoMode.FULL);
        assertNotEquals(point, differentMode);
    }

    @Test
    void testEqualsWithSameObjectAndNull() {
        assertEquals(point, point);
        assertNotEquals(point, null);
        assertNotEquals(point, "not-a-point");
    }
}
