package pt.ipp.isep.dei.mapper.dto;

public class LocomotiveDTO {
    private String id;
    private String type;
    private double cost;


    public LocomotiveDTO (String id, String type, double cost) {
        this.id = id;
        this.type = type;
        this.cost = cost;
    }

    // Getters and Setters
    public String getId() { return id; }
    public String getType() { return type; }
    public double getCost() { return cost; }


}
