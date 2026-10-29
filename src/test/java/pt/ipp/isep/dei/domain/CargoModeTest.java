package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CargoModeTest {

    @Test
    void testValues() {
        // Garante que todos os valores estão presentes
        CargoMode[] values = CargoMode.values();
        assertEquals(3, values.length);
        assertArrayEquals(new CargoMode[]{CargoMode.FULL, CargoMode.HALF, CargoMode.AVAILABLE}, values);
    }

    @Test
    void testValueOf() {
        // Garante que o valueOf funciona corretamente
        assertEquals(CargoMode.FULL, CargoMode.valueOf("FULL"));
        assertEquals(CargoMode.HALF, CargoMode.valueOf("HALF"));
        assertEquals(CargoMode.AVAILABLE, CargoMode.valueOf("AVAILABLE"));
    }
}
