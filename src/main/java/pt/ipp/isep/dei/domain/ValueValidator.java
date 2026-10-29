package pt.ipp.isep.dei.domain;

import pt.ipp.isep.dei.ui.console.menu.MainMenuUI;

import java.io.Serializable;

/**
 * Utility class for validating numeric values related to map dimensions.
 * <p>
 * Provides methods to check if a value is positive and
 * to validate width and height dimensions for the map.
 * </p>
 */
public class ValueValidator implements Serializable {

    /**
     * Checks if the given integer value is strictly positive.
     *
     * @param value the integer to check
     * @return {@code true} if {@code value} is greater than zero; {@code false} otherwise
     */
    public boolean isPositive(int value) {
        return value > 0;
    }

    /**
     * Validates that both width and height are positive values.
     *
     * @param width  the width dimension to validate
     * @param height the height dimension to validate
     * @return {@code true} if both {@code width} and {@code height} are positive; {@code false} otherwise
     */
    public boolean validateWidthLength(int width, int height) {
        return isPositive(width) && isPositive(height);
    }
}