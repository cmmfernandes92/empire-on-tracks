package pt.ipp.isep.dei.ui.gui;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import pt.ipp.isep.dei.controller.BuildLineController;
import pt.ipp.isep.dei.domain.Player;
import pt.ipp.isep.dei.domain.RailLine;
import pt.ipp.isep.dei.domain.Station;

import java.util.List;

public class BuildLineFormController {

    @FXML private ComboBox<Station> firstStationComboBox;
    @FXML private ComboBox<Station> secondStationComboBox;
    @FXML private CheckBox electrifiedCheckBox;
    @FXML private CheckBox doubleTrackCheckBox;
    @FXML private Label budgetLabel;

    private Player player;
    private BuildLineController buildLineController;
    private BudgetRefresh budgetRefresh;

    public void init(Player player,
                     List<Station> stationList,
                     BuildLineController buildLineController,
                     BudgetRefresh budgetRefresh) {

        this.player = player;
        this.buildLineController = buildLineController;
        this.budgetRefresh = budgetRefresh;

        firstStationComboBox.getItems().setAll(stationList);
        secondStationComboBox.getItems().setAll(stationList);

        updateBudgetLabel();
    }

    @FXML
    private void handleCreate() {

        Station firstStation  = firstStationComboBox.getValue();
        Station secondStation = secondStationComboBox.getValue();

        if (firstStation == null || secondStation == null || firstStation.equals(secondStation)) {
            showInfo("Select two distinct stations.");
            return;
        }

        boolean electrified = electrifiedCheckBox.isSelected();
        boolean doubleTrack = doubleTrackCheckBox.isSelected();

        RailLine newLine = buildLineController.createRailLine(
                firstStation, secondStation, electrified, doubleTrack);

        if (newLine == null) {
            showInfo("Failed to create rail line: insufficient budget or line already exists.");
            return;
        }

        showInfo("Rail line created successfully.");
        updateBudgetLabel();
        if (budgetRefresh != null) budgetRefresh.refreshBudget();
        close();
    }

    @FXML
    private void handleCancel() {
        close();
    }

    private void updateBudgetLabel() {
        budgetLabel.setText("Current budget: " +
                String.format("%,.2f", player.getBudget()));
    }

    private void close() {
        Stage stage = (Stage) firstStationComboBox.getScene().getWindow();
        stage.close();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
