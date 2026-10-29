package pt.ipp.isep.dei.repository;

import pt.ipp.isep.dei.domain.Period;
import pt.ipp.isep.dei.domain.Scenario;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ScenarioRepository {
    private final List<Scenario> scenarios = new ArrayList<>();

    public void addScenario(Scenario scenario) {
        scenarios.add(scenario);
    }

    public Scenario getScenarioByName(String name) {
        for (Scenario scenario : scenarios) {
            if (scenario.getName().equalsIgnoreCase(name)) {
                return scenario;
            }
        }
        return null; // ou lança uma exceção se quiseres sinalizar erro
    }
}