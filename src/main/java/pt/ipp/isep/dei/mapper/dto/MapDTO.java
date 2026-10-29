package pt.ipp.isep.dei.mapper.dto;

/**
 * Data Transfer Object (DTO) representing a map's basic properties.
 * <p>
 * Used to transfer map details (name, width, height) without domain behavior.
 * </p>
 *
 * @author Team
 * @version 1.0
 */
public final class MapDTO {

    private final String name;
    private final int width;
    private final int height;

    /**
     * Constructs a MapDTO with the specified name and dimensions.
     *
     * @param name   name of the map
     * @param width  width of the map (in cells)
     * @param height height of the map (in cells)
     */
    public MapDTO(String name, int width, int height) {
        this.name = name;
        this.width = width;
        this.height = height;
    }

    /**
     * Returns the map's name.
     *
     * @return name of the map
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the map's width.
     *
     * @return width of the map
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the map's height.
     *
     * @return height of the map
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns a formatted string representation of the map.
     *
     * @return string in the format: Map "name" (widthxheight)
     */
    @Override
    public String toString() {
        return String.format("Map \"%s\" (%dx%d)", name, width, height);
    }
}