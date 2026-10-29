package pt.ipp.isep.dei.controller;

import java.time.LocalDate;

/**
 * Simulation object to get current in-game date.
 */
public class SimulationInGameData{
    private static LocalDate currentDate = LocalDate.of(1850, 1, 1);

    public static LocalDate getCurrentDate() {
        return currentDate;
    }

    public static void advanceYears(int years) {
        currentDate = currentDate.plusYears(years);
    }
}