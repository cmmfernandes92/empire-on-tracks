package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.Player;
import pt.ipp.isep.dei.domain.Train;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for listing trains owned by the player.
 */
public class ListTrainsController {
    private Player player;

    public ListTrainsController(Player player) {
        this.player = player;
    }

    /**
     * Returns the list of owned trains, grouped by locomotive type and sorted by name.
     */
    public List<Train> requestTrainList() {
        List<Train> trains = player.getOwnedTrains();
        return groupAndSortTrains(trains);
    }

    /**
     * Groups by locomotive type, sorts by name.
     */
    private List<Train> groupAndSortTrains(List<Train> trains) {
        return trains.stream()
                .sorted(Comparator.comparing(Train::getLocomotiveType)
                        .thenComparing(Train::getName))
                .collect(Collectors.toList());
    }
}
