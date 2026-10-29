package pt.ipp.isep.dei.domain;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import pt.ipp.isep.dei.domain.CargoType;
import pt.ipp.isep.dei.domain.City;
import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.EnumMap;
import java.util.Map; // ⚠️ este vai colidir com pt.ipp.isep.dei.domain.Map


public class City implements Serializable, Industry {

    private String name;
    private int positionX;
    private int positionY;
    private int houseBlockCount;
    private String assignmentMethod;
    private List<HouseBlock> houseBlocks = new ArrayList<>();
    private static final Random random = new Random();

    private final Map<CargoType, Cargo> producedCargoMap = new EnumMap<>(CargoType.class);
    private final Map<CargoType, Cargo> consumedCargoMap = new EnumMap<>(CargoType.class);

    public City(String name, int positionX, int positionY, int houseBlockCount, String assignmentMethod) {
        this.name = name;
        this.positionX = positionX;
        this.positionY = positionY;
        this.houseBlockCount = houseBlockCount;
        this.assignmentMethod = assignmentMethod;

        producedCargoMap.put(CargoType.PASSENGERS, new Cargo(CargoType.PASSENGERS, 0));
        producedCargoMap.put(CargoType.MAIL, new Cargo(CargoType.MAIL, 0));

        for (CargoType type : CargoType.values()) {
            consumedCargoMap.put(type, new Cargo(type, 0));
        }
    }

    public String getName() {
        return name;
    }

    public Location getCenter() {
        return new Location(positionX, positionY);
    }

    public void validateName() {
        if (!name.matches("[a-zA-Z ]+")) {
            throw new IllegalArgumentException("Invalid city name.");
        }
    }

    public void validateHouseBlocks() {
        if (houseBlockCount < 1) {
            throw new IllegalArgumentException("House block count must be greater or equal to 1.");
        }
    }

    public void createHouseBlocksManually(List<Integer> posX, List<Integer> posY) {
        for (int i = 0; i < houseBlockCount; i++) {
            int blockX = posX.get(i);
            int blockY = posY.get(i);
            if (blockX < 0 || blockY < 0) continue;
            HouseBlock block = new HouseBlock(blockX, blockY);
            houseBlocks.add(block);
        }
    }

    public void createHouseBlocksAutomatically() {
        for (int i = 0; i < houseBlockCount; i++) {
            Location loc = computeRelativePosition();
            if ((loc.getX() <= 0 || loc.getY() <= 0) || (loc.getX() == positionX && loc.getY() == positionY)) {
                i--;
                continue;
            }
            houseBlocks.add(new HouseBlock(loc.getX(), loc.getY()));
        }
    }

    private Location computeRelativePosition() {
        double stddev = 2;
        int offsetX = (int) (random.nextGaussian() * stddev);
        int offsetY = (int) (random.nextGaussian() * stddev);
        return new Location(positionX + offsetX, positionY + offsetY);
    }

    @Override
    public void produceCargo(Player player) {
        double passengers = 2.0 * houseBlockCount;
        double mail = 1.0 * houseBlockCount;
        producedCargoMap.get(CargoType.PASSENGERS).addQuantity(passengers);
        producedCargoMap.get(CargoType.MAIL).addQuantity(mail);
    }

    @Override
    public void consumeCargo(double ignored) {
        double quantityPerType = 0.5 * houseBlockCount;
        for (Cargo cargo : consumedCargoMap.values()) {
            cargo.removeQuantity(quantityPerType);
        }
    }

    public void receiveCargo(CargoType type, double quantity) {
        if (consumedCargoMap.containsKey(type)) {
            consumedCargoMap.get(type).addQuantity(quantity);
        }
    }

    @Override
    public String getIndustryName() {
        return name;
    }

    @Override
    public int getIndustryCost() {
        return 0;
    }

    @Override
    public double getProduction() {
        return 3.0 * houseBlockCount;
    }

    @Override
    public double getDemand() {
        return 0.5 * houseBlockCount * CargoType.values().length;
    }

    @Override
    public Location getLocation() {
        return new Location(positionX, positionY);
    }

    @Override
    public void setLocation(Location location) {
        this.positionX = location.getX();
        this.positionY = location.getY();
    }

    @Override
    public Cargo getCargo() {
        return producedCargoMap.get(CargoType.PASSENGERS);
    }

    @Override
    public CargoType getInputCargoType() {
        return null;
    }

    @Override
    public double getInputCargoQuantity() {
        return 0;
    }

    @Override
    public CargoType getOutputCargoType() {
        return CargoType.PASSENGERS;
    }

    @Override
    public double getOutputCargoQuantity() {
        return producedCargoMap.get(CargoType.PASSENGERS).getQuantity();
    }

    @Override
    public void setProduction(double amount) {
        double half = amount / 2;
        producedCargoMap.get(CargoType.PASSENGERS).setQuantity(half);
        producedCargoMap.get(CargoType.MAIL).setQuantity(half);
    }

    public Map<CargoType, Cargo> getProducedCargoMap() {
        return producedCargoMap;
    }

    public Map<CargoType, Cargo> getConsumedCargoMap() {
        return consumedCargoMap;
    }

    public List<HouseBlock> getHouseBlocks() {
        return houseBlocks;
    }

    public int getHouseBlockCount() {
        return houseBlockCount;
    }
}
