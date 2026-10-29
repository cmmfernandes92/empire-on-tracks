package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CargoTypeTest {

    @Test
    void testValuesIncludeAllCargoTypes() {
        CargoType[] values = CargoType.values();
        assertEquals(10, values.length);
        assertArrayEquals(
                new CargoType[]{
                        CargoType.COAL,
                        CargoType.STEEL,
                        CargoType.FOOD,
                        CargoType.TECHNOLOGY,
                        CargoType.OIL,
                        CargoType.GAS,
                        CargoType.PLASTIC,
                        CargoType.PASSENGERS,
                        CargoType.MAIL,
                        CargoType.WHEAT
                },
                values
        );
    }

    @Test
    void testValueOfEachEnumConstant() {
        assertEquals(CargoType.COAL, CargoType.valueOf("COAL"));
        assertEquals(CargoType.STEEL, CargoType.valueOf("STEEL"));
        assertEquals(CargoType.FOOD, CargoType.valueOf("FOOD"));
        assertEquals(CargoType.TECHNOLOGY, CargoType.valueOf("TECHNOLOGY"));
        assertEquals(CargoType.OIL, CargoType.valueOf("OIL"));
        assertEquals(CargoType.GAS, CargoType.valueOf("GAS"));
        assertEquals(CargoType.PLASTIC, CargoType.valueOf("PLASTIC"));
        assertEquals(CargoType.PASSENGERS, CargoType.valueOf("PASSENGERS"));
        assertEquals(CargoType.MAIL, CargoType.valueOf("MAIL"));
        assertEquals(CargoType.WHEAT, CargoType.valueOf("WHEAT"));
    }
}
