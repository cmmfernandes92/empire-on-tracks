# US007- List all the Stations by Details

## 4. Tests 

**Test 1:** Check that getAllNamesStation() returns all added station names.

    @Test
    public void ensureGetAllNamesStationReturnsCorrectList() {
    Map map = new Map();
    map.addStation(new Station("1", "Porto", new Location(1, 1), 3));
    map.addStation(new Station("2", "Lisboa", new Location(2, 2), 3));

    List<String> result = map.getAllNamesStation();
    assertEquals(2, result.size());
    assertTrue(result.contains("Porto"));
    assertTrue(result.contains("Lisboa"));
    }
	

**Test 2:** Check that getDetails() returns the correct station if it exists. 

	@Test
    public void ensureGetDetailsReturnsCorrectStation() {
    Map map = new Map();
    Station s = new Station("1", "Braga", new Location(0, 0), 2);
    map.addStation(s);

    Station result = map.getDetails("Braga");
    assertNotNull(result);
    assertEquals("Braga", result.getStationName());
    }

**Test 3:** Check that getDetails() returns null when the station does not exist.

     @Test
     public void ensureGetDetailsReturnsNullForUnknownStation() {
     Map map = new Map();
     Station result = map.getDetails("Unknown");
     assertNull(result);
     }

_It is also recommended to organize this content by subsections._ 


## 5. Construction (Implementation)

### Class CreateTaskController 

```java
public Task createTask(String reference, String description, String informalDescription, String technicalDescription,
                       Integer duration, Double cost, String taskCategoryDescription) {

	TaskCategory taskCategory = getTaskCategoryByDescription(taskCategoryDescription);

	Employee employee = getEmployeeFromSession();
	Organization organization = getOrganizationRepository().getOrganizationByEmployee(employee);

	newTask = organization.createTask(reference, description, informalDescription, technicalDescription, duration,
                                      cost,taskCategory, employee);
    
	return newTask;
}
```


### Class ListStationController
```java
public Station getStationDetails(String stationName) {
    Station station = getStationByName(stationName);

    if (station == null) {
        System.out.println("Station not found.");
        return null;
    }

    if (station.getBuildings() == null || station.getBuildings().isEmpty()) {
        System.out.println("Station found, but it has no buildings.");
        return station;
    }

    return station;
}

```



## 6. Integration and Demo 

* A new option was added in the Player menu to list all stations and view their details.



## 7. Observations

n/a