package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransformingIndustryTest {

    private TransformingIndustry industry;

    @BeforeEach
    void setUp() {
        industry = new TransformingIndustry(
                "Bakery", 100, 40.0, 25.0,
                CargoType.WHEAT, CargoType.FOOD
        );
    }

    @Test
    void testConstructorAssignsFields() {
        assertEquals("Bakery", industry.getIndustryName());
        assertEquals(100, industry.getIndustryCost());
        assertEquals(40.0, industry.getProduction());
        assertEquals(25.0, industry.getDemand());
        assertEquals(CargoType.WHEAT, industry.getInputCargoType());
        assertEquals(CargoType.FOOD, industry.getOutputCargoType());
        assertNull(industry.getLocation());
        assertEquals(0.0, industry.getCargo().getQuantity());
        assertEquals(0.0, industry.getOutputCargoQuantity());
    }

    @Test
    void testConstructorWithLocation() {
        Location loc = new Location(2, 7);
        TransformingIndustry withLoc = new TransformingIndustry(
                "Mill", 150, 30.0, 10.0, loc, CargoType.COAL, CargoType.OIL
        );
        assertEquals(loc, withLoc.getLocation());
        assertEquals("Mill", withLoc.getIndustryName());
        assertEquals(CargoType.COAL, withLoc.getInputCargoType());
        assertEquals(CargoType.OIL, withLoc.getOutputCargoType());
    }

    @Test
    void testSetAndGetLocation() {
        Location loc = new Location(3, 5);
        industry.setLocation(loc);
        assertEquals(loc, industry.getLocation());
    }

    @Test
    void testSetAndGetProductionAndDemand() {
        industry.setProduction(55.5);
        assertEquals(55.5, industry.getProduction());
        industry.setDemand(11.2);
        assertEquals(11.2, industry.getDemand());
    }


    @Test
    void testSetOutputCargoType() {
        industry.setOutputCargo(CargoType.COAL);
        assertEquals(CargoType.COAL, industry.getOutputCargoType());
        // Should reset quantity to 0
        assertEquals(0.0, industry.getCargo().getQuantity());
    }

}
