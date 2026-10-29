package pt.ipp.isep.dei.ui.console;

import pt.ipp.isep.dei.controller.ListTrainsController;
import pt.ipp.isep.dei.domain.PointOfRoute;
import pt.ipp.isep.dei.domain.Train;
import pt.ipp.isep.dei.domain.CargoType;

import java.util.*;

/**
 * Console-based user interface for listing the player's trains.
 * <p>
 * Displays trains grouped by locomotive type, sorted by name,
 * and includes their cargo assignments per route stop.
 */
public class ListTrainsUI implements Runnable {
    private ListTrainsController controller;

    public ListTrainsUI(ListTrainsController controller) {
        this.controller = controller;
    }

    public ListTrainsUI() {
    }

    /**
     * Requests the player's train list from the controller and displays it,
     * grouping by locomotive type and sorting by train name.
     */
    public void requestTrainList() {
        List<Train> trains = controller.requestTrainList();
        displayTrainList(trains);
    }

    /**
     * Displays the provided list of trains, grouping them by locomotive type
     * and showing the cargo assignments for each route stop.
     *
     * @param trains the list of trains to display
     */
    public void displayTrainList(List<Train> trains) {
        if (trains == null || trains.isEmpty()) {
            System.out.println("You have no trains yet. Buy a locomotive to create your first train!");
            return;
        }

        List<String> allTypes = List.of("Steam", "Diesel", "Electric");

        for (String type : allTypes) {
            System.out.println("\n== " + type + " Trains ==");

            List<Train> filtered = trains.stream()
                    .filter(t -> t.getLocomotiveType().equalsIgnoreCase(type))
                    .sorted(Comparator.comparing(Train::getName, String.CASE_INSENSITIVE_ORDER))
                    .toList();

            if (filtered.isEmpty()) {
                System.out.println("No owned trains of this type.");
                continue;
            }

            for (Train train : filtered) {
                System.out.printf("🚂 %s%n", train.getName());

                if (train.getAssignedRoute() == null || train.getAssignedRoute().getPointsOfRoute().isEmpty()) {
                    System.out.println("   No route assigned.");
                    continue;
                }

                // Collect distinct cargo types assigned across the route
                Set<String> assignedCargoTypes = new HashSet<>();
                for (PointOfRoute por : train.getAssignedRoute().getPointsOfRoute()) {
                    for (CargoType cargoType : por.getCargoCarriages().keySet()) {
                        assignedCargoTypes.add(cargoType.name());
                    }
                }

                if (assignedCargoTypes.isEmpty()) {
                    System.out.println("   Cargoes assigned: None");
                } else {
                    System.out.println("   Cargoes assigned: " + String.join(", ", assignedCargoTypes));
                }

                // Print route details
                for (PointOfRoute por : train.getAssignedRoute().getPointsOfRoute()) {
                    String station = por.getStation().getStationName();
                    Map<CargoType, Integer> cargoMap = por.getCargoCarriages();
                    String mode = por.getCargoMode().name(); // FULL, HALF, AVAILABLE

                    if (cargoMap.isEmpty()) {
                        System.out.printf("   ▫ Stop: %s – Cargoes: None – Mode: %s%n", station, mode);
                    } else {
                        StringBuilder cargoStr = new StringBuilder();
                        for (Map.Entry<CargoType, Integer> entry : cargoMap.entrySet()) {
                            cargoStr.append(entry.getKey().name())
                                    .append(" x").append(entry.getValue())
                                    .append(", ");
                        }
                        cargoStr.setLength(cargoStr.length() - 2);
                        System.out.printf("   ▫ Stop: %s – Cargoes: %s – Mode: %s%n", station, cargoStr, mode);
                    }
                }
            }
        }
    }


    /** Menu dispatch entry-point */
    @Override
    public void run() {
        requestTrainList();
    }
}
