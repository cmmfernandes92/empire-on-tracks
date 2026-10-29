package pt.ipp.isep.dei.mapper.dto;

/**
 * Data Transfer Object for representing an industry's cargo state.
 * <p>
 * Contains index, name, input cargo type and quantity, and output cargo type and quantity.
 * </p>
 *
 * @author Team
 * @version 1.0
 */
public class IndustryDTO1 {

    private int index;
    private String name;
    private String inputCargoType;
    private double inputCargoQuantity;
    private String outputCargoType;
    private double outputCargoQuantity;

    /**
     * Constructs an IndustryDTO1.
     *
     * @param index                 zero-based identifier for the industry
     * @param name                  display name of the industry
     * @param inputCargoType        type of cargo consumed ("-" if none)
     * @param inputCargoQuantity    quantity of input cargo (0 if none)
     * @param outputCargoType       type of cargo produced
     * @param outputCargoQuantity   quantity of output cargo produced
     */
    public IndustryDTO1(int index,
                        String name,
                        String inputCargoType,
                        double inputCargoQuantity,
                        String outputCargoType,
                        double outputCargoQuantity) {
        this.index = index;
        this.name = name;
        this.inputCargoType = inputCargoType;
        this.inputCargoQuantity = inputCargoQuantity;
        this.outputCargoType = outputCargoType;
        this.outputCargoQuantity = outputCargoQuantity;
    }

    /**
     * @return zero-based index of this industry
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return display name of the industry
     */
    public String getName() {
        return name;
    }

    /**
     * @return type of cargo consumed
     */
    public String getInputCargoType() {
        return inputCargoType;
    }

    /**
     * @return quantity of input cargo consumed
     */
    public double getInputCargoQuantity() {
        return inputCargoQuantity;
    }

    /**
     * @return type of cargo produced
     */
    public String getOutputCargoType() {
        return outputCargoType;
    }

    /**
     * @return quantity of output cargo produced
     */
    public double getOutputCargoQuantity() {
        return outputCargoQuantity;
    }

    /**
     * Returns a string representation including all fields.
     *
     * @return detailed string of this DTO
     */
    @Override
    public String toString() {
        return String.format(
                "IndustryDTO[index=%d, name=%s, inputCargoType=%s, inputCargoQuantity=%.2f, outputCargoType=%s, outputCargoQuantity=%.2f]",
                index,
                name,
                inputCargoType,
                inputCargoQuantity,
                outputCargoType,
                outputCargoQuantity
        );
    }
}