package pt.ipp.isep.dei.ui.gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import pt.ipp.isep.dei.controller.BuildStationController;
import pt.ipp.isep.dei.domain.City;
import pt.ipp.isep.dei.domain.CityManager;
import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.domain.StationType;
import pt.ipp.isep.dei.repository.Repositories;

import java.util.Arrays;
import java.util.List;

public class BuildStationFormController {

    @FXML
    private ComboBox<StationType> stationTypeCombo;

    @FXML
    private ComboBox<String> anchorCombo;

    @FXML
    private TextField xField;

    @FXML
    private TextField yField;

    @FXML
    private TextField stationNameField;

    @FXML
    private Label feedbackLabel;

    private Map map;
    private BuildStationController controller;

    @FXML
    private void initialize() {
        this.map = Repositories.getInstance().getMapRepository().getCurrentMap();
        this.controller = new BuildStationController(map, new CityManager(map.getCities()));

        stationTypeCombo.setItems(FXCollections.observableArrayList(StationType.values()));
        anchorCombo.setItems(FXCollections.observableArrayList("CENTER", "NE", "SE", "SW", "NW"));
        anchorCombo.setValue("CENTER");

        stationTypeCombo.setOnAction(e -> {
            StationType selected = stationTypeCombo.getValue();
            if (selected != null && selected == StationType.Station_4x4) {
                anchorCombo.setDisable(false);
            } else {
                anchorCombo.setDisable(true);
                anchorCombo.setValue("CENTER");
            }
        });
    }

    @FXML
    private void handleBuild() {
        try {
            StationType type = stationTypeCombo.getValue();
            int x = Integer.parseInt(xField.getText().trim());
            int y = Integer.parseInt(yField.getText().trim());
            String name = stationNameField.getText().trim();
            String orientation = anchorCombo.getValue();

            if (name.isBlank()) {
                City nearby = controller.getClosestCity(x, y);
                if (nearby != null) {
                    name = nearby.getName() + " Station";
                } else {
                    feedbackLabel.setText("Please enter a station name or select a location near a city.");
                    return;
                }
            }

            boolean success = controller.buildStation(name, x, y, type, orientation);
            feedbackLabel.setText(success ? "Station built successfully!" : "Error building station.");
        } catch (Exception e) {
            feedbackLabel.setText("Invalid input: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        ((Stage) xField.getScene().getWindow()).close();
    }
}

