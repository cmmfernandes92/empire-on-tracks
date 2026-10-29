package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarriageTest {

    @Test
    void constructorWithValidCargoTypeAndCapacity() {
        Carriage carriage = new Carriage(CargoType.MAIL, 150);
        assertEquals(CargoType.MAIL, carriage.getCargoType());
        assertEquals(150, carriage.getCapacity());
        assertEquals(0, carriage.getCurrentLoad());
    }

    @Test
    void constructorWithDefaultCapacity() {
        Carriage carriage = new Carriage(CargoType.PASSENGERS);
        assertEquals(CargoType.PASSENGERS, carriage.getCargoType());
        assertEquals(100, carriage.getCapacity());
        assertEquals(0, carriage.getCurrentLoad());
    }

    @Test
    void constructorThrowsIfCargoTypeIsNull() {
        assertThrows(NullPointerException.class, () -> new Carriage(null, 100));
        assertThrows(NullPointerException.class, () -> new Carriage(null));
    }

    @Test
    void constructorThrowsIfCapacityIsZeroOrNegative() {
        assertThrows(IllegalArgumentException.class, () -> new Carriage(CargoType.MAIL, 0));
        assertThrows(IllegalArgumentException.class, () -> new Carriage(CargoType.MAIL, -10));
    }

    @Test
    void getAvailableCapacityReturnsCorrectValue() {
        Carriage carriage = new Carriage(CargoType.MAIL, 100);
        carriage.loadCargo(30);
        assertEquals(70, carriage.getAvailableCapacity());
    }

    @Test
    void isFullAndIsHalfFullBehaviors() {
        Carriage carriage = new Carriage(CargoType.PASSENGERS, 100);
        assertFalse(carriage.isHalfFull());
        assertFalse(carriage.isFull());

        carriage.loadCargo(50);
        assertTrue(carriage.isHalfFull());
        assertFalse(carriage.isFull());

        carriage.loadCargo(50);
        assertTrue(carriage.isFull());
    }

    @Test
    void loadCargoLoadsCorrectAmount() {
        Carriage carriage = new Carriage(CargoType.PASSENGERS, 100);
        int loaded = carriage.loadCargo(60);
        assertEquals(60, loaded);
        assertEquals(60, carriage.getCurrentLoad());
    }

    @Test
    void loadCargoTruncatesIfTooMuch() {
        Carriage carriage = new Carriage(CargoType.MAIL, 100);
        carriage.loadCargo(90);
        int loaded = carriage.loadCargo(20);
        assertEquals(10, loaded); // Only 10 left
        assertEquals(100, carriage.getCurrentLoad());
    }

    @Test
    void loadCargoThrowsIfNegative() {
        Carriage carriage = new Carriage(CargoType.MAIL, 100);
        assertThrows(IllegalArgumentException.class, () -> carriage.loadCargo(-1));
    }

    @Test
    void unloadCargoEmptiesAndReturnsCorrectAmount() {
        Carriage carriage = new Carriage(CargoType.MAIL, 100);
        carriage.loadCargo(80);
        int unloaded = carriage.unloadCargo();
        assertEquals(80, unloaded);
        assertEquals(0, carriage.getCurrentLoad());
    }

    @Test
    void unloadAllWorks() {
        Carriage carriage = new Carriage(CargoType.PASSENGERS, 100);
        carriage.loadCargo(75);
        carriage.unloadAll();
        assertEquals(0, carriage.getCurrentLoad());
    }

    @Test
    void toStringReturnsExpectedFormat() {
        Carriage carriage = new Carriage(CargoType.PASSENGERS, 200);
        carriage.loadCargo(100);
        String result = carriage.toString();
        assertTrue(result.contains("PASSENGERS"));
        assertTrue(result.contains("100/200"));
    }
}
