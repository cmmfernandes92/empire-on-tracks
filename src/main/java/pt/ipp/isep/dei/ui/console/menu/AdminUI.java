package pt.ipp.isep.dei.ui.console.menu;

import pt.ipp.isep.dei.ui.console.ShowTextUI;
import pt.ipp.isep.dei.ui.console.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Console UI for administrator actions.
 * <p>
 * Presents admin-specific menu options and dispatches to chosen UI.
 */
public class AdminUI implements Runnable {

    /**
     * Constructs a new AdminUI.
     */
    public AdminUI() {
        // No-op constructor
    }

    /**
     * Displays the admin menu and handles user selections.
     * <p>
     * Loops until the user exits (selecting -1).
     */
    @Override
    public void run() {
        final List<MenuItem> options = new ArrayList<>();
        options.add(new MenuItem("Option 2", new ShowTextUI("You have chosen Option 2.")));
        options.add(new MenuItem("Option 3", new ShowTextUI("You have chosen Option 3.")));
        options.add(new MenuItem("Option 4", new ShowTextUI("You have chosen Option 4.")));

        int selection;
        do {
            selection = Utils.showAndSelectIndex(
                    options,
                    "\n\n--- ADMIN MENU -------------------------"
            );
            if (selection >= 0 && selection < options.size()) {
                options.get(selection).run();
            }
        } while (selection != -1);
    }
}
