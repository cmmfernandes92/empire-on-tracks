# US003 - Add a City

## 4. Tests 

**Test 1:** Check that it is not possible to add a city with a null name. 

	@Test
        void createCity_withNullName_throwsException() {
            Executable exec = () -> City.createCity(null, 0, 0, 1);
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class, exec);
            assertTrue(e.getMessage().contains("Invalid city name"));
        }
	

**Test 2:** Check that it is not possible to assign houseblocks manually with invalid coordinates. 

	@Test
        void assignHouseBlocksManual_withWrongSize_throwsException() {
            List<Location> tooFew = new ArrayList<>();
            tooFew.add(new Location(0, 0));
            Executable exec = () -> city.assignHouseBlocksManually(tooFew);
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class, exec);
            assertTrue(e.getMessage().contains("must match house block count"));
        }

_It is also recommended to organize this content by subsections._ 


## 5. Construction (Implementation)

### Class AddCityController 

```java
public City submitCityDetails(String name, int x, int y, int houseBlocks) {
    Objects.requireNonNull(name, "city name must not be null");
    city = City.createCity(name, x, y, houseBlocks);
    if (map.isCityNameDuplicate(name)) {
        throw new IllegalStateException("City name is already in use: " + name);
    }
    return city;
}

public City selectedAssignmentMethod(String method, List<Location> positions) {
    Objects.requireNonNull(method, "assignment method must not be null");
    switch (method.toLowerCase()) {
        case "automatic":
            city.assignHouseBlocksAutomatic();
            break;
        case "manual":
            if (!map.validateCoordinates(positions)) {
                throw new IllegalStateException("Coordinates out of map bounds");
            }
            city.assignHouseBlocksManually(positions);
            break;
        default:
            throw new IllegalArgumentException("Unknown assignment method: " + method);
    }
    return city;
}

public void confirmCityCreation() {
    map.addCity(city);
}


```

### Class City

```java
 public static City createCity(String name, int centerX, int centerY, int houseBlocksCount) {
    if (name == null || name.trim().isEmpty()) {
        throw new IllegalArgumentException("Invalid city name: " + name);
    }
    if (houseBlocksCount <= 0) {
        throw new IllegalArgumentException("Invalid number of house blocks: " + houseBlocksCount);
    }
    return new City(name, centerX, centerY, houseBlocksCount);
}

public void assignHouseBlocksAutomatic() {
    for (int i = 0; i < houseBlocksCount; i++) {
        Location pos = Location.computeBlockPosition(center);
        houseBlocks.add(HouseBlock.createAt(pos));
    }
}

public void assignHouseBlocksManually(List<Location> positions) {
    Objects.requireNonNull(positions, "positions must not be null");
    if (positions.size() != houseBlocksCount) {
        throw new IllegalArgumentException(
                "Number of positions (" + positions.size() + ") must match house block count (" + houseBlocksCount + ")");
    }
    for (Location pos : positions) {
        houseBlocks.add(HouseBlock.createAt(pos));
    }
}
```


## 6. Integration and Demo 

* A new option of adding a city as an Editor was added. 


## 7. Observations

n/a