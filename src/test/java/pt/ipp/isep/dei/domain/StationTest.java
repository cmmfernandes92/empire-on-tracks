package pt.ipp.isep.dei.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StationTest {

    private Station station;

    @BeforeEach
    void setUp() {
        station = new Station("S1", "Central", 10, 20, 3);
    }

    @Test
    void constructorInitializesAllCargoTypesWithZero() {
        List<Cargo> cargoes = station.getCargoes();
        assertEquals(CargoType.values().length, cargoes.size());
        for (Cargo c : cargoes) {
            assertEquals(0.0, c.getQuantity(), 1e-9);
        }
    }

    @Test
    void idNameLocationAndCityName() {
        assertEquals("S1", station.getId());
        assertEquals("Central", station.getStationName());
        assertEquals(10, station.getLocation().getX());
        assertEquals(20, station.getLocation().getY());
        assertNull(station.getCityName());
        station.setCityName("Metropolis");
        assertEquals("Metropolis", station.getCityName());
    }

    @Test
    void loadCargoNonPositiveOrMissingTypeReturnsZero() {
        // Request zero or negative
        assertEquals(0.0, station.loadCargo(CargoType.COAL, 0), 1e-9);
        assertEquals(0.0, station.loadCargo(CargoType.FOOD, -5), 1e-9);
        // No cargo available yet
        assertEquals(0.0, station.loadCargo(CargoType.PASSENGERS, 10), 1e-9);
    }

    @Test
    void unloadAndLoadCargoAdjustsQuantities() {
        // Unload 50 of COAL into station
        station.unloadCargo(CargoType.COAL, 50.0);
        assertEquals(50.0, station.getCargoQuantity(CargoType.COAL), 1e-9);
        // Load 30 (should take 30)
        double taken = station.loadCargo(CargoType.COAL, 30.0);
        assertEquals(30.0, taken, 1e-9);
        assertEquals(20.0, station.getCargoQuantity(CargoType.COAL), 1e-9);
    }

    @Test
    void unloadCargoWithNegativeQuantityThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> station.unloadCargo(CargoType.OIL, -1.0));
    }

    @Test
    void demandAndBaseValueAndClamping() {
        // defaults
        assertEquals(5, station.getDemandFor(CargoType.OIL));
        assertEquals(100.0, station.getBaseValueFor(CargoType.OIL), 1e-9);
        // set within bounds
        station.setDemandFor(CargoType.OIL, 8);
        assertEquals(8, station.getDemandFor(CargoType.OIL));
        // clamp above max 9
        station.setDemandFor(CargoType.OIL, 20);
        assertEquals(9, station.getDemandFor(CargoType.OIL));
        // clamp below zero
        station.setDemandFor(CargoType.OIL, -5);
        assertEquals(0, station.getDemandFor(CargoType.OIL));
        // base value change
        station.setBaseValueFor(CargoType.OIL, 123.45);
        assertEquals(123.45, station.getBaseValueFor(CargoType.OIL), 1e-9);
    }

    @Test
    void clearCargoesResetsAllToZero() {
        station.unloadCargo(CargoType.FOOD, 80.0);
        station.unloadCargo(CargoType.COAL, 40.0);
        station.clearCargoes();
        for (Cargo c : station.getCargoes()) {
            assertEquals(0.0, c.getQuantity(), 1e-9);
        }
    }

    @Test
    void deliveredThisYearTracking() {
        assertEquals(0, station.getDeliveredThisYear(CargoType.WHEAT));
        station.addDeliveredThisYear(CargoType.WHEAT, 5);
        station.addDeliveredThisYear(CargoType.WHEAT, 3);
        assertEquals(8, station.getDeliveredThisYear(CargoType.WHEAT));
        station.resetDeliveredThisYear(CargoType.WHEAT);
        assertEquals(0, station.getDeliveredThisYear(CargoType.WHEAT));
    }

    @Test
    void buildingManagementHasAndRemove() {
        assertFalse(station.hasBuilding("Cafe"));
        Building b1 = new Building("Cafe", new Date(), true);
        station.addBuilding(b1);
        assertTrue(station.hasBuilding("Cafe"));
        station.removeBuilding("Cafe");
        assertFalse(station.hasBuilding("Cafe"));
    }

    @Test
    void collectCargoOfTypeWithNoCarriagesReturnsZero() {
        station.unloadCargo(CargoType.COAL, 20.0);
        int loaded = station.collectCargoOfType(CargoType.COAL, Collections.emptyList());
        assertEquals(0, loaded);
        // station quantity remains
        assertEquals(20.0, station.getCargoQuantity(CargoType.COAL), 1e-9);
    }

    @Test
    void toStringIncludesNameLocationAndCargoCount() {
        station.unloadCargo(CargoType.OIL, 5.0);
        String s = station.toString();
        assertTrue(s.contains("Central"));
        assertTrue(s.contains("@ " + station.getLocation().toString()));
        assertTrue(s.contains("cargoes=" + station.getCargoes().size()));
    }

    @Test
    void collectAndDistributeWithNoIndustriesReturnsEmpty() {
        List<String> events = station.collectAndDistribute(Collections.emptyList());
        assertTrue(events.isEmpty());
    }
}
