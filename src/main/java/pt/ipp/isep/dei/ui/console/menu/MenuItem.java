package pt.ipp.isep.dei.ui.console.menu;

import org.apache.commons.lang3.StringUtils;
import pt.ipp.isep.dei.ui.console.ListTrainsUI;

import java.util.Objects;

/**
 * Represents a menu entry pairing a text description with a UI action.
 * <p>
 * Validates that description is non-blank and the UI runnable is non-null.
 */
public class MenuItem {

    private final String description;
    private final Runnable ui;

    /**
     * Constructs a MenuItem with the given description and UI action.
     *
     * @param description the text shown in the menu; must not be null or blank
     * @param ui          the Runnable to execute when selected; must not be null
     * @throws IllegalArgumentException if description is null or blank, or ui is null
     */
    public MenuItem(String description, Runnable ui) {
        Objects.requireNonNull(description, "MenuItem description must not be null");
        if (description.isBlank()) {
            throw new IllegalArgumentException("MenuItem description cannot be blank.");
        }
        Objects.requireNonNull(ui, "MenuItem UI action must not be null");

        this.description = description;
        this.ui = ui;
    }


    /**
     * Executes the associated UI action.
     */
    public void run() {
        ui.run();
    }

    /**
     * Checks if this menu item matches the given description.
     *
     * @param description the description to compare
     * @return true if descriptions are equal
     */
    public boolean hasDescription(String description) {
        return this.description.equals(description);
    }

    /**
     * Returns the text used to display this menu item.
     *
     * @return the menu item's description
     */
    @Override
    public String toString() {
        return description;
    }
}
