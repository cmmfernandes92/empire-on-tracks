package pt.ipp.isep.dei.ui.console;

import pt.ipp.isep.dei.controller.CreateScenarioController;
import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.mapper.dto.IndustryDTO1;

import java.util.List;
import java.util.Scanner;

public class CreateScenarioUI implements Runnable {
    private final CreateScenarioController controller;
    private final Scanner scanner;

    public CreateScenarioUI(Map map) {
        this.controller = new CreateScenarioController(map);
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void run() {
        askForScenarioDetails();
    }

    public void askForScenarioDetails() {
        System.out.println("Please provide scenario name, start date, and end date.");
        System.out.print("Name: ");
        String name = scanner.nextLine();

        int startYear = 0;
        int endYear = 0;
        boolean validDates = false;
        while (!validDates) {
            try {
                System.out.print("Start Date (>=1804): ");
                startYear = Integer.parseInt(scanner.nextLine());

                System.out.print("End Date: ");
                endYear = Integer.parseInt(scanner.nextLine());

                if (controller.validateYears(startYear, endYear)) {
                    validDates = true;
                } else {
                    System.out.println("Invalid date range. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter valid years.");
            }
        }

        showOptionsAndSelect(name, startYear, endYear);
    }

    public void showOptionsAndSelect(String name, int startYear, int endYear) {
        System.out.println("Select an option:");
        System.out.println("1. Locomotive Types");
        System.out.println("2. Production Rates");
        System.out.println("3. Mixed Industry Behaviors");
        System.out.println("5. Finish Scenario Creation");
        System.out.println("6. Exit to Main Menu");
        System.out.print("Choice: ");
        String option = scanner.nextLine();

        switch (option) {
            case "1": selectLocomotiveTypes(name, startYear, endYear); break;
            case "2": selectProductionRates(name, startYear, endYear); break;
            case "3": selectMixedBehavior(name, startYear, endYear); break;
            case "5": finalizeScenario(name, startYear, endYear); break;
            case "6": exitToMainMenu(); break;
            default:
                System.out.println("Invalid option, please try again.");
                showOptionsAndSelect(name, startYear, endYear);
                break;
        }
    }

    public void selectLocomotiveTypes(String name, int startYear, int endYear) {
        List<String> types = controller.getLocomotiveTypeNames();

        for (String type : types) {
            System.out.print("Do you want to add the " + type + " locomotives? (yes/no): ");
            String answer = scanner.nextLine().trim().toLowerCase();
            if (answer.equals("yes") || answer.equals("sim")) {
                controller.addLocomotiveType(type);
                System.out.println(type + " locomotives added.");
            } else {
                System.out.println(type + " locomotives not added.");
            }
        }

        showOptionsAndSelect(name, startYear, endYear);
    }

    public void selectProductionRates(String name, int startYear, int endYear) {
        List<IndustryDTO1> industries = controller.getIndustryDTOs();
        System.out.println("Available Producing Industries for setting production rates:");

        for (int i = 0; i < industries.size(); i++) {
            System.out.printf("%d) %s (Consumes %s: %.1f, Produces %s: %.1f)%n",
                    i + 1,
                    industries.get(i).getName(),
                    industries.get(i).getInputCargoType(),
                    industries.get(i).getInputCargoQuantity(),
                    industries.get(i).getOutputCargoType(),
                    industries.get(i).getOutputCargoQuantity());
        }

        while (true) {
            System.out.print("Select Industry by index (or 'done'): ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("done")) break;

            try {
                int selectedIndex = Integer.parseInt(input) - 1;
                if (selectedIndex >= 0 && selectedIndex < industries.size()) {
                    System.out.print("Enter production rate for " + industries.get(selectedIndex).getName() + ": ");
                    double rate = Double.parseDouble(scanner.nextLine());
                    controller.addProductionRateChange(selectedIndex, rate);
                    System.out.println("Production rate set.");
                } else {
                    System.out.println("Invalid index.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input.");
            }
        }

        showOptionsAndSelect(name, startYear, endYear);
    }

    public void selectMixedBehavior(String name, int startYear, int endYear) {
        List<IndustryDTO1> industries = controller.getMixedIndustryDTOs();

        for (int i = 0; i < industries.size(); i++) {
            System.out.println((i + 1) + ") " + industries.get(i).getName());
        }

        while (true) {
            System.out.print("Select mixed industry by index (or 'done' to finish): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("done")) break;

            try {
                int index = Integer.parseInt(input) - 1;
                if (index < 0 || index >= industries.size()) {
                    System.out.println("Invalid index.");
                    continue;
                }

                IndustryDTO1 dto = industries.get(index);

                System.out.println("Choose behavior for " + dto.getName() + ":");
                System.out.println("1 - Import (produces selected cargo)");
                System.out.println("2 - Export (removes cargo, adds budget)");
                System.out.println("3 - Transform (requires input, produces output)");

                System.out.print("Type your option (1–3): ");
                String behaviorChoice = scanner.nextLine();

                MixedBehavior behavior;
                switch (behaviorChoice) {
                    case "1": behavior = MixedBehavior.IMPORT; break;
                    case "2": behavior = MixedBehavior.EXPORT; break;
                    case "3": behavior = MixedBehavior.TRANSFORM; break;
                    default:
                        System.out.println("Invalid option.");
                        continue;
                }

                CargoType inputCargo = null;
                CargoType outputCargo = null;
                CargoType[] types = CargoType.values();

                if (behavior == MixedBehavior.TRANSFORM) {
                    System.out.println("Select input cargo type:");
                    for (int i = 0; i < types.length; i++) {
                        System.out.printf("%d - %s%n", i + 1, types[i]);
                    }
                    inputCargo = types[Integer.parseInt(scanner.nextLine()) - 1];

                    System.out.println("Select output cargo type:");
                    for (int i = 0; i < types.length; i++) {
                        System.out.printf("%d - %s%n", i + 1, types[i]);
                    }
                    outputCargo = types[Integer.parseInt(scanner.nextLine()) - 1];

                } else if (behavior == MixedBehavior.IMPORT) {
                    System.out.println("Select output cargo type:");
                    for (int i = 0; i < types.length; i++) {
                        System.out.printf("%d - %s%n", i + 1, types[i]);
                    }
                    outputCargo = types[Integer.parseInt(scanner.nextLine()) - 1];
                } else {
                    System.out.println("Select input cargo type:");
                    for (int i = 0; i < types.length; i++) {
                        System.out.printf("%d - %s%n", i + 1, types[i]);
                    }
                    inputCargo = types[Integer.parseInt(scanner.nextLine()) - 1];
                }

                controller.addMixedIndustryBehavior(dto.getIndex(), behavior, inputCargo, outputCargo);
                System.out.printf("Behavior %s set for '%s' (in: %s, out: %s)%n",
                        behavior, dto.getName(), inputCargo, outputCargo);

            } catch (Exception e) {
                System.out.println("Invalid input.");
            }
        }

        showOptionsAndSelect(name, startYear, endYear);
    }

    public void finalizeScenario(String name, int startYear, int endYear) {
        System.out.println("Confirming the following data: " + name + ", " + startYear + ", " + endYear);
        System.out.print("Confirm (yes/no): ");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("yes")) {
            controller.setScenarioInfo(name, startYear, endYear);
            controller.createScenario();
            System.out.println("Scenario created successfully! It has been added to the map.");
            controller.showScenarioDetails();
        } else {
            System.out.println("Scenario creation cancelled.");
        }
    }

    public void exitToMainMenu() {
        System.out.println("Exiting to Main Menu...");
    }
}
