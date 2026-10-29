package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.Building;
import pt.ipp.isep.dei.domain.Player;
import pt.ipp.isep.dei.domain.Station;
import pt.ipp.isep.dei.domain.GameState;
import pt.ipp.isep.dei.controller.SimulationInGameData;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Controller for managing station upgrades.
 */
public class UpgradeStationController {

    private final Station station;
    private final Player player;

    public UpgradeStationController(Station station, Player player) {
        this.station = station;
        this.player = player;
    }

    /**
     * Checks whether a building is available based on current simulation date.
     * For example, TELEPHONE is only available after 1876.
     */
    public boolean isUpgradeAvailable(String type) {
        if (type.equalsIgnoreCase("TELEPHONE")) {
            LocalDate current = SimulationInGameData.getCurrentDate();
            return current.isAfter(LocalDate.of(1876, 1, 1));
        }
        return true;
    }

    /**
     * Checks if the given building type is compatible with the station.
     * For now, all buildings are considered compatible.
     */
    public boolean isCompatible(String type) {
        return true; // Extend this if needed in the future
    }

    /**
     * Checks whether the player has enough budget to afford the upgrade.
     */
    public boolean hasSufficientBudget(double cost) {
        return player.hasSufficientBudget(cost);
    }

    /**
     * Checks whether the station already has the given building installed and active.
     */
    public boolean hasBuilding(String type) {
        return station.hasBuilding(type);
    }

    /**
     * Applies the building upgrade, handling budget deduction, replacement logic, and game saving.
     *
     * @param type the type of building to install
     * @param cost the cost of the upgrade
     * @return true if successful, false otherwise
     */
    public boolean applyUpgrade(String type, double cost) {
        if (!isUpgradeAvailable(type)) return false;
        if (!isCompatible(type)) return false;
        if (hasBuilding(type)) return false;
        if (!player.hasSufficientBudget(cost)) return false;

        // Replace TELEGRAPH if installing TELEPHONE
        if (type.equalsIgnoreCase("TELEPHONE") && station.hasBuilding("TELEGRAPH")) {
            station.removeBuilding("TELEGRAPH");
        }

        // Create building with current simulation date
        LocalDate localDate = SimulationInGameData.getCurrentDate();
        Date installDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Building building = new Building(type, installDate, true);
        station.addBuilding(building);

        player.spend(cost);
        GameState.getInstance().save();

        return true;
    }
}
