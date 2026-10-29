package pt.ipp.isep.dei.mapper.dto;

public class SizeDTO {
    private final int id;
    private final int width;
    private final int height;

    public SizeDTO(int id, int width, int height) {
        this.id = id;
        this.width = width;
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return String.format("ID %d - %dx%d", id, width, height);
    }
}
