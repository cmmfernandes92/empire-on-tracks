package pt.ipp.isep.dei.ui.console;

import pt.ipp.isep.dei.controller.CreateMapController;
import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.mapper.dto.SizeDTO;

import java.util.List;
import java.util.Scanner;

public class CreateMapUI implements Runnable {

    private final CreateMapController ctrl;
    private final Scanner scanner = new Scanner(System.in);

    public CreateMapUI(CreateMapController ctrl) {
        this.ctrl = ctrl;
    }

    @Override
    public void run() {
        create();
    }

    private void create() {
        System.out.print("Enter map name: ");
        String name = scanner.nextLine();

        System.out.print("Use predefined size? (y/n): ");
        String predefined = scanner.nextLine();

        int width, height;

        if (predefined.equalsIgnoreCase("y")) {
            List<SizeDTO> sizes = ctrl.getAvailableSizes();
            if (sizes.isEmpty()) {
                System.out.println("No predefined sizes available.");
                return;
            }

            System.out.println("Available sizes:");
            for (int i = 0; i < sizes.size(); i++) {
                SizeDTO dto = sizes.get(i);
                System.out.printf("  %d) %dx%d%n", i + 1, dto.getWidth(), dto.getHeight());
            }

            System.out.print("Select a size by number: ");
            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
            } catch (Exception e) {
                System.out.println("Invalid input. Must be an integer.");
                scanner.nextLine();
                return;
            }

            if (choice < 1 || choice > sizes.size()) {
                System.out.println("Invalid choice.");
                return;
            }

            SizeDTO selected = sizes.get(choice - 1);
            width = selected.getWidth();
            height = selected.getHeight();

        } else {
            try {
                System.out.print("Enter map width: ");
                width = scanner.nextInt();
                System.out.print("Enter map height: ");
                height = scanner.nextInt();
                scanner.nextLine(); // consume newline
            } catch (Exception e) {
                System.out.println("Invalid input. Dimensions must be integers.");
                scanner.nextLine();
                return;
            }
        }

        if (!ctrl.validateDimensions(width, height)) {
            System.out.println("Error: Dimensions must be positive.");
            return;
        }
        if (!ctrl.validateMapName(name)) {
            System.out.println("Error: Name is empty or duplicate.");
            return;
        }

        System.out.printf("Confirm creation of \"%s\" (%dx%d)? (y/n): ",
                name, width, height);
        String confirm = scanner.nextLine();
        if (!confirm.equalsIgnoreCase("y")) {
            System.out.println("Map creation cancelled.");
            return;
        }

        Map draft = ctrl.createMapDraft(name, width, height);
        ctrl.saveMap(draft);
        System.out.printf("Map created: \"%s\" (%dx%d)%n",
                draft.getName(), draft.getWidth(), draft.getHeight());
    }
}
