package pt.ipp.isep.dei.ui.console;

import pt.ipp.isep.dei.controller.ListStationController;
import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.repository.Repositories;

import java.util.List;
import java.util.Scanner;

public class StationListUI implements Runnable {
    private final ListStationController controller;
    private final Scanner scanner = new Scanner(System.in);

    public StationListUI() {
        this.controller = new ListStationController(
                Repositories.getInstance().getMapRepository().getCurrentMap()
        );
    }

    public void start() {
        List<String> stationNames = controller.getAllStations();
        if (stationNames.isEmpty()) {
            System.out.println("No stations available.");
            return;
        }

        System.out.println("Available Stations:");
        for (int i = 0; i < stationNames.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, stationNames.get(i));
        }

        System.out.print("Select a station by number: ");
        int selectedIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // consume newline
        if (selectedIndex < 0 || selectedIndex >= stationNames.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        String selectedStationName = stationNames.get(selectedIndex);
        Station station = controller.getStationDetails(selectedStationName);

        if (station != null) {
            System.out.println("\nStation: " + station.getStationName());
            System.out.println("Buildings:");
            for (Building b : station.getBuildings()) {
                System.out.println("  - " + b);
            }
            System.out.println("Cargoes:");
            for (Cargo c : station.getCargoes()) {
                System.out.println("  - " + c);
            }
        } else {
            System.out.println("Station not found.");
        }
    }

    @Override
    public void run() {
        start();
    }
}
