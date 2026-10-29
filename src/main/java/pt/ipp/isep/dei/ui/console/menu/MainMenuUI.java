package pt.ipp.isep.dei.ui.console.menu;

import pt.ipp.isep.dei.ui.console.DevTeamUI;
import pt.ipp.isep.dei.ui.console.authorization.AuthenticationUI;
import pt.ipp.isep.dei.ui.console.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Console UI for the application's main menu.
 * <p>
 * Presents options to log in or to view the development team,
 * and dispatches to the chosen sub-UI until the user exits.
 */
public class MainMenuUI implements Runnable {

    /**
     * Constructs a new MainMenuUI.
     */
    public MainMenuUI() {
        // No-op constructor
    }

    /**
     * Displays the main menu and processes user selections.
     * <p>
     * Continues looping until the user chooses to exit (index -1).
     */
    @Override
    public void run() {
        // Welcome message
        System.out.println("\n=== Welcome to Empire on Tracks ===");

        final List<MenuItem> options = new ArrayList<>();
        options.add(new MenuItem("Do Login", new AuthenticationUI()));
        options.add(new MenuItem("Know the Development Team", new DevTeamUI()));
        // Role‐based submenus
        options.add(new MenuItem("Editor Role", new EditorMenuUI()));
        options.add(new MenuItem("Player Role", new PlayerMenuUI()));

        int selection;
        do {
            selection = Utils.showAndSelectIndex(
                    options,
                    "\n\n--- MAIN MENU --------------------------"
            );
            if (selection >= 0 && selection < options.size()) {
                options.get(selection).run();
            }
        } while (selection != -1);
    }
}
