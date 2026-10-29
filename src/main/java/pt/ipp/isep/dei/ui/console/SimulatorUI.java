package pt.ipp.isep.dei.ui.console;

import pt.ipp.isep.dei.controller.*;
import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.repository.Repositories;
import pt.ipp.isep.dei.ui.console.menu.MenuItem;
import pt.ipp.isep.dei.ui.console.utils.Utils;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

import static pt.ipp.isep.dei.ui.console.menu.PlayerMenuUI.SAVE_DIR;

public class SimulatorUI implements Runnable {
    private static final String SAVE_DIR =
            System.getProperty("user.dir")
                    + File.separator
                    + "src" + File.separator
                    + "main" + File.separator
                    + "saved-simulations";

    private final SimulatorController controller;
    private final Player player;
    private final AssignTrainToRouteController assignCtrl;

    public SimulatorUI(SimulatorController controller, Player player,
                       AssignTrainToRouteController assignCtrl) {
        this.controller = Objects.requireNonNull(controller);
        this.player = player;
        this.assignCtrl = Objects.requireNonNull(assignCtrl);
    }

    private void showStatus() {
        LocalDateTime now = controller.getCurrentDate();
        System.out.println("\n== SIMULATION STATUS ==");
        System.out.println("Current date: " + now);

        System.out.println("\n-- Industries on Map --");
        List<Industry> inds = controller.getIndustries();
        for (int i = 0; i < inds.size(); i++) {
            Industry ind = inds.get(i);
            Cargo stock = ind.getCargo();
            System.out.printf("[%d] %s | %s: %.2f | Prod: %.2f | Dem: %.2f | Loc: %s%n",
                    i,
                    ind.getIndustryName(),
                    stock.getType(), stock.getQuantity(),
                    ind.getProduction(),
                    ind.getDemand(),
                    ind.getLocation()
            );
        }
        System.out.println("\n-- Cities on Map --");
        List<City> cities = controller.getCities();
        for (int i = 0; i < cities.size(); i++) {
            City city = cities.get(i);
            System.out.printf("[%d] %s | Passengers: %.2f | Mail: %.2f | Location: %s | Blocks: %d%n",
                    i,
                    city.getIndustryName(),
                    city.getProducedCargoMap().get(CargoType.PASSENGERS).getQuantity(),
                    city.getProducedCargoMap().get(CargoType.MAIL).getQuantity(),
                    city.getLocation(),
                    city.getHouseBlockCount()
            );

            System.out.print("   Consumed: ");
            String consumed = city.getConsumedCargoMap().entrySet().stream()
                    .filter(e -> e.getValue().getQuantity() > 0)
                    .map(e -> e.getKey() + ": " + e.getValue().getQuantity())
                    .collect(Collectors.joining(", "));
            System.out.println(consumed.isEmpty() ? "none" : consumed);
        }



        System.out.println("\n-- Stations and Cargoes --");
        controller.getStations().forEach(s -> {
            String line = s.getCargoes().stream()
                    .filter(c -> c.getQuantity() > 0)
                    .map(c -> String.format("%s: %.2f", c.getType(), c.getQuantity()))
                    .collect(Collectors.joining(", "));
            System.out.printf("%s @ %s -> [%s]%n",
                    s.getStationName(),
                    s.getLocation(),
                    line.isEmpty() ? "vazio" : line);
        });

        System.out.println("\n-- Trains --");
        assignCtrl.getOwnedTrains().forEach(t ->
                System.out.printf("[%s] loco=%s route=%s load=%s%n",
                        t.getName(),
                        t.getLocomotiveType(),
                        t.getAssignedRoute() != null ? t.getAssignedRoute().toString() : "none",
                        t.getCurrentCargoes()
                )
        );

        System.out.println("\n-- Locomotive Templates --");
        controller.getLocomotiveTemplates().forEach(System.out::println);

        System.out.println();
    }

    private void listTrains() {
        List<Train> trains = assignCtrl.getOwnedTrains();
        if (trains.isEmpty()) {
            System.out.println("No trains owned.");
        } else {
            for (int i = 0; i < trains.size(); i++) {
                Train t = trains.get(i);
                System.out.printf("[%d] %s (route: %s)%n",
                        i,
                        t.getName(),
                        t.getAssignedRoute() != null ? t.getAssignedRoute().toString() : "none"
                );
            }
        }
    }

    private void showEditSubmenu() {
        List<MenuItem> editOptions = new ArrayList<>();

        editOptions.add(new MenuItem("UC5 – Build a Station", new BuildStationUI()));
        editOptions.add(new MenuItem("UC6 – Upgrade a Station", () -> {
            List<Station> stations = controller.getStations();
            if (stations.isEmpty()) {
                System.out.println("No stations available.");
                return;
            }

            int selectedIndex = Utils.showAndSelectIndex(
                    stations.stream()
                            .map(s -> String.format("%s @ %s", s.getStationName(), s.getLocation()))
                            .collect(Collectors.toList()),
                    "Select a station to upgrade:"
            );

            if (selectedIndex == -1) {
                System.out.println("Operation cancelled.");
                return;
            }

            Station selectedStation = stations.get(selectedIndex);
            new UpgradeStationUI(selectedStation, player).run();
        }));
        editOptions.add(new MenuItem("UC7 – List all Stations", new StationListUI()));
        editOptions.add(new MenuItem("UC8 – Build a Railway Line", new BuildLineUI(
                new BuildLineController(player, Repositories.getInstance().getMapRepository().getCurrentMap())
        )));
        editOptions.add(new MenuItem("UC9 – Buy a Locomotive", new BuyLocomotiveUI(
                new BuyLocomotiveController(player, controller.getCatalog())
        )));
        editOptions.add(new MenuItem("UC10 – Assign a Train to a Route", () -> {
            new AssignTrainToRouteUI(assignCtrl).run();
        }));
        editOptions.add(new MenuItem("UC11 – List all Trains", new ListTrainsUI(new ListTrainsController(player))));
        editOptions.add(new MenuItem("UC12 – Operate Simulation", this::showStatus));

        int choice;
        do {
            choice = Utils.showAndSelectIndex(editOptions, "== EDIT COMMANDS (UC5–UC12) ==");
            if (choice >= 0 && choice < editOptions.size()) {
                editOptions.get(choice).run();
            }
        } while (choice != -1);
    }

    @Override
    public void run() {
        List<MenuItem> options = new ArrayList<>();

        options.add(new MenuItem("Start automatic simulation", () -> {
            controller.startSimulation(5);
            System.out.println("Automatic simulation started.");
        }));

        options.add(new MenuItem("Pause automatic simulation", () -> {
            controller.pauseSimulation();
            System.out.println("Simulation paused.");
        }));

        options.add(new MenuItem("Advance one month (manual step)", () -> {
            List<String> evs = controller.stepWithEvents();
            System.out.println("Manual step executed (advanced one month).");
            if (evs.isEmpty()) {
                System.out.println("No events this step.");
            } else {
                System.out.println("Events:");
                evs.forEach(e -> System.out.println("  - " + e));
            }
        }));

        options.add(new MenuItem("Show simulation status", this::showStatus));
        options.add(new MenuItem("Edit simulation (UC5–UC12)", this::showEditSubmenu));
        options.add(new MenuItem("Help", this::printHelp));
        options.add(new MenuItem("Save Simulation", this::saveSimulation));
        options.add(new MenuItem("Exit", controller::pauseSimulation));

        int choice;
        do {
            choice = Utils.showAndSelectIndex(options, "\n--- SIMULATOR MENU ------------------------");
            if (choice >= 0 && choice < options.size()) {
                options.get(choice).run();
            }
        } while (choice != -1);
    }

    private void printHelp() {
        System.out.println("Available options:");
        System.out.println("  - Start: start auto simulation");
        System.out.println("  - Pause: pause auto simulation");
        System.out.println("  - Step: advance one month manually");
        System.out.println("  - Status: show simulation status");
        System.out.println("  - Edit: open UC5–UC12 menu");
        System.out.println("  - Save Simulation: save simulation state");
        System.out.println("  - Exit: quit simulation");
    }

    /**
     * Asks the user for the file name and saves the simulation state
     * using the SimulatorController.
     */
    private void saveSimulation() {
        File dir = new File(SAVE_DIR);
        dir.mkdirs();

        Scanner sc = new Scanner(System.in);
        System.out.print("File name to save: ");
        String name = sc.nextLine().trim();
        String path = SAVE_DIR + File.separator + name + ".sim";

        controller.saveToFile(path);
        System.out.println("Simulation saved at: " + path);
    }

}