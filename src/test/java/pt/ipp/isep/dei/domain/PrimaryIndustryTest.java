package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrimaryIndustryTest {

    private PrimaryIndustry oilIndustry;
    private PrimaryIndustry defaultIndustry;

    @BeforeEach
    void setUp() {
        oilIndustry = new PrimaryIndustry("OilWell", 150, 30.0, 10.0, CargoType.OIL);
        defaultIndustry = new PrimaryIndustry();
    }

    @Test
    void testConstructorAssignsFields() {
        assertEquals("OilWell", oilIndustry.getIndustryName());
        assertEquals(150, oilIndustry.getIndustryCost());
        assertEquals(30.0, oilIndustry.getProduction());
        assertEquals(10.0, oilIndustry.getDemand());
        assertEquals(CargoType.OIL, oilIndustry.getCargoType());
        assertNull(oilIndustry.getLocation());
    }

    @Test
    void testDefaultConstructor() {
        assertEquals("Industry", defaultIndustry.getIndustryName());
        assertEquals(20, defaultIndustry.getIndustryCost());
        assertEquals(20.0, defaultIndustry.getProduction());
        assertEquals(0.0, defaultIndustry.getDemand());
        assertEquals(CargoType.OIL, defaultIndustry.getCargoType());
    }

    @Test
    void testFullConstructorWithLocation() {
        Location loc = new Location(5, 7);
        PrimaryIndustry ind = new PrimaryIndustry("WheatFarm", 100, 25, 8, loc, CargoType.WHEAT);
        assertEquals(loc, ind.getLocation());
        assertEquals(CargoType.WHEAT, ind.getCargoType());
    }

    @Test
    void testSetAndGetProductionAndDemand() {
        oilIndustry.setProduction(99.5);
        oilIndustry.setDemand(2.1);
        assertEquals(99.5, oilIndustry.getProduction());
        assertEquals(2.1, oilIndustry.getDemand());
    }

    @Test
    void testSetAndGetLocation() {
        Location l = new Location(2, 3);
        oilIndustry.setLocation(l);
        assertEquals(l, oilIndustry.getLocation());
    }

    @Test
    void testProduceCargoAddsToStock() {
        double initial = oilIndustry.getCargo().getQuantity();
        oilIndustry.produceCargo(null); // player parameter is ignored
        assertEquals(initial + oilIndustry.getProduction(), oilIndustry.getCargo().getQuantity());
    }

    @Test
    void testConsumeCargoRemovesFromStock() {
        oilIndustry.produceCargo(null);
        oilIndustry.consumeCargo(5.0);
        assertEquals(oilIndustry.getProduction() - 5.0, oilIndustry.getCargo().getQuantity());
    }

    @Test
    void testSetCargoTypeChangesType() {
        oilIndustry.setCargoType(CargoType.COAL);
        assertEquals(CargoType.COAL, oilIndustry.getCargoType());
    }

    @Test
    void testSetCargoTypeNullThrows() {
        assertThrows(NullPointerException.class, () -> oilIndustry.setCargoType(null));
    }

    @Test
    void testToStringIncludesNameAndType() {
        oilIndustry.produceCargo(null);
        String s = oilIndustry.toString();
        assertTrue(s.contains("OilWell"));
        assertTrue(s.contains("OIL"));
    }

    @Test
    void testInterfaceStubsReturnDefaults() {
        assertNull(oilIndustry.getInputCargoType());
        assertEquals(0, oilIndustry.getInputCargoQuantity());
        assertNull(oilIndustry.getOutputCargoType());
        assertEquals(0, oilIndustry.getOutputCargoQuantity());
    }
}
