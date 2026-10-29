package pt.ipp.isep.dei.ui.console;

import pt.ipp.isep.dei.controller.AddIndustryController;
import pt.ipp.isep.dei.mapper.dto.IndustryDTO;
import pt.ipp.isep.dei.repository.Repositories;
import pt.ipp.isep.dei.domain.Map;

import java.util.List;
import java.util.Scanner;

/**
 * Simple console UI that guides the user through the
 * “Add Industry” workflow.
 *
 * <p>Sequence:</p>
 * <ol>
 *   <li>Display the list of available industry templates;</li>
 *   <li>Let the user pick a template index;</li>
 *   <li>Ask for map coordinates;</li>
 *   <li>Validate and request the controller to place the industry.</li>
 * </ol>
 *
 * @author  Team
 * @version 1.0
 */
public class AddIndustryUI implements Runnable {

    private final AddIndustryController ctrl;
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Builds the UI component.
     *
     * @param repos shared repositories aggregate
     * @param map   simulation map used by this session
     */
    public AddIndustryUI(Repositories repos, Map map) {
        this.ctrl = new AddIndustryController(repos, map);
    }

    /* Runnable entry-point -------------------------------------------------- */
    @Override public void run() { start(); }

    /** Main interaction loop. */
    private void start() {
        System.out.println("=== Add Industry ===\n");

        // 1) Display list with type and quantity
        List<IndustryDTO> templates = ctrl.getIndustryTypeList();
        for (IndustryDTO d : templates) {
            String consumes = d.getConsumesType().equals("–")
                    ? "–"
                    : d.getConsumesType() + ":" + (int) d.getConsumesQty();
            String produces = d.getProducesType() + ":" + (int) d.getProducesQty();

            System.out.printf(
                    "%d \u2013 %-15s | %-18s | %s%n",
                    d.getIndex(),
                    d.getName(),
                    consumes,
                    produces
            );
        }

        // 2) Prompt for index
        System.out.print("Choose template index: ");
        int idx = scanner.nextInt();
        if (idx < 0 || idx >= templates.size()) {
            System.out.println("Invalid index.");
            return;
        }
        IndustryDTO choice = templates.get(idx);

        // 3) Coordinates
        System.out.print("X coordinate: ");
        int x = scanner.nextInt();
        System.out.print("Y coordinate: ");
        int y = scanner.nextInt();

        if (!ctrl.isValidPosition(x, y)) {
            System.out.println("Invalid or occupied position.");
            return;
        }

        // 4) Create the industry
        if (ctrl.addIndustry(choice, x, y))
            System.out.println("Industry added successfully!");
        else
            System.out.println("Error adding industry.");
    }
}
