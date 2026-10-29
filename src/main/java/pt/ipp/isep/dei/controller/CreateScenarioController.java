package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.mapper.dto.IndustryDTO1;
import pt.ipp.isep.dei.repository.LocomotiveRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CreateScenarioController {
    private final Map map;
    private String name;
    private int startYear;
    private int endYear;
    private final List<ScenarioEvent> eventList = new ArrayList<>();

    public CreateScenarioController(Map map) {
        this.map = map;
    }

    public boolean validateYears(int start, int end) {
        return start >= 1804 && end >= start;
    }

    public void addLocomotiveType(String typeName) {
        List<Locomotive> locomotivesOfType = LocomotiveRepository.getInstance().getAllTemplates().stream()
                .filter(locomotive -> locomotive.getType().equalsIgnoreCase(typeName))
                .collect(Collectors.toList());

        if (!locomotivesOfType.isEmpty()) {
            locomotivesOfType.forEach(locomotive ->
                    eventList.add(new AddTemplateLocomotiveEvent(locomotive)));
            System.out.println(locomotivesOfType.size() + " locomotives of type " + typeName + " added.");
        } else {
            System.out.println("No locomotives of type " + typeName + " found in the repository.");
        }
    }

    public void addProductionRateChange(int industryIndex, double percentage) {
        eventList.add(new ChangeIndustryProductionRateEvent(industryIndex, percentage));
    }

    public void addCargoChange(int industryIndex, CargoType newInput, CargoType newOutput) {
        if (newInput != null || newOutput != null) {
            eventList.add(new ChangeIndustryCargoEvent(industryIndex, newInput, newOutput));
        }
    }

    public void addMixedIndustryBehavior(int index, MixedBehavior behavior,
                                         CargoType input, CargoType output) {
        eventList.add(new ChangeMixedIndustryBehaviorEvent(index, behavior, input, output));
    }


    public void setScenarioInfo(String name, int start, int end) {
        this.name = name;
        this.startYear = start;
        this.endYear = end;
    }

    public void createScenario() {
        Scenario s = new Scenario(name, startYear, endYear, eventList);
        map.addScenario(s);
    }

    public void showScenarioDetails() {
        System.out.println("Scenario Name: " + name);
        System.out.println("Start Year: " + startYear);
        System.out.println("End Year: " + endYear);
        System.out.println("Industries and Cargo Details:");
        for (Industry industry : map.getIndustries()) {
            System.out.println("Industry: " + industry.getIndustryName());
            System.out.println("Input Cargo: " + industry.getInputCargoType() + " - " + industry.getInputCargoQuantity());
            System.out.println("Output Cargo: " + industry.getOutputCargoType() + " - " + industry.getOutputCargoQuantity());
            System.out.println("-----------------------------");
        }
    }

    public List<String> getLocomotiveTypeNames() {
        return LocomotiveRepository.getInstance().getAllTemplates().stream()
                .map(Locomotive::getType)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<IndustryDTO1> getIndustryDTOs() {
        return map.getIndustries().stream()
                .filter(industry -> industry instanceof PrimaryIndustry)
                .map(industry -> new IndustryDTO1(
                        map.getIndustries().indexOf(industry),
                        industry.getIndustryName(),
                        industry.getInputCargoType() != null ? industry.getInputCargoType().toString() : "None",
                        industry.getInputCargoQuantity(),
                        industry.getOutputCargoType() != null ? industry.getOutputCargoType().toString() : "None",
                        industry.getOutputCargoQuantity()
                ))
                .collect(Collectors.toList());
    }

    public List<IndustryDTO1> getMixedIndustryDTOs() {
        return map.getIndustries().stream()
                .filter(industry -> industry instanceof MixedIndustry)
                .map(industry -> new IndustryDTO1(
                        map.getIndustries().indexOf(industry),
                        industry.getIndustryName(),
                        industry.getInputCargoType() != null ? industry.getInputCargoType().toString() : "None",
                        industry.getInputCargoQuantity(),
                        industry.getOutputCargoType() != null ? industry.getOutputCargoType().toString() : "None",
                        industry.getOutputCargoQuantity()
                ))
                .collect(Collectors.toList());
    }
}
