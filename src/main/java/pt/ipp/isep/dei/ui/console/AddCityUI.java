package pt.ipp.isep.dei.ui.console;

import pt.ipp.isep.dei.controller.AddCityController;
import pt.ipp.isep.dei.domain.City;
import pt.ipp.isep.dei.domain.HouseBlock;
import pt.ipp.isep.dei.domain.Map;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * User Interface class for the "Add City" feature (US03).
 * <p>
 * Allows an Editor to:
 * <ul>
 *   <li>Add a city to the map with a name, position (X,Y), and house blocks</li>
 *   <li>Validate city names (AC01)</li>
 *   <li>Assign house blocks manually or automatically (AC02)</li>
 *   <li>Remove cities from the map</li>
 * </ul>
 *
 * @author
 */
public class AddCityUI implements Runnable {

    private AddCityController controller;
    private Map map;

    /**
     * Constructs the UI with the provided map.
     *
     * @param map the map where cities will be added or removed
     */
    public AddCityUI(Map map) {
        this.map = map;
        this.controller = new AddCityController(map);
    }

    /**
     * Starts the interactive menu loop for city management.
     */
    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== City Management Menu ===");
            System.out.println("1. Add new city");
            System.out.println("2. Remove city");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    addCity(scanner);
                    break;
                case 2:
                    removeCity(scanner);
                    break;
                case 3:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    /**
     * Guides the user through the process of creating and adding a new city.
     * <p>
     * Enforces AC01 and AC02:
     * - Validates that the city name contains only letters and spaces.
     * - Allows the choice between manual and automatic house block assignment.
     *
     * @param scanner a {@code Scanner} instance for reading user input
     */
    private void addCity(Scanner scanner) {
        String name;

        // Validate city name (AC01)
        while (true) {
            System.out.print("Enter city name: ");
            name = scanner.nextLine();

            City city = new City(name, 0, 0, 0, "");

            try {
                city.validateName();
                if (map.isCityNameDuplicate(city.getName())) {
                    System.out.println("City '" + city.getName() + "' already exists.");
                } else {
                    break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.print("Enter city X position: ");
        int posX = scanner.nextInt();

        System.out.print("Enter city Y position: ");
        int posY = scanner.nextInt();

        if (!controller.getMap().isEmpty(posX, posY)) {
            System.out.println("Cannot place the city at these coordinates. There is already something here.");
            return;
        }

        int houseBlockCount;
        while (true) {
            System.out.print("Enter number of house blocks: ");
            houseBlockCount = scanner.nextInt();
            scanner.nextLine();

            City city = new City(name, posX, posY, houseBlockCount, "");
            try {
                city.validateHouseBlocks(); // Validate count (>=1)
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        // AC02: Choose between manual or automatic assignment
        System.out.println("Choose house block assignment method:");
        System.out.println("1. Manual");
        System.out.println("2. Automatic");
        System.out.print("Your choice: ");
        int methodOption = scanner.nextInt();
        scanner.nextLine();
        String method = (methodOption == 1) ? "manual" : "automatic";

        if (method.equals("manual")) {
            List<Integer> posXArray = new ArrayList<>();
            List<Integer> posYArray = new ArrayList<>();

            for (int i = 0; i < houseBlockCount; i++) {
                System.out.println("Enter coordinates for House Block #" + (i + 1));
                System.out.print("  X: ");
                posXArray.add(scanner.nextInt());
                System.out.print("  Y: ");
                posYArray.add(scanner.nextInt());
            }

            City city = controller.createCity(name, posX, posY, houseBlockCount, method, posXArray, posYArray);
            displaySuccess(city);
        } else {
            City city = controller.createCity(name, posX, posY, houseBlockCount, method, null, null);
            displaySuccess(city);
        }
    }

    /**
     * Allows the user to remove a city from the map.
     *
     * @param scanner a {@code Scanner} instance for reading user input
     */
    private void removeCity(Scanner scanner) {
        List<City> cities = controller.getMap().getCities();

        if (cities.isEmpty()) {
            System.out.println("No cities available on the map.");
            return;
        }

        System.out.println("Cities currently on the map:");
        for (int i = 0; i < cities.size(); i++) {
            System.out.printf("%d - %s%n", i + 1, cities.get(i).getName());
        }

        System.out.print("Enter the number of the city to remove: ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice < 1 || choice > cities.size()) {
                System.out.println("Invalid selection. Please select a valid number.");
                return;
            }

            City selectedCity = cities.get(choice - 1);
            boolean removed = controller.removeCity(selectedCity.getName());

            if (removed) {
                System.out.println("City '" + selectedCity.getName() + "' successfully removed.");
            } else {
                System.out.println("Could not remove the city. It may no longer exist.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a numeric value.");
        }
    }

    /**
     * Displays a success message and the coordinates of house blocks in the city.
     *
     * @param city the city that was successfully created
     */
    private void displaySuccess(City city) {
        if (city != null) {
            System.out.println("\nCity created successfully: " + city.getName());
            System.out.println("House Blocks:");

            List<HouseBlock> houseBlocks = city.getHouseBlocks();
            for (int i = 0; i < houseBlocks.size(); i++) {
                int x = houseBlocks.get(i).getLocation().getX();
                int y = houseBlocks.get(i).getLocation().getY();
                System.out.printf("  #%d: (X: %d, Y: %d)%n", i + 1, x, y);
            }
        } else {
            System.out.println("Failed to create city. Name might be invalid or already exists.");
        }
    }

    /**
     * Runs the UI thread.
     * This method implements {@link Runnable}.
     */
    @Override
    public void run() {
        start();
    }
}
