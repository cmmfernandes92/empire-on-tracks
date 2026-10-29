package pt.ipp.isep.dei.ui.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import pt.ipp.isep.dei.controller.*;
import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.repository.Repositories;

import java.util.List;

public class EditSimulationMenuController {

    private Player player;
    private List<Station> stationList;
    private BudgetRefresh budgetRefresh;
    private SimulatorController simCtrl;

    public void init(Player player,
                     List<Station> stationList,
                     BudgetRefresh budgetRefresh,
                     SimulatorController simCtrl) {

        this.player = player;
        this.stationList = stationList;
        this.budgetRefresh = budgetRefresh;
        this.simCtrl = simCtrl;
    }

    @FXML
    void handleUC5BuildStation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/pt/ipp/isep/dei/ui/gui/BuildStationForm.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("UC5 - Build a Station");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showInfo("Could not open the Build Station form.\n" + e.getMessage());
        }
    }

    @FXML
    void handleUC6UpgradeStation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/pt/ipp/isep/dei/ui/gui/UpgradeStationForm.fxml"));
            Parent root = loader.load();
            List<Station> stationList = Repositories.getInstance()
                    .getMapRepository().getCurrentMap().getStations();
            if (stationList == null || stationList.isEmpty()) {
                showInfo("No stations available for upgrade.");
                return;
            }
            if (player == null) {
                showInfo("Player is not initialized.");
                return;
            }
            UpgradeStationFormController controller = loader.getController();
            controller.init(player, stationList, budgetRefresh);
            Stage stage = new Stage();
            stage.setTitle("Upgrade Station");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showInfo("Could not open Upgrade Station form.\n" + e.getMessage());
        }
    }

    @FXML
    void handleUC7ListStations(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/pt/ipp/isep/dei/ui/gui/ListStationsView.fxml"));
            Parent root = loader.load();
            ListStationsViewController listStationsController = loader.getController();
            listStationsController.init();
            Stage stage = new Stage();
            stage.setTitle("Station List");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showInfo("Could not open Station List.\n" + e.getMessage());
        }
    }

    @FXML
    void handleUC8BuildLine(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/pt/ipp/isep/dei/ui/gui/BuildLineForm.fxml"));
            Parent root = loader.load();
            List<Station> stationList = Repositories.getInstance()
                    .getMapRepository().getCurrentMap().getStations();
            if (stationList == null || stationList.size() < 2) {
                showInfo("At least two stations are required.");
                return;
            }
            if (player == null) {
                showInfo("Player is not initialized.");
                return;
            }
            BuildLineController buildLineController = new BuildLineController(
                    player,
                    Repositories.getInstance().getMapRepository().getCurrentMap());
            BuildLineFormController formController = loader.getController();
            formController.init(player, stationList, buildLineController, budgetRefresh);
            Stage stage = new Stage();
            stage.setTitle("Build Railway Line");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showInfo("Could not open Build Line form.\n" + e.getMessage());
        }
    }

    @FXML
    void handleUC9BuyLocomotive(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/pt/ipp/isep/dei/ui/gui/BuyLocomotiveForm.fxml"));
            Parent root = loader.load();
            if (player == null) {
                showInfo("Player is not initialized.");
                return;
            }
            BuyLocomotiveController buyLocomotiveController =
                    new BuyLocomotiveController(player, simCtrl.getCatalog());
            BuyLocomotiveFormController formController = loader.getController();
            formController.init(player, buyLocomotiveController, budgetRefresh);
            Stage stage = new Stage();
            stage.setTitle("Buy Locomotive");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showInfo("Could not open Buy Locomotive form.\n" + e.getMessage());
        }
    }

    @FXML
    void handleUC10AssignTrain(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/pt/ipp/isep/dei/ui/gui/RouteBuilderView.fxml"));
            Parent root = loader.load();
            AssignTrainToRouteController assignController =
                    new AssignTrainToRouteController(
                            player,
                            Repositories.getInstance().getMapRepository().getCurrentMap(),
                            Repositories.getInstance().getMapRepository().getCurrentMap().getStations(),
                            List.of(CargoType.values()));
            RouteBuilderController routeBuilderController = loader.getController();
            routeBuilderController.init(assignController);
            Stage stage = new Stage();
            stage.setTitle("Assign Train to Route");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showInfo("Could not open Route Builder.\n" + e.getMessage());
        }
    }

    @FXML
    void handleUC11ListTrains(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/pt/ipp/isep/dei/ui/gui/ListTrainsView.fxml"));
            Parent root = loader.load();

            ListTrainsViewController listCtrl = loader.getController();
            listCtrl.init(player);                    // passa o player

            Stage stage = new Stage();
            stage.setTitle("Player Trains");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showInfo("Could not open Train List.\n" + e.getMessage());
        }
    }

    @FXML
    void handleUC12OperateSimulation(ActionEvent event) {
        if (simCtrl == null || player == null) {
            showInfo("Simulator not initialized.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/pt/ipp/isep/dei/ui/gui/SimulatorStatus.fxml"));
            Parent root = loader.load();
            SimulatorStatusController statusController = loader.getController();
            statusController.init(simCtrl, player);
            Stage stage = new Stage();
            stage.setTitle("Simulation Status");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showInfo("Could not show simulation status.\n" + e.getMessage());
        }
    }

    @FXML
    void handleCancel(ActionEvent event) {
        ((Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow()).close();
    }

    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}