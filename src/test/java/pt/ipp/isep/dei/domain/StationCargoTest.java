package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StationCargoTest {

    private Station station;
    private StationCargo stationCargo;

    @BeforeEach
    void setUp() {
        station = new Station("S01", "Central", 5, 10, 2);
        stationCargo = new StationCargo(station, CargoType.PASSENGERS);
    }

    @Test
    void testGetStation() {
        assertEquals(station, stationCargo.getStation());
    }

    @Test
    void testGetCargoType() {
        assertEquals(CargoType.PASSENGERS, stationCargo.getCargoType());
    }

    @Test
    void testToString() {
        String expected = "Central (PASSENGERS)";
        assertEquals(expected, stationCargo.toString());
    }

    @Test
    void testConstructorWithNullStation() {
        assertThrows(NullPointerException.class, () -> new StationCargo(null, CargoType.MAIL));
    }

    @Test
    void testConstructorWithNullCargoType() {
        assertThrows(NullPointerException.class, () -> new StationCargo(station, null));
    }
}
