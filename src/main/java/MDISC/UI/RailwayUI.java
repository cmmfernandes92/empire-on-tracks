package MDISC.UI;

import MDISC.Controller.AppController;

import java.util.Scanner;

/**
 * Main User Interface (UI) for the railway application.
 * <p>
 * Responsibilities:
 * <ol>
 *   <li>Prompt for the path to the stations CSV file.</li>
 *   <li>Prompt for the path to the lines CSV file.</li>
 *   <li>Prompt for the train type (Electric, Diesel, or Steam).</li>
 *   <li>Instantiate {@link AppController} and display the interactive menu.</li>
 * </ol>
 */
public class RailwayUI {

    /**
     * Entry point of the application.
     * <ol>
     *   <li>Reads the file path for station definitions from the user.</li>
     *   <li>Reads the file path for line definitions from the user.</li>
     *   <li>Prompts the user to choose a train type:
     *       <ul>
     *         <li>1 – Electric (only electrified lines will be used).</li>
     *         <li>2 – Diesel (all lines can be used).</li>
     *         <li>3 – Steam (all lines can be used).</li>
     *       </ul>
     *   </li>
     *   <li>Creates an {@link AppController} with the specified parameters and invokes its interactive menu.</li>
     *   <li>Closes the {@link Scanner} before exiting.</li>
     * </ol>
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the path to the stations CSV file: ");
        String stationsPath = sc.nextLine().trim();

        System.out.print("Enter the path to the lines CSV file: ");
        String linesPath = sc.nextLine().trim();

        System.out.println("\nSelect the train type:");
        System.out.println("1) Electric");
        System.out.println("2) Diesel");
        System.out.println("3) Steam");
        System.out.print("Choice: ");
        int choice = sc.nextInt();
        sc.nextLine(); // consume newline

        boolean onlyElectrified = (choice == 1);

        AppController controller = new AppController(stationsPath, linesPath, onlyElectrified);
        controller.startInteractive();

        sc.close();
    }
}
