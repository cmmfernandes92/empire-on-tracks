# US004 - Create a Scenario

## 4. Tests 

**Test 1:** Check that it is not possible to create an instance of the Scenario class with null values. 

	@Test
    void setPeriod_withNull_throwsException() {
        assertThrows(NullPointerException.class, () -> scenario.setPeriod(null));
    }
	

**Test 2:** Check that it is not possible to create an instance of the Scenario class with a null locomotive type. 

	@Test
    void addLocomotive_withNull_throwsException() {
        assertThrows(NullPointerException.class, () -> scenario.addLocomotive(null));
    }

_It is also recommended to organize this content by subsections._ 


## 5. Construction (Implementation)

### Class CreateScenarioController 

```java
public void startCreateScenario(LocalDateTime triggerDate, int periodStart, int periodEnd) {
    Objects.requireNonNull(triggerDate, "triggerDate must not be null");
    Period period = new Period(periodStart, periodEnd);

    this.scenario = new Scenario(triggerDate);
    this.scenario.setPeriod(period);
    mapRepo.getCurrentMap();
}

public List<Locomotive> getAllLocomotiveTemplates() {
    return locoRepo.getAllTemplates();
}

public void addLocomotiveByTemplate(int templateIndex) {
    Locomotive clone = locoRepo.getTemplate(templateIndex);
    if (clone == null) {
        throw new IllegalArgumentException("Invalid locomotive template index: " + templateIndex);
    }
    scenario.addLocomotive(clone);
}


public void setLocomotiveAvailability(int locoIndex, int periodStart, int periodEnd) {
    Period availability = new Period(periodStart, periodEnd);
    List<Locomotive> list = scenario.getLocomotives();
    if (locoIndex < 0 || locoIndex >= list.size()) {
        throw new IllegalArgumentException("Invalid locomotive index: " + locoIndex);
    }
    list.get(locoIndex).setAvailability(availability);
}


public void addCargoType(CargoType cargoType) {
    Objects.requireNonNull(cargoType, "cargoType must not be null");
    Cargo c = new Cargo(cargoType,0);
    cargoRepo.addAvailableCargo(c);
    scenario.addCargo(c);
}

public void setCargoBehavior(CargoType cargoType, String behavior) {
    scenario.getCargos().stream()
            .filter(c -> c.getType() == cargoType)
            .findFirst()
            .ifPresent(c -> c.setBehavior(behavior));
}


public void setIndustryProductionFrequency(int percent) {
    scenario.setIndustryProductionFrequency(percent);
}

public boolean confirmScenarioCreation() {
    scenarioRepo.addScenario(scenario);
    return true;
}
```

### Class Scenario

```java
public void addLocomotive(Locomotive loco) {
    locomotives.add(Objects.requireNonNull(loco, "loco must not be null"));
}

public List<Locomotive> getLocomotives() {
    return new ArrayList<>(locomotives);
}

public void addCargo(Cargo cargo) {
    cargos.add(Objects.requireNonNull(cargo, "cargo must not be null"));
}

public List<Cargo> getCargos() {
    return new ArrayList<>(cargos);
}
public void setIndustryProductionFrequency(int percent) {
    if (percent < 0 || percent > 100) {
        throw new IllegalArgumentException("percent must be between 0 and 100");
    }
    this.industryProductionFrequency = percent;
}
```


## 6. Integration and Demo 

* A new option to create a scenario was added.


## 7. Observations

n/a