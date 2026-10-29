package pt.ipp.isep.dei.ui.gui;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import pt.ipp.isep.dei.domain.Building;
import pt.ipp.isep.dei.domain.BuildingType;
import pt.ipp.isep.dei.domain.Player;
import pt.ipp.isep.dei.domain.Station;
import java.util.Date;
import java.util.List;

public class UpgradeStationFormController {

    @FXML
    private ComboBox<Station> stationComboBox;
    @FXML
    private ComboBox<String> upgradeComboBox;
    @FXML
    private Label stationNameLabel;
    @FXML
    private Label budgetLabel;

    private Player player;
    private List<Station> stationList;
    private BudgetRefresh budgetRefresh;

    public void init(Player player, List<Station> stationList) {
        init(player, stationList, null);
    }

    public void init(Player player, List<Station> stationList, BudgetRefresh budgetRefresh) {
        if (player == null || stationList == null) {
            throw new IllegalArgumentException("Player and station list must not be null");
        }
        this.player = player;
        this.stationList = stationList;
        this.budgetRefresh = budgetRefresh;

        stationComboBox.getItems().setAll(stationList);
        upgradeComboBox.getItems().setAll(
                "TELEGRAPH 200$", "TELEPHONE 400$", "SMALL_CAFE 150$", "LARGE_CAFE 300$",
                "CUSTOMS 250$", "POST_OFFICE 220$", "SMALL_HOTEL 350$", "LARGE_HOTEL 500$",
                "SILO 180$", "LIQUID_STORAGE 270$"
        );
        updateBudgetLabel();
    }

    private void updateBudgetLabel() {
        if (budgetLabel != null) {
            budgetLabel.setText("Budget: $" + String.format("%,.2f", player.getBudget()));
        }
    }

    @FXML
    private void handleApplyUpgrade() {
        Station selectedStation = stationComboBox.getValue();
        String selectedUpgrade = upgradeComboBox.getValue();
        if (selectedStation == null || selectedUpgrade == null) {
            showAlert("Please select a station and an upgrade.");
            return;
        }
        double cost = getUpgradeCost(selectedUpgrade);
        if (player.getBudget() < cost) {
            showAlert("You do not have enough budget for this upgrade.");
            return;
        }
        player.spend(cost);

        Building newBuilding = new Building(selectedUpgrade, new Date(), true);
        selectedStation.getBuildings().add(newBuilding);
        showAlert("Upgrade " + selectedUpgrade + " applied to " + selectedStation.getStationName());
        updateBudgetLabel();
        if (budgetRefresh != null) budgetRefresh.refreshBudget();
        closeWindow();
    }

    private double getUpgradeCost(String upgrade) {
        return switch (upgrade) {
            case "TELEGRAPH 200$"     -> 200.0;
            case "TELEPHONE 400$"     -> 400.0;
            case "SMALL_CAFE 150$"    -> 150.0;
            case "LARGE_CAFE 300$"    -> 300.0;
            case "CUSTOMS 250$"       -> 250.0;
            case "POST_OFFICE 220$"   -> 220.0;
            case "SMALL_HOTEL 350$"   -> 350.0;
            case "LARGE_HOTEL 500$"   -> 500.0;
            case "SILO 180$"          -> 180.0;
            case "LIQUID_STORAGE 270$"-> 270.0;
            default              -> 0.0;
        };
    }

    @FXML
    private void handleBack() {
        closeWindow();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) stationComboBox.getScene().getWindow();
        stage.close();
    }
}