# US009 - Buy a Locomotive

## 4. Tests 

### LocomotiveTest

**Test 1:** Check that the constructor correctly sets all fields and getters return expected values.

```java
@Test
void testConstructorAndGetters() {
    Locomotive loco = new Locomotive(
            "L001", "Diesel", 10000, false,
            2200, 1.8, 120.0, 1965,
            1300.5, 800.0
    );

    assertEquals("L001", loco.getId());
    assertEquals("Diesel", loco.getType());
    assertEquals(10000, loco.getPrice());
    assertFalse(loco.requiresElectrification());
    assertEquals(2200, loco.getPower());
    assertEquals(1.8, loco.getAcceleration());
    assertEquals(120.0, loco.getTopSpeed());
    assertEquals(1965, loco.getStartYear());
    assertEquals(1300.5, loco.getFuelCostPerYear());
    assertEquals(800.0, loco.getMaintenancePerYear());
}
```

**Test 2:** Check that toString() includes all key field values in its output.

```java
	@Test void testToStringContainsAllFields() { 
Locomotive loco = new Locomotive(
"L002", "Steam", 7500, false,
1800, 1.2, 90.0, 1930,
950.0, 600.0
);
String result = loco.toString();
assertTrue(result.contains("L002"));
assertTrue(result.contains("Steam"));
assertTrue(result.contains("7500"));
assertTrue(result.contains("false"));
assertTrue(result.contains("1800"));
assertTrue(result.contains("1.20")); // acceleration
assertTrue(result.contains("90.00")); // top speed
assertTrue(result.contains("1930"));
assertTrue(result.contains("950.00"));
assertTrue(result.contains("600.00"));
}
```


## 5. Construction (Implementation)

### Class BuyLocomotiveController 

```java
public class BuyLocomotiveController {
    private Player player;

    public BuyLocomotiveController(Player player) {
        this.player = player;
    }

    public List<Locomotive> requestAvailableLocomotives() {
        return Repositories.getInstance().getLocomotiveCatalog().getAvailableLocomotives();
    }
    public int buyLocomotiveAsTrain(String locomotiveID, String trainName) {
        // 1. Check for unique train name
        for (Train t : player.getOwnedTrains()) {
            if (t.getName().equalsIgnoreCase(trainName)) {
                return 2; // Train name already exists
            }
        }

        // 2. Find selected locomotive
        List<Locomotive> available = requestAvailableLocomotives();
        Locomotive chosen = null;
        for (Locomotive loco : available) {
            if (loco.getId().equals(locomotiveID)) {
                chosen = loco;
                break;
            }
        }
        if (chosen == null) return 3; // Not found

        // 3. Check budget
        if (!player.hasSufficientBudget(chosen.getPrice())) {
            return 1; // Not enough money
        }

        // 4. Deduct budget and add to ownedLocomotives (optional)
        player.buyLocomotive(chosen);

        // 5. Create and add new train
        Train newTrain = new Train(trainName, chosen, List.of(), List.of());
        player.addTrain(newTrain);

        return 0; // Success
    }

    public Player getPlayer() {
        return player;
    }
}
```

### Class Locomotive

```java
public class Locomotive {
    // Identification & economics
    private final String id;
    private final String type;                  // "Steam", "Diesel", "Electric"
    private final double price;
    private final boolean requiresElectrification;
    private List<Station> route = List.of();
    private int currentIndex = 0;


    // Technical specs
    private final int power;                    // kW or HP
    private final double acceleration;          // m/s² or similar
    private final double topSpeed;              // km/h
    private final int startYear;                // Year in which locomotive becomes available

    // Operating costs
    private final double fuelCostPerYear;       // e.g., €/year
    private final double maintenancePerYear;    // e.g., €/year


    private final double capacity = 0;              // max cargo volume the loco can carry
    private final List<Cargo> currentLoad = new ArrayList<>();

    // scenario availability
    private Period availability;

    /**
     * Constructs a new Locomotive instance.
     */
    public Locomotive(String id,
                      String type,
                      double price,
                      boolean requiresElectrification,
                      int power,
                      double acceleration,
                      double topSpeed,
                      int startYear,
                      double fuelCostPerYear,
                      double maintenancePerYear) {
        this.id                      = Objects.requireNonNull(id);
        this.type                    = Objects.requireNonNull(type);
        this.price                   = price;
        this.requiresElectrification = requiresElectrification;
        this.power                   = power;
        this.acceleration            = acceleration;
        this.topSpeed                = topSpeed;
        this.startYear               = startYear;
        this.fuelCostPerYear         = fuelCostPerYear;
        this.maintenancePerYear      = maintenancePerYear;
        setRoute(route);
    }

    // Getters
    public String getId()                    { return id; }
    public String getType()                  { return type; }
    public double getPrice()                 { return price; }
    public boolean requiresElectrification() { return requiresElectrification; }
    public int getPower()                    { return power; }
    public double getAcceleration()          { return acceleration; }
    public double getTopSpeed()              { return topSpeed; }
    public int getStartYear()                { return startYear; }
    public double getFuelCostPerYear()       { return fuelCostPerYear; }
    public double getMaintenancePerYear()    { return maintenancePerYear; }
    public double getCapacity()              { return capacity; }

    /** Returns an unmodifiable copy of the route. */
    public List<Station> getRoute()          { return List.copyOf(route); }

    /** Sets a new route and resets position to the first station. */
    public void setRoute(List<Station> route) {
        this.route = new ArrayList<>(route);
        this.currentIndex = 0;
    }
    public Station getCurrentStation() {
        return route.isEmpty() ? null : route.get(currentIndex);
    }
    public void move() {
        if (!route.isEmpty())
            currentIndex = (currentIndex + 1) % route.size();
    }




    /**
     * Unloads all cargo to the current station and clears internal load.
     */
    public void unloadToStation() {
        Station station = getCurrentStation();
        if (station == null) return;
        for (Cargo c : new ArrayList<>(currentLoad)) {
            double qty = c.getQuantity();
            if (qty > 0) {
                station.unloadCargo(c.getType(), qty);
                c.setQuantity(0);
            }
        }
        currentLoad.removeIf(c -> c.getQuantity() == 0);
    }

    public double spaceLeft() {
        double used = currentLoad.stream().mapToDouble(Cargo::getQuantity).sum();
        return capacity - used;
    }

    private void addToLoad(CargoType type, double qty) {
        for (Cargo c : currentLoad) {
            if (c.getType() == type) {
                c.addQuantity(qty);
                return;
            }
        }
        currentLoad.add(new Cargo(type, qty));
    }

    public Period getAvailability()         { return availability; }
    public void setAvailability(Period a)   { this.availability = a; }

    @Override
    public String toString() {
        return String.format(
                "Locomotive[id=%s,type=%s,at=%s,load=%s]",
                id,
                type,
                Optional.ofNullable(getCurrentStation()).map(Station::getStationName).orElse("none"),
                currentLoad
        );
    }
}
```

### Class Locomotive

```java
public Optional<Task> createTask(String reference, String description, String informalDescription,
                                 String technicalDescription, Integer duration, Double cost, TaskCategory taskCategory,
                                 Employee employee) {
    
    Task task = new Task(reference, description, informalDescription, technicalDescription, duration, cost,
                         taskCategory, employee);

    addTask(task);
        
    return task;
}
```

### Class Train

```java
public class Train {
    private String name;
    private Locomotive locomotive;
    private List<Carriage> carriages;
    private List<String> currentCargoes;
    private Route route;
    private int currentStopIndex = 0;


    public Train(String name,
                 Locomotive locomotive,
                 List<Carriage> carriages,
                 List<String> currentCargoes) {
        this.name = Objects.requireNonNull(name);
        this.locomotive = Objects.requireNonNull(locomotive);
        this.carriages = Objects.requireNonNull(carriages);
        this.currentCargoes = Objects.requireNonNull(currentCargoes);
    }

    public String getName() {
        return name;
    }

    public String getLocomotiveType() {
        return locomotive != null ? locomotive.getType() : "";
    }

    public List<String> getCurrentCargoes() {
        return currentCargoes;
    }

    public void assignRoute(Route route) {
        this.route = Objects.requireNonNull(route);
        this.currentStopIndex = 0;
    }


    public Route getRoute() {
        return route;
    }

    @Override
    public String toString() {
        return String.format("Train[name=%s, loco=%s, route=%s]",
                name,
                locomotive.getType(),
                route != null ? route.toString() : "none");
    }

}

```

### Class Locomotive Catalog

```java
public class LocomotiveCatalog {
    private List<Locomotive> allLocomotives = new ArrayList<>();

    public void addLocomotive(Locomotive loco) {
        allLocomotives.add(loco);
    }

    public List<Locomotive> getAvailableLocomotives() {
        return new ArrayList<>(allLocomotives);
    }
}
```

## 6. Integration and Demo 

* A new option on the Player menu options to "Buy a Locomotive" was added.

## 7. Observations

n/a