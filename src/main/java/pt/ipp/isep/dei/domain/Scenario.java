package pt.ipp.isep.dei.domain;

import java.util.*;
import java.io.Serializable;

/**
 * Represents a scenario in the simulation, which includes a name, a time period,
 * available locomotive types, industries, and a list of events that occur during the scenario.
 * <p>
 * Scenarios are used to simulate various changes and events in the map's environment,
 * such as modifying industries, adding locomotives, and altering production rates.
 * </p>
 */
public class Scenario implements Serializable {

    private String name;
    private Period period;
    private List<LocomotiveType> availableTypes;
    private List<Industry> industries;
    private List<ScenarioEvent> events;

    /**
     * Constructs a new Scenario with the specified name, time period, and list of events.
     *
     * @param name      The name of the scenario.
     * @param startYear The starting year of the scenario.
     * @param endYear   The ending year of the scenario.
     * @param eventList The list of events to be applied in the scenario.
     */
    public Scenario(String name, int startYear, int endYear, List<ScenarioEvent> eventList) {
        this.name = name;
        this.period = new Period(startYear, endYear);
        this.industries = new ArrayList<>();
        this.events = eventList;
    }

    /**
     * Adds an industry to the scenario.
     *
     * @param industry The industry to be added.
     */
    public void addIndustry(Industry industry) {
        industries.add(industry);
    }

    /**
     * Retrieves the list of industries associated with the scenario.
     *
     * @return The list of industries in the scenario.
     */
    public List<Industry> getIndustries() {
        return industries;
    }

    /**
     * Retrieves the name of the scenario.
     *
     * @return The name of the scenario.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the period (start and end years) of the scenario.
     *
     * @return The period of the scenario.
     */
    public Period getPeriod() {
        return period;
    }

    /**
     * Retrieves the list of events that occur during the scenario.
     *
     * @return The list of events in the scenario.
     */
    public List<ScenarioEvent> getEvents() {
        return events;
    }

    /**
     * Applies all the events in the scenario to the specified map and locomotives.
     * <p>
     * This method iterates over all the events in the scenario and applies each of them
     * to the provided map and locomotives.
     * </p>
     *
     * @param map         The map where the events will be applied.
     * @param locomotives The list of locomotives that will be affected by the events.
     */
    public void applyAll(Map map, LocomotiveCatalog locomotives) {
        for (ScenarioEvent event : events) {
            event.apply(map, locomotives);  // Applies the event to the map and locomotives
        }
    }
}
