package pt.ipp.isep.dei.domain;

import java.io.Serializable;

public class Size implements Serializable {

    private final int width;
    private final int height;

    public Size(int width, int height) {
        if (!isPositive(width) || !isPositive(height)) {
            throw new IllegalArgumentException("Width and height must be positive");
        }
        this.width = width;
        this.height = height;
    }

    private boolean isPositive(int value) {
        return value > 0;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return width + "x" + height;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Size)) return false;
        Size other = (Size) obj;
        return this.width == other.width && this.height == other.height;
    }

    @Override
    public int hashCode() {
        return 31 * width + height;
    }
}
