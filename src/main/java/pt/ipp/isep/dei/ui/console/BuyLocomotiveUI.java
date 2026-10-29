package pt.ipp.isep.dei.ui.console;

import java.util.List;
import java.util.Scanner;
import pt.ipp.isep.dei.controller.BuyLocomotiveController;
import pt.ipp.isep.dei.domain.Locomotive;
import pt.ipp.isep.dei.domain.Player;
import pt.ipp.isep.dei.domain.Train;
import pt.ipp.isep.dei.mapper.dto.LocomotiveDTO;

/**
 * Console-based UI for purchasing a locomotive and assigning it to a new train.
 * <p>
 * Guides the player through viewing available locomotives, making a purchase,
 * and naming the new train.
 */
public class BuyLocomotiveUI implements Runnable {
    private BuyLocomotiveController controller;
    private Scanner scanner = new Scanner(System.in);

    /**
     * Constructs the UI with the specified controller.
     */
    public BuyLocomotiveUI( BuyLocomotiveController controller) {
        this.controller = controller;
    }

    /**
     * Starts the interactive flow for purchasing a locomotive and assigning it to a new train.
     * <p>
     * Steps:
     * <ol>
     *   <li>Displays all available locomotives.</li>
     *   <li>Prompts the player to select a locomotive to buy.</li>
     *   <li>Asks the player to provide a name for the new train.</li>
     *   <li>Attempts the purchase and assignment, displaying feedback based on the result.</li>
     * </ol>
     * </p>
     */
    public void start() {
        System.out.println("\n=== Buy Locomotive ===");
        List<LocomotiveDTO> locomotives = controller.getAvailableLocomotivesDTO();

        if (locomotives.isEmpty()) {
            System.out.println("No locomotives available.");
            return;
        }

        System.out.println("Available locomotives:");
        for (int i = 0; i < locomotives.size(); i++) {
            LocomotiveDTO loco = locomotives.get(i);
            System.out.printf("[%d] %s - $%.2f (%s)\n", i + 1, loco.getId(), loco.getCost(), loco.getType());
        }

        System.out.print("Choose one to buy (by number): ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        if (choice < 1 || choice > locomotives.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        LocomotiveDTO selected = locomotives.get(choice - 1);

        // Prompt for train name
        System.out.print("\uD83D\uDE82 Enter a name for your new train: ");
        String trainName = scanner.nextLine().trim();

        int result = controller.buyLocomotiveAsTrain(selected.getId(), trainName);

        switch (result) {
            case 0: //Successfully created train
                Train newTrain = controller.getPlayer().getTrainByName(trainName);

                System.out.println(" \uD83D\uDE82 Successfully purchased locomotive and created train: " + trainName +
                        " with plate number " + newTrain.getPlateNumber() + ".");
                break;
            case 1:
                System.out.println("Insufficient budget to purchase this locomotive.");
                break;
            case 2:
                System.out.println("You already have a train with that name. Please choose another name.");
                break;
            case 3:
                System.out.println("Locomotive not found (unexpected error).");
                break;
        }
    }

    /**
     * Entry‐point for Runnable dispatch.
     */

    @Override
    public void run() {
        start();
    }
}
