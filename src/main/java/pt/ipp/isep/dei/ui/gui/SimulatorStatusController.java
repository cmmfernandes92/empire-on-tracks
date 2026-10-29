package pt.ipp.isep.dei.ui.gui;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import pt.ipp.isep.dei.controller.ListTrainsController;
import pt.ipp.isep.dei.controller.SimulatorController;
import pt.ipp.isep.dei.domain.*;

import java.util.List;
import java.util.stream.Collectors;

public class SimulatorStatusController {
    private Player player;
    @FXML
    private TextArea statusTextArea;

    private SimulatorController simCtrl;

    public void init(SimulatorController simCtrl, Player player) {
        this.simCtrl = simCtrl;
        this.player = player;
        updateStatus();
    }

    private void updateStatus() {
        StringBuilder sb = new StringBuilder();

        sb.append("== SIMULATION STATUS ==\n");
        sb.append("Current date: ").append(simCtrl.getCurrentDate()).append("\n\n");

        sb.append("-- Industries on Map --\n");
        for (Industry ind : simCtrl.getIndustries()) {
            Cargo cargo = ind.getCargo();
            sb.append(String.format("%s | %s: %.2f | Prod: %.2f | Dem: %.2f | Loc: %s\n",
                    ind.getIndustryName(),
                    cargo.getType(), cargo.getQuantity(),
                    ind.getProduction(), ind.getDemand(),
                    ind.getLocation()));
        }
        sb.append("\n");

        sb.append("-- Stations and Cargoes --\n");
        for (Station st : simCtrl.getStations()) {
            String cargoes = st.getCargoes().stream()
                    .filter(c -> c.getQuantity() > 0)
                    .map(c -> String.format("%s: %.2f", c.getType(), c.getQuantity()))
                    .collect(Collectors.joining(", "));
            sb.append(String.format("%s @ %s -> [%s]\n",
                    st.getStationName(), st.getLocation(),
                    cargoes.isEmpty() ? "vazio" : cargoes));
        }
        sb.append("\n");

        sb.append("-- Trains --\n");
        ListTrainsController trainCtrl = new ListTrainsController(player);
        for (Train t : trainCtrl.requestTrainList()) {
            sb.append(String.format("[%s] loco=%s route=%s load=%s\n",
                    t.getName(),
                    t.getLocomotiveType(),
                    t.getAssignedRoute() != null ? t.getAssignedRoute().toString() : "none",
                    t.getCurrentCargoes()));
        }
        sb.append("\n");

        sb.append("-- Locomotive Templates --\n");
        for (Locomotive l : simCtrl.getLocomotiveTemplates()) {
            sb.append(l).append("\n");
        }

        statusTextArea.setText(sb.toString());
    }

    @FXML
    void handleClose(ActionEvent event) {
        ((Stage) statusTextArea.getScene().getWindow()).close();
    }
}

