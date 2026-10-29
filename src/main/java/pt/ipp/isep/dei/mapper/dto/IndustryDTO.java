package pt.ipp.isep.dei.mapper.dto;

/**
 * Data Transfer Object for presenting industry templates in the UI.
 * <p>
 * Contains index, name, input cargo (type & amount), and output cargo (type & amount).
 * </p>
 *
 * @author Team
 * @version 1.0
 */
public final class IndustryDTO {

    private final int index;
    private final String name;
    private final String consumesType;
    private final double consumesQty;
    private final String producesType;
    private final double producesQty;

    /**
     * Constructs an IndustryDTO.
     *
     * @param index         zero-based identifier for this industry template
     * @param name          display name of the industry
     * @param consumesType  input cargo type as a string ("–" if none)
     * @param consumesQty   input cargo quantity (0 if none)
     * @param producesType  output cargo type as a string
     * @param producesQty   output cargo quantity
     */
    public IndustryDTO(int index,
                       String name,
                       String consumesType,
                       double consumesQty,
                       String producesType,
                       double producesQty) {
        this.index = index;
        this.name = name;
        this.consumesType = consumesType;
        this.consumesQty = consumesQty;
        this.producesType = producesType;
        this.producesQty = producesQty;
    }

    /**
     * @return zero-based index of this industry template
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return name of the industry
     */
    public String getName() {
        return name;
    }

    /**
     * @return input cargo type ("–" if none)
     */
    public String getConsumesType() {
        return consumesType;
    }

    /**
     * @return input cargo quantity (0 if none)
     */
    public double getConsumesQty() {
        return consumesQty;
    }

    /**
     * @return output cargo type
     */
    public String getProducesType() {
        return producesType;
    }

    /**
     * @return output cargo quantity
     */
    public double getProducesQty() {
        return producesQty;
    }

    /**
     * Returns the display name of the industry.
     *
     * @return industry name
     */
    @Override
    public String toString() {
        return name;
    }
}
