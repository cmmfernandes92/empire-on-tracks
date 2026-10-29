package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MixedIndustryTest {

    private MixedIndustry industry;
    private Player player;

    @BeforeEach
    void setUp() {
        industry = new MixedIndustry(
                "FoodFactory", 200, 50.0, 30.0,
                CargoType.WHEAT, CargoType.FOOD
        );
        player = new Player(5000);
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals("FoodFactory", industry.getIndustryName());
        assertEquals(200, industry.getIndustryCost());
        assertEquals(50.0, industry.getProduction());
        assertEquals(30.0, industry.getDemand());
        assertEquals(CargoType.WHEAT, industry.getInputCargoType());
        assertEquals(0.0, industry.getInputCargoQuantity());
        assertEquals(CargoType.FOOD, industry.getOutputCargoType());
        assertEquals(0.0, industry.getOutputCargoQuantity());
        assertNull(industry.getLocation());
        assertEquals(MixedBehavior.TRANSFORM, industry.getBehavior());
    }

    @Test
    void testSettersAndLocation() {
        industry.setProduction(22.0);
        assertEquals(22.0, industry.getProduction());

        Location loc = new Location(2, 2);
        industry.setLocation(loc);
        assertEquals(loc, industry.getLocation());

        industry.setBehavior(MixedBehavior.EXPORT);
        assertEquals(MixedBehavior.EXPORT, industry.getBehavior());
    }

    @Test
    void testSetInputCargoAndSetOutputCargo() {
        industry.setInputCargo(CargoType.OIL);
        assertEquals(CargoType.OIL, industry.getInputCargoType());
        assertEquals(0.0, industry.getInputCargoQuantity());

        industry.setOutputCargo(CargoType.COAL);
        assertEquals(CargoType.COAL, industry.getOutputCargoType());
        assertEquals(0.0, industry.getOutputCargoQuantity());
    }

    @Test
    void testSetInputCargoQuantity() {
        industry.setInputCargoQuantity(77.7);
        assertEquals(77.7, industry.getInputCargoQuantity());
    }

    @Test
    void testConsumeCargoAddsToInput() {
        industry.consumeCargo(10.5);
        assertEquals(10.5, industry.getInputCargoQuantity());
    }



    @Test
    void testProduceCargoImportMode() {
        industry.setBehavior(MixedBehavior.IMPORT);
        double initialBudget = player.getBudget();
        industry.produceCargo(player);
        // Player pays cost, receives output cargo
        assertEquals(initialBudget - 200, player.getBudget());
        assertEquals(50.0, industry.getOutputCargoQuantity());
    }

    @Test
    void testProduceCargoImportModeFailsIfInsufficientBudget() {
        player.setBudget(100);
        industry.setBehavior(MixedBehavior.IMPORT);
        industry.produceCargo(player);
        // Budget not enough, cargo should not be produced
        assertEquals(100, player.getBudget());
        assertEquals(0.0, industry.getOutputCargoQuantity());
    }

    @Test
    void testProduceCargoExportMode() {
        industry.setBehavior(MixedBehavior.EXPORT);
        industry.setInputCargoQuantity(100.0);
        double prevBudget = player.getBudget();
        industry.produceCargo(player);
        // Input removed, budget increased by (100/50)*1000 = 2000
        assertEquals(prevBudget + 2000, player.getBudget());
        assertEquals(0.0, industry.getInputCargoQuantity());
    }

    @Test
    void testProduceCargoExportModeWithNoInputDoesNothing() {
        industry.setBehavior(MixedBehavior.EXPORT);
        double prevBudget = player.getBudget();
        industry.produceCargo(player);
        assertEquals(prevBudget, player.getBudget());
        assertEquals(0.0, industry.getInputCargoQuantity());
    }



    @Test
    void testProduceCargoTransformModeNotEnoughInput() {
        industry.setBehavior(MixedBehavior.TRANSFORM);
        industry.setInputCargoQuantity(20.0);
        industry.produceCargo(player);
        // Not enough input: should do nothing
        assertEquals(20.0, industry.getInputCargoQuantity());
        assertEquals(0.0, industry.getOutputCargoQuantity());
    }
}
