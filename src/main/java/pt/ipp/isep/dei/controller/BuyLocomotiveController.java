package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.mapper.LocomotiveMapper;
import pt.ipp.isep.dei.mapper.dto.LocomotiveDTO;

import java.util.ArrayList;
import java.util.List;

public class BuyLocomotiveController {
    private final Player player;
    private final LocomotiveCatalog catalog;

    public BuyLocomotiveController(Player player, LocomotiveCatalog catalog) {
        this.player = player;
        this.catalog = catalog;
    }

    public List<Locomotive> requestAvailableLocomotives() {
        return catalog.getAllLocomotives(); // Usa o catálogo do cenário
    }

    public List<LocomotiveDTO> getAvailableLocomotivesDTO() {
        return LocomotiveMapper.toDTO(requestAvailableLocomotives());
    }

    public int buyLocomotiveAsTrain(String locomotiveID, String trainName) {
        for (Train t : player.getOwnedTrains()) {
            if (t.getName().equalsIgnoreCase(trainName)) {
                return 2;
            }
        }

        Locomotive chosenLocomotive = null;
        for (Locomotive loco : requestAvailableLocomotives()) {
            if (loco.getId().equals(locomotiveID)) {
                chosenLocomotive = loco;
                break;
            }
        }

        if (chosenLocomotive == null) return 3;
        if (!player.hasSufficientBudget(chosenLocomotive.getPrice())) return 1;

        player.buyLocomotive(chosenLocomotive);

        String plateNumber = player.createUniquePlateNumber(chosenLocomotive);
        Train newTrain = new Train(trainName, plateNumber, chosenLocomotive, new ArrayList<>(), new ArrayList<>());
        player.addTrain(newTrain);

        return 0;
    }

    public Player getPlayer() {
        return player;
    }
}
