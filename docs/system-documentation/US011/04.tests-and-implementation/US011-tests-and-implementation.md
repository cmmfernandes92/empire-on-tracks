# US011 - List All Trains

## 4. Tests 

**Test 1:** Check that the constructor correctly sets the type and capacity for different carriage types.
```java
	@Test
void testCarriageConstructorAndGetters() {
Carriage cargoCarriage = new Carriage("cargo", 50);
Carriage passengerCarriage = new Carriage("passenger", 100);

    assertEquals("cargo", cargoCarriage.getType());
    assertEquals(50, cargoCarriage.getCapacity());
    assertEquals("passenger", passengerCarriage.getType());
    assertEquals(100, passengerCarriage.getCapacity());
}
```

**Test 2:** Check that getType() returns the correct carriage type. 
```java
@Test
void getType_returnsType() {
    Carriage c = new Carriage("passenger", 80);
    assertEquals("passenger", c.getType());
}

```


**Test 3:** Check that getCapacity() returns the correct capacity value.
```java
@Test
void getCapacity_returnsCapacity() {
    Carriage c = new Carriage("mail", 10);
    assertEquals(10, c.getCapacity());
}
```

## 5. Construction (Implementation)

### Class ListTrainsController 

```java
/**
 * Controller for listing trains owned by the player.
 */
public class ListTrainsController {
    private Player player;

    public ListTrainsController(Player player) {
        this.player = player;
    }

    /**
     * Returns the list of owned trains, grouped by locomotive type and sorted by name.
     */
    public List<Train> requestTrainList() {
        List<Train> trains = player.getOwnedTrains();
        return groupAndSortTrains(trains);
    }

    /**
     * Groups by locomotive type, sorts by name.
     */
    private List<Train> groupAndSortTrains(List<Train> trains) {
        return trains.stream()
                .sorted(Comparator.comparing(Train::getLocomotiveType)
                        .thenComparing(Train::getName))
                .collect(Collectors.toList());
    }
}
```

### Class Carriage

```java
/**
 * Represents a train carriage.
 */
public class Carriage {
    private String type; // e.g., "cargo" or "passenger"
    private int capacity;

    public Carriage(String type, int capacity) {
        this.type = type;
        this.capacity = capacity;
    }

    public String getType() {
        return type;
    }

    public int getCapacity() {
        return capacity;
    }
}

```


## 6. Integration and Demo 

* A new option on the Player menu options to "List All Trains" was added.

## 7. Observations

n/a