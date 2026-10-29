# US008 - Build a Railway Line

## 4. Tests 

**Test 1:** Check that a RailLine is added correctly to the map.

    @Test
    public void ensureAddRailLineAddsLineToMap() {
    Map map = new Map();
    RailLine line = new RailLine("Porto", "Lisboa", true, false);

    map.addRailLine(line);

    List<RailLine> lines = map.getRailLines();
    assertEquals(1, lines.size());
    assertEquals("Porto", lines.get(0).getStationA());
    assertEquals("Lisboa", lines.get(0).getStationB());
    }
	

**Test 2:** Check that player cannot afford a line if cost exceeds budget.

    @Test
    public void ensurePlayerCannotAffordLineWithInsufficientBudget() {
    Player player = new Player("Player1", 1000.0);
    double cost = 2000.0;

    boolean result = player.canAffordLine(cost);

    assertFalse(result);
}

_It is also recommended to organize this content by subsections._ 


## 5. Construction (Implementation)

### Class BuildLineController

```java
public RailLine createRailLine(Station start, Station end, boolean isElectrified, boolean isDouble) {
    RailLine line = new RailLine(start, end, isElectrified, isDouble);
    if (!player.hasSufficientBudget(line.getCost())) {
        return null;
    }
    player.setBudget(player.getBudget() - line.getCost());
    map.addRailLine(line);
    return line;
}
```

### Class RailLine

```java
private double calculateCost() {
    // Temos de ver o que colocar aqui
    double base = 6;
    if (isElectrified) base += 5;
    if (isDouble) base += 3;
    return base;
}
}
```


## 6. Integration and Demo 

* A new option on the Player menu options was added to build a RailLine.



## 7. Observations

n/a