package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.repository.*;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static pt.ipp.isep.dei.ui.console.menu.PlayerMenuUI.SAVE_DIR;

/**
 * Controller responsible for running the simulation, handling time progression,
 * industry and station updates, and moving trains along their routes.
 */
public class SimulatorController {
    private final Map map;
    private final Time clock;
    private final IndustryRepository industryRepo;
    private final LocomotiveRepository locomotiveRepo;
    private final Player player;
    private Scenario selectedScenario;
    private final LocomotiveCatalog catalog;


    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> autoStepFuture;

    private final List<TransactionRecord> transactionHistory = new ArrayList<>();
    private int lastFinancialReportYear = -1; // -1 so first year triggers


    /**
     * Constructs a SimulatorController with the required domain components.
     *
     * @param map            The map used for the simulation.
     * @param clock          The simulation clock.
     * @param industryRepo   Repository of industries.
     * @param locomotiveRepo Repository of locomotives.
     * @param player         The player whose trains will be simulated.
     */
    public SimulatorController(Map map,
                               Time clock,
                               IndustryRepository industryRepo,
                               LocomotiveRepository locomotiveRepo,
                               Player player, Scenario scenario) {
        this.map = map;
        this.clock = clock;
        this.industryRepo = industryRepo;
        this.locomotiveRepo = locomotiveRepo;
        this.player = player;
        this.selectedScenario = scenario;
        this.catalog = new LocomotiveCatalog();

// Aplica o cenário, se existir
        if (this.selectedScenario != null) {
            selectedScenario.applyAll(map, catalog);
        }
    }

    /**
     * Starts automatic simulation progression at the given interval in seconds.
     *
     * @param tickIntervalSeconds interval between simulation steps
     */
    public void startSimulation(long tickIntervalSeconds) {
        if (autoStepFuture == null || autoStepFuture.isDone() || autoStepFuture.isCancelled()) {
            autoStepFuture = scheduler.scheduleAtFixedRate(
                    () -> {
                        if (!clock.isFinished()) {
                            List<String> events = stepWithEvents();
                            for (String event : events) {
                                System.out.println("[AUTO] " + event);
                            }
                        } else {
                            pauseSimulation();
                        }
                    },
                    0,
                    tickIntervalSeconds,
                    TimeUnit.SECONDS
            );
        }
    }

    /**
     * Pauses the automatic simulation progression.
     */
    public void pauseSimulation() {
        if (autoStepFuture != null) {
            autoStepFuture.cancel(false);
            autoStepFuture = null;
        }
    }

    /**
     * Advances the simulation by one month, updates the map,
     * performs industry and station updates, moves all player trains,
     * applies scenario events, and returns a list of generated events.
     *
     * @return list of event descriptions generated this step
     */
    public List<String> stepWithEvents() {
        clock.advanceByMonth();
        map.update(player);

        List<String> events = new ArrayList<>();

        // --- Move player trains along their assigned routes ---
        for (Train train : player.getOwnedTrains()) {
            // (a) If currently traveling, advance by one month
            if (train.isInTransit()) {
                boolean justArrived = train.advanceTravelByOneMonth();
                if (justArrived) {
                    Station arrived = train.getAssignedRoute()
                            .getPointsOfRoute()
                            .get(train.getCurrentStopIndex())
                            .getStation();

                    events.add("🚆🚦 Train '" + train.getName() + "' has arrived at " + arrived.getStationName() + "!");

                    // --- Unloading Cargo (DESCARREGAR) ---
                    events.addAll(unloadCargoAtStation(train, arrived));

                    // --- Loading Cargo (CARREGAR) ---
                    events.addAll(loadCargoAtStation(train, arrived));
                }
            }
            // (b) If at station and has a route, start traveling to the next stop
            else if (train.hasRoute()) {
                Station nextStation = train.getNextStationOnRoute();
                if (nextStation != null) {
                    Station currentStation = train.getAssignedRoute()
                            .getPointsOfRoute()
                            .get(train.getCurrentStopIndex())
                            .getStation();
                    double distanceKm = map.getShortestDistance(currentStation, nextStation);

                    // --- Unloading Cargo (DESCARREGAR) ---
                    //events.addAll(unloadCargoAtStation(train, currentStation));

                    // --- Loading Cargo (CARREGAR) ---
                    events.addAll(loadCargoAtStation(train, currentStation));


                    // 1. Get current PointOfRoute and check CargoMode
                    PointOfRoute currentPOR = train.getAssignedRoute().getPointsOfRoute().get(train.getCurrentStopIndex());
                    CargoMode cargoMode = currentPOR.getCargoMode();

                    // --- DEBUG: Print if carriages are full ---
                    //for (CargoType ctype : train.getAssignedCargoTypesForCurrentStop()) {
                    //    System.out.println("DEBUG: activeCarriagesFull(" + ctype + ") = " + train.activeCarriagesFull(ctype));
                    //}

                    // 2. Check if train is allowed to depart
                    boolean canDepart = true;
                    for (CargoType ctype : train.getAssignedCargoTypesForCurrentStop()) {
                        switch (cargoMode) {
                            case FULL:
                                if (!train.activeCarriagesFull(ctype)) {
                                    canDepart = false;
                                    break;
                                }
                                break;
                            case HALF:
                                if (train.activeCarriagesLoadedCount(ctype) < (train.activeCarriagesCount(ctype) + 1) / 2) {
                                    canDepart = false;
                                    break;
                                }
                                break;
                            case AVAILABLE:
                                // always true, so do nothing
                                break;
                        }
                        if (!canDepart) break; // stop checking if any cargo type is not ready
                    }


                    if (Double.isFinite(distanceKm) && canDepart) {

                        // Get the RailLine between the current and next station
                        RailLine railLine = map.getRailLines().stream()
                                .filter(rl -> (rl.getStart().equals(currentStation) && rl.getEnd().equals(nextStation)) ||
                                        (rl.getStart().equals(nextStation) && rl.getEnd().equals(currentStation)))
                                .findFirst()
                                .orElse(null);

                        boolean lineIsDouble = false;

                        if (railLine != null) {
                            // Get the track type: true if double, false if single
                            lineIsDouble = railLine.isDouble();
                        }

                        train.startTravelTo(nextStation, distanceKm, lineIsDouble);
                        train.addDistanceTraveled(distanceKm);

                        events.add(
                                "Train '" + train.getName() + "' departed " +
                                        currentStation.getStationName() +
                                        " for " + nextStation.getStationName() +
                                        " (distance: " + String.format("%.1f", distanceKm) + " km)"
                        );
                    } else if (!canDepart) {
                        events.add("Train '" + train.getName() + "' waiting at " + currentStation.getStationName()
                                + " (mode: " + cargoMode + ")");
                    } else {
                        events.add(
                                "ERROR: No rail route from '" + currentStation.getStationName() +
                                        "' to '" + nextStation.getStationName() + "' for train '" + train.getName() + "'"
                        );
                    }
                }
            }
        }

        // --- Industry to station collection/distribution (optional, for your legacy logic) ---
        for (Station st : map.getStations()) {
            events.addAll(st.collectAndDistribute(map.getIndustries()));
        }

        // --- Scenario events (if applicable) ---
        ScenarioRepository repo = Repositories.getInstance().getScenarioRepository();
        Scenario selected = getSelectedScenario();
//        if (selected != null) {
//            List<String> scEvents = selected.applyAll(map, map.getLocomotives());
//            if (scEvents != null) {
//                events.addAll(scEvents);
//            }
//        }

        // Monthly maintenance costs charged to player and transaction is recorded
        double monthlyTrackMaintenanceCost = calculateMonthlyTrackMaintenanceCosts();
        double monthlyLocomotiveMaintenanceCost = calculateMonthlyLocomotiveMaintenanceCosts();

        player.spend(monthlyTrackMaintenanceCost);

        transactionHistory.add(new TransactionRecord(
                TransactionRecord.Type.TRACK_MAINTENANCE,
                -monthlyTrackMaintenanceCost,
                clock.getDate().getYear()
        ));

        player.spend(monthlyLocomotiveMaintenanceCost);

        transactionHistory.add(new TransactionRecord(
                TransactionRecord.Type.TRAIN_MAINTENANCE,
                -monthlyLocomotiveMaintenanceCost,
                clock.getDate().getYear()
                ));

        // Train fuel costs
        for (Train train : player.getOwnedTrains()) {
            double distance = train.consumeAndResetDistanceTraveledThisMonth();
            double monthlyFuelCost = train.getLocomotive().getFuelCostPerKm() * distance;
            if (monthlyFuelCost > 0) {
                player.spend(monthlyFuelCost);
                transactionHistory.add(new TransactionRecord(
                        TransactionRecord.Type.FUEL,
                        -monthlyFuelCost,
                        clock.getDate().getYear()
                ));
            }
            //debug
            //System.out.printf("Train traveled %.0f and spent %.2f on fuel.", distance, monthlyFuelCost);
        }

        // DEBUG costs.
        // System.out.printf("\nCompany spent %.2f on track maintenance and %.2f on locomotive maintenance this month%n",
           //     monthlyTrackMaintenanceCost, monthlyLocomotiveMaintenanceCost);
        System.out.printf("\nCurrent budget: %.2f\n", player.getBudget());


        // --- Debug print: station stocks ---
        /*for (Station st : map.getStations()) {
            System.out.println("DEBUG: station " + st.getStationName() +
                    " stock = " + st.getCargoes().stream()
                    .filter(c -> c.getQuantity() != 0.0) // Only include non-zero quantities
                    .map(c -> c.getType() + ":" + c.getQuantity())
                    .collect(Collectors.joining(", ")));
        }*/

        // -- Check if year ended and return financial report for that year
        int currentYear = clock.getDate().getYear();
        if (lastFinancialReportYear == -1) {
            lastFinancialReportYear = currentYear;
        } else if (currentYear > lastFinancialReportYear) {
            // End of the previous year reached, generate and announce financial record.
            events.add("Financial Record for " + lastFinancialReportYear + " now available.");
            List<String> financialSummary = FinancialReportService.getYearlyFinancialSummary(
                    transactionHistory, lastFinancialReportYear
            );
            events.addAll(financialSummary);

            // Update station demand per cargo type.
            for (Station st : map.getStations()) {
                for (CargoType type : CargoType.values()) {
                    int delivered = st.getDeliveredThisYear(type);
                    // Reduce demand for cargo when 30 or more units were received at station
                    if (delivered >= 30) {st.setDemandFor(type, st.getDemandFor(type) - 1);
                    //System.out.println("DEBUG Station Demand Update. " + st.getStationName() +
                      //      " demand for " + type.toString() +
                        //    " was reduced by 1 point.");
                        }
                    // Increase demand for cargo when 10 or less units were received at station
                    else if (delivered > 0 && delivered <= 10) st.setDemandFor(type, st.getDemandFor(type) + 1);
                    st.resetDeliveredThisYear(type);


                }
            }
            lastFinancialReportYear = currentYear;
        }

        return events;
    }

    /**
     * Unload cargo from train carriages at the station.
     * @param train The train unloading cargo.
     * @param arrived The station at which the train arrives.
     * @return List of events describing the unloading actions.
     */
    private List<String> unloadCargoAtStation(Train train, Station arrived) {
        List<String> events = new ArrayList<>();
        for (Carriage carriage : train.getCarriages()) {
            CargoType cargoType = carriage.getCargoType();
            int amountToUnload = carriage.getCurrentLoad();
            if (amountToUnload > 0) {
                arrived.unloadCargo(cargoType, amountToUnload);  // Unload cargo at station
                carriage.unloadCargo();  // Unload cargo from the carriage
                events.add("Train '" + train.getName() + "' delivered "
                        + amountToUnload + " of " + cargoType
                        + " at station '" + arrived.getStationName() + "'");

                // Train deliveries generate revenue, transaction gets recorded
                int demand = arrived.getDemandFor(cargoType);
                //double base = getBaseCargoValue(cargoType); // Implement this function somewhere (lookup or hardcoded)
                double base = 100.0;
                double revenue = 0.0;
                if (demand > 0) {
                    double multiplier = 1.0 + 0.2 * (demand - 5);
                    revenue = base * multiplier * amountToUnload;
                    player.earn(revenue);
                    transactionHistory.add(new TransactionRecord(
                            TransactionRecord.Type.REVENUE_DELIVERY,
                            revenue,
                            clock.getDate().getYear()
                    ));
                }
                arrived.addDeliveredThisYear(cargoType, amountToUnload);
            }
        }
        return events;
    }

    /**
     * Load cargo from the station into the train's carriages.
     * @param train The train loading cargo.
     * @param arrived The station from which the train loads cargo.
     * @return List of events describing the loading actions.
     */
    private List<String> loadCargoAtStation(Train train, Station arrived) {
        List<String> events = new ArrayList<>();
        for (CargoType ctype : train.getAssignedCargoTypesForCurrentStop()) {
            // Only pass the correct carriages for this cargo type!
            List<Carriage> activeCarriages = train.getActiveCarriagesForCurrentStop(ctype);
            int collectedQty = arrived.collectCargoOfType(ctype, activeCarriages);
            if (collectedQty > 0) {
                events.add("Train '" + train.getName() + "' collected "
                        + collectedQty + " of " + ctype
                        + " at station '" + arrived.getStationName() + "'");
            }
        }
        return events;
    }


    /**
     * Calculates the monthly maintenance cost of all rail lines on the map.
     * <p>
     * Each rail line has a maintenance cost per kilometer, which is multiplied
     * by its distance. Double-track and single-track lines are treated separately,
     * but both contribute to the total cost. The yearly total is then divided by 12.
     *
     * @return the total monthly maintenance cost for track infrastructure
     */

    public double calculateMonthlyTrackMaintenanceCosts() {
        double singleTrackKm = 0.0;
        double doubleTrackKm = 0.0;

        for (RailLine railLine : map.getRailLines()) {
            if (railLine.isDouble()) {
                doubleTrackKm += railLine.getDistance()*railLine.getMaintenanceCost();
            } else {
                singleTrackKm += railLine.getDistance()*railLine.getMaintenanceCost();
            }
        }

        // Calculate the monthly maintenance cost for each track type (based on yearly cost)
        return ((singleTrackKm + doubleTrackKm ) / 12);
    }

    /**
     * Calculates the total monthly maintenance cost for all locomotives owned by the player.
     * <p>
     * Each train's locomotive has a yearly maintenance cost, which is converted to
     * a monthly cost and summed across all owned trains.
     *
     * @return the total monthly locomotive maintenance cost
     */

    public double calculateMonthlyLocomotiveMaintenanceCosts() {
        double monthlyLocomotiveMaintenanceCost = 0.0;
        for (Train train : player.getOwnedTrains()) {
            monthlyLocomotiveMaintenanceCost += train.getLocomotive().getMaintenancePerYear() / 12;
        }
        return monthlyLocomotiveMaintenanceCost;
    }


    /**
     * Returns the current simulation date/time.
     *
     * @return current date in simulation
     */
    public LocalDateTime getCurrentDate() {
        return clock.getDate();
    }

    /**
     * Returns the industries present on the map.
     *
     * @return list of industries
     */
    public List<Industry> getIndustries() {
        return map.getIndustries();
    }

    /**
     * Returns the stations present on the map.
     *
     * @return list of stations
     */
    public List<Station> getStations() {
        return map.getStations();
    }


    /**
     * Returns the available locomotive templates from the repository.
     *
     * @return list of locomotive templates
     */
    public List<Locomotive> getLocomotiveTemplates() {
        return locomotiveRepo.getAllTemplates();
    }

    /**
     * Sets the currently selected scenario.
     *
     * @param scenario the scenario to select
     */
    public void setSelectedScenario(Scenario scenario) {
        this.selectedScenario = scenario;
    }

    /**
     * Returns the currently selected scenario.
     *
     * @return the selected scenario
     */
    public Scenario getSelectedScenario() {
        return selectedScenario;
    }

    /**
     * Shuts down the scheduler and releases resources.
     */
    public void shutdown() {
        pauseSimulation();
        scheduler.shutdownNow();
    }

    /**
     * Saves the current simulation state to a file.
     * <p>
     * Creates a {@link SimulationSnapshot} containing the current map,
     * clock, player, selected scenario, and owned trains, and serializes
     * it to the specified file.
     *
     * @param filename the path to the file where the simulation will be saved
     * @throws RuntimeException if an I/O error occurs during saving
     */
    public void saveToFile(String filename) {
        SimulationSnapshot snap = new SimulationSnapshot(
                map, clock, player, selectedScenario, player.getOwnedTrains()
        );
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(snap);
        } catch (IOException e) {
            throw new RuntimeException("Error saving simulation", e);
        }
    }

    /**
     * Loads a saved simulation snapshot from a .sim file.
     * @param filename the full or relative path to the file
     * @return the snapshot containing the entire state
     */
    public static SimulationSnapshot loadFromFile(String filename) {
        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(filename))) {
            return (SimulationSnapshot) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error loading simulation", e);
        }
    }


    public List<City> getCities() {
        return map.getCities();
    }
    public LocomotiveCatalog getCatalog() {
        return catalog;
    }

}
