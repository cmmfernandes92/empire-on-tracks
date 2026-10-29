package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import pt.ipp.isep.dei.domain.*;

import java.util.ArrayList;
import java.util.List;

public class ScenarioTest {

    private Scenario scenario;
    private List<ScenarioEvent> eventList;

    @BeforeEach
    public void setUp() {
        // Set up the event list (empty to start)
        eventList = new ArrayList<>();
    }

    // Test applying events (but not relying on other classes like Map or Locomotive)
    @Test
    public void testApplyAllWithNoEffect() {
        // Create the scenario
        scenario = new Scenario("Test Scenario", 1900, 2000, eventList);

        // Add an event that does nothing (this is an empty implementation)
        ScenarioEvent noOpEvent = new ScenarioEvent() {
            @Override
            public void apply(Map map, LocomotiveCatalog locomotives) {
                // No operation, does nothing
            }
        };
        eventList.add(noOpEvent);

        // Apply all events (should have no effect since the event does nothing)
        scenario.applyAll(null, null);

        // Verify that events were applied (nothing changes in the scenario)
        assertEquals(1, scenario.getEvents().size());
    }




}

