package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CargoTest {

    private Cargo cargo;

    @BeforeEach
    void setUp() {
        cargo = new Cargo(CargoType.COAL, 100.0);
    }

    @Test
    void testConstructorInitializesCorrectly() {
        assertEquals(CargoType.COAL, cargo.getType());
        assertEquals(0.0, cargo.getQuantity(), 0.0001); // quantity = 0 despite parameter
    }

    @Test
    void testSetAndGetCargoType() {
        cargo.setCargoType(CargoType.OIL);
        assertEquals(CargoType.OIL, cargo.getType());
    }

    @Test
    void testSetAndGetQuantity() {
        cargo.setQuantity(150.5);
        assertEquals(150.5, cargo.getQuantity(), 0.0001);
    }

    @Test
    void testAddQuantityPositiveValue() {
        cargo.addQuantity(50.0);
        assertEquals(50.0, cargo.getQuantity(), 0.0001);
    }

    @Test
    void testAddQuantityNegativeOrZeroDoesNothing() {
        cargo.addQuantity(-10.0);
        cargo.addQuantity(0.0);
        assertEquals(0.0, cargo.getQuantity(), 0.0001);
    }

    @Test
    void testRemoveQuantitySuccess() {
        cargo.setQuantity(100.0);
        boolean result = cargo.removeQuantity(40.0);
        assertTrue(result);
        assertEquals(60.0, cargo.getQuantity(), 0.0001);
    }

    @Test
    void testRemoveQuantityFailsWhenTooMuch() {
        cargo.setQuantity(30.0);
        boolean result = cargo.removeQuantity(50.0);
        assertFalse(result);
        assertEquals(30.0, cargo.getQuantity(), 0.0001);
    }

    @Test
    void testRemoveQuantityFailsWhenNegative() {
        cargo.setQuantity(30.0);
        boolean result = cargo.removeQuantity(-5.0);
        assertFalse(result);
        assertEquals(30.0, cargo.getQuantity(), 0.0001);
    }

    @Test
    void testToStringIncludesQuantityAndType() {
        cargo.setQuantity(42.0);
        assertEquals("42.0 units of COAL", cargo.toString());
    }

    @Test
    void testGetBehaviorsInitiallyEmpty() {
        assertTrue(cargo.getBehaviors().isEmpty());
    }

    @Test
    void testSetBehaviorAddsNewBehavior() {
        cargo.setBehavior("flammable");
        assertTrue(cargo.getBehaviors().contains("flammable"));
    }

    @Test
    void testSetBehaviorDoesNotAddDuplicate() {
        cargo.setBehavior("flammable");
        cargo.setBehavior("flammable");
        assertEquals(1, cargo.getBehaviors().size());
    }
}
