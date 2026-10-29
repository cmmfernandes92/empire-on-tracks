package pt.ipp.isep.dei.domain;

import pt.ipp.isep.dei.user.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a player in the simulation, managing budget, owned locomotives and trains,
 * and scenario selection.
 * <p>
 * Allows purchasing locomotives, generating unique plate numbers for trains,
 * and performing budget transactions (earn/spend).
 * </p>
 *
 * @author Team
 * @version 1.0
 */
public class Player implements Serializable {

    private double budget;
    private final List<Locomotive> ownedLocomotives = new ArrayList<>();
    private final List<Train> ownedTrains = new ArrayList<>();
    private Scenario selectedScenario;
    private java.util.Map<String, Integer> plateCounters = new java.util.HashMap<>();

    /**
     * Constructs a Player with an initial budget.
     *
     * @param initialBudget starting budget; must be non-negative
     * @throws IllegalArgumentException if initialBudget is negative
     */
    public Player(double initialBudget) {
        if (initialBudget < 0) {
            throw new IllegalArgumentException("Initial budget cannot be negative");
        }
        this.budget = initialBudget;
    }

    /**
     * Returns the current budget.
     *
     * @return player's budget
     */
    public double getBudget() {
        return budget;
    }

    /**
     * Sets a new budget value.
     *
     * @param budget new budget; must be non-negative
     * @throws IllegalArgumentException if budget is negative
     */
    public void setBudget(double budget) {
        if (budget < 0) {
            throw new IllegalArgumentException("Budget cannot be negative");
        }
        this.budget = budget;
    }

    /**
     * Checks if the player has enough budget for a given amount.
     *
     * @param price amount to check
     * @return {@code true} if budget is at least price
     */
    public boolean hasSufficientBudget(double price) {
        return budget >= price;
    }

    /**
     * Attempts to purchase a locomotive.
     * <p>
     * If the player has sufficient budget, deducts the locomotive price,
     * adds it to ownedLocomotives, and returns it; otherwise returns null.
     * </p>
     *
     * @param loco locomotive to buy; must not be null
     * @return the purchased Locomotive, or {@code null} if insufficient budget
     * @throws NullPointerException if loco is null
     */
    public Locomotive buyLocomotive(Locomotive loco) {
        Objects.requireNonNull(loco, "Locomotive cannot be null");
        if (hasSufficientBudget(loco.getPrice())) {
            budget -= loco.getPrice();
            ownedLocomotives.add(loco);
            return loco;
        }
        return null;
    }

    /**
     * Returns an unmodifiable view of owned locomotives.
     *
     * @return list of owned locomotives
     */
    public List<Locomotive> getOwnedLocomotives() {
        return Collections.unmodifiableList(ownedLocomotives);
    }

    /**
     * Generates a unique plate number for a train based on its locomotive ID.
     * <p>
     * Ensures the returned plate is not already used by any owned train.
     * </p>
     *
     * @param locomotive locomotive to base the plate number on
     * @return unique plate string in format "ID-number"
     */
    public String createUniquePlateNumber(Locomotive locomotive) {
        String locoId = locomotive.getId();
        int nextNum = plateCounters.getOrDefault(locoId, 1);
        String plate;
        do {
            plate = String.format("%s-%d", locoId, nextNum);
            nextNum++;
        } while (isPlateNumberUsed(plate));
        plateCounters.put(locoId, nextNum);
        return plate;
    }

    /**
     * Checks if a plate number is already used by owned trains.
     *
     * @param plate plate number to check
     * @return {@code true} if any owned train has this plate
     */
    private boolean isPlateNumberUsed(String plate) {
        for (Train t : ownedTrains) {
            if (plate.equals(t.getPlateNumber())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a train to the player's collection.
     *
     * @param train train to add; must not be null
     * @throws NullPointerException if train is null
     */
    public void addTrain(Train train) {
        Objects.requireNonNull(train, "Train cannot be null");
        ownedTrains.add(train);
    }

    /**
     * Returns an unmodifiable view of owned trains.
     *
     * @return list of owned trains
     */
    public List<Train> getOwnedTrains() {
        return Collections.unmodifiableList(ownedTrains);
    }

    /**
     * Attempts to spend an amount from the budget.
     *
     * @param amount amount to spend; must be non-negative
     * @return {@code true} if spending succeeded; {@code false} otherwise
     * @throws IllegalArgumentException if amount is negative
     */
    public boolean spend(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Spend amount cannot be negative");
        }
        if (hasSufficientBudget(amount)) {
            budget -= amount;
            return true;
        }
        return false;
    }

    /**
     * Adds an earning amount to the budget.
     *
     * @param amount amount to earn; must be non-negative
     * @return {@code true} after earning
     * @throws IllegalArgumentException if amount is negative
     */
    public boolean earn(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Earning amount cannot be negative");
        }
        budget += amount;
        return true;
    }

    /**
     * Returns the currently selected scenario.
     *
     * @return selected scenario, or {@code null} if none set
     */
    public Scenario getSelectedScenario() {
        return selectedScenario;
    }

    /**
     * Sets the selected scenario.
     *
     * @param selectedScenario scenario to select
     */
    public void setSelectedScenario(Scenario selectedScenario) {
        this.selectedScenario = selectedScenario;
    }

    /**
     * Replaces the list of owned trains (e.g., when loading a snapshot).
     *
     * @param trains list of trains; must not be null
     * @throws NullPointerException if trains is null
     */
    public void setOwnedTrains(List<Train> trains) {
        Objects.requireNonNull(trains, "Train list cannot be null");
        this.ownedTrains.clear();
        this.ownedTrains.addAll(trains);
    }

    /**
     * Removes a train from the player's collection.
     *
     * @param train train to remove
     * @return {@code true} if removed; {@code false} otherwise
     */
    public boolean removeTrain(Train train) {
        return ownedTrains.remove(train);
    }

    /**
     * Retrieves a train by its name.
     *
     * @param name name of the train
     * @return train with matching name, or {@code null} if none found
     */
    public Train getTrainByName(String name) {
        return ownedTrains.stream()
                .filter(t -> t.getName().equals(name))
                .findFirst().orElse(null);
    }

    /**
     * Checks if the budget is enough for a given industry cost.
     *
     * @param industryCost cost to check
     * @return {@code true} if budget is at least industryCost
     */
    public boolean hasBudgetFor(int industryCost) {
        return budget >= industryCost;
    }
}
