package pt.ipp.isep.dei.ui.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import pt.ipp.isep.dei.domain.Building;
import pt.ipp.isep.dei.domain.Cargo;
import pt.ipp.isep.dei.domain.Station;

public class StationDetailsViewController {

    @FXML private Label title;
    @FXML private ListView<String> listBuildings;
    @FXML private ListView<String> listCargoes;

    public void init(Station station) {
        title.setText("Station: " + station.getStationName());

        for (Building building : station.getBuildings()) {
            listBuildings.getItems().add(building.toString());
        }
        for (Cargo cargo : station.getCargoes()) {
            listCargoes.getItems().add(cargo.toString());
        }
    }

    @FXML
    private void handleClose() {
        Stage currentStage = (Stage) title.getScene().getWindow();
        currentStage.close();
    }
}
