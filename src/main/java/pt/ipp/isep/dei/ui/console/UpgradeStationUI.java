package pt.ipp.isep.dei.ui.console;

import pt.ipp.isep.dei.controller.UpgradeStationController;
import pt.ipp.isep.dei.domain.Player;
import pt.ipp.isep.dei.domain.Station;

import java.util.*;

public class UpgradeStationUI implements Runnable {

    private final Station station;
    private final Player player;

    public UpgradeStationUI(Station station, Player player) {
        this.station = station;
        this.player = player;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        UpgradeStationController controller = new UpgradeStationController(station, player);

        System.out.println("\n--- Upgrade Station ---");

        // Define all available upgrades and their prices
        Map<Integer, String> upgradeOptions = new LinkedHashMap<>();
        Map<String, Double> upgradePrices = new HashMap<>();

        upgradeOptions.put(1, "TELEGRAPH");
        upgradePrices.put("TELEGRAPH", 200.0);

        upgradeOptions.put(2, "TELEPHONE");
        upgradePrices.put("TELEPHONE", 400.0);

        upgradeOptions.put(3, "SMALL_CAFE");
        upgradePrices.put("SMALL_CAFE", 150.0);

        upgradeOptions.put(4, "LARGE_CAFE");
        upgradePrices.put("LARGE_CAFE", 300.0);

        upgradeOptions.put(5, "CUSTOMS");
        upgradePrices.put("CUSTOMS", 250.0);

        upgradeOptions.put(6, "POST_OFFICE");
        upgradePrices.put("POST_OFFICE", 220.0);

        upgradeOptions.put(7, "SMALL_HOTEL");
        upgradePrices.put("SMALL_HOTEL", 350.0);

        upgradeOptions.put(8, "LARGE_HOTEL");
        upgradePrices.put("LARGE_HOTEL", 500.0);

        upgradeOptions.put(9, "SILO");
        upgradePrices.put("SILO", 180.0);

        upgradeOptions.put(10, "LIQUID_STORAGE");
        upgradePrices.put("LIQUID_STORAGE", 270.0);

        upgradeOptions.put(11, "Return to station list.");
        while (true) {
            System.out.println("\nAvailable upgrades:");
            for (Map.Entry<Integer, String> entry : upgradeOptions.entrySet()) {
                String upgradeName = entry.getValue();
                double price = upgradePrices.getOrDefault(upgradeName, 0.0);
                System.out.printf("%d - %s (%.2f coins)%n", entry.getKey(), upgradeName, price);
            }


            System.out.print("\nSelect the number of the upgrade to install: ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid selection. Try again.");
                continue;
            }

            if (choice == 11) {
                System.out.println("Upgrade cancelled. Returning to station selection...");
                return;
            }

            String type = upgradeOptions.get(choice);
            if (type == null) {
                System.out.println("Invalid choice. Try again.");
                continue;
            }

            double cost = upgradePrices.getOrDefault(type, 0.0);
            System.out.printf("Upgrade cost: %.2f coins. Do you want to complete the operation? (yes/no): ", cost);
            String confirm = scanner.nextLine().trim().toLowerCase();

            if (!confirm.equals("yes")) {
                System.out.println("Operation cancelled. Returning to upgrade list...");
                continue;
            }

            if (!controller.isUpgradeAvailable(type)) {
                System.out.println("This upgrade is not available at the current date.");
                return;
            }

            if (!controller.isCompatible(type)) {
                System.out.println("This upgrade is not compatible with the current station.");
                return;
            }

            if (controller.hasBuilding(type)) {
                System.out.println("This building is already installed.");
                return;
            }

            if (!controller.hasSufficientBudget(cost)) {
                System.out.println("You do not have enough budget to apply this upgrade.");
                return;
            }

            boolean success = controller.applyUpgrade(type, cost);
            if (success) {
                System.out.println("Upgrade applied successfully!");
            } else {
                System.out.println("Failed to apply the upgrade.");
            }

            return;
        }
    }
}
