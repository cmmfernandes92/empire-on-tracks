// File: pt/ipp/isep/dei/domain/SimulationSnapshot.java
package pt.ipp.isep.dei.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Encapsula todo o estado necessário para salvar e retomar uma simulação.
 */
public class SimulationSnapshot implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Map map;
    private final Time clock;
    private final Player player;
    private final Scenario scenario;
    private final List<Train> trains;

    public SimulationSnapshot(Map map,
                              Time clock,
                              Player player,
                              Scenario scenario,
                              List<Train> trains) {
        this.map = map;
        this.clock = clock;
        this.player = player;
        this.scenario = scenario;
        this.trains = trains;
    }

    public Map getMap() {
        return map;
    }

    public Time getClock() {
        return clock;
    }

    public Player getPlayer() {
        return player;
    }

    public Scenario getScenario() {
        return scenario;
    }

    public List<Train> getTrains() {
        return trains;
    }
}
