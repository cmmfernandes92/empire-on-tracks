package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player(1_000.0);
    }

    @Test
    void constructorRejectsNegativeBudget() {
        assertThrows(IllegalArgumentException.class, () -> new Player(-0.1));
    }

    @Test
    void getAndSetBudget() {
        assertEquals(1_000.0, player.getBudget());
        player.setBudget(500.0);
        assertEquals(500.0, player.getBudget());
        assertThrows(IllegalArgumentException.class, () -> player.setBudget(-1.0));
    }

    @Test
    void hasSufficientBudget() {
        assertTrue(player.hasSufficientBudget(999.99));
        assertTrue(player.hasSufficientBudget(1_000.0));
        assertFalse(player.hasSufficientBudget(1_000.01));
    }

    @Test
    void spendAndEarn() {
        assertTrue(player.spend(200.0));
        assertEquals(800.0, player.getBudget());
        assertFalse(player.spend(1_000.0));            // not enough left
        assertThrows(IllegalArgumentException.class, () -> player.spend(-5.0));

        assertTrue(player.earn(100.0));
        assertEquals(900.0, player.getBudget());
        assertThrows(IllegalArgumentException.class, () -> player.earn(-1.0));
    }



    @Test
    void hasBudgetForIndustryCost() {
        assertTrue(player.hasBudgetFor(500));
        assertFalse(player.hasBudgetFor(1500));
    }
}
