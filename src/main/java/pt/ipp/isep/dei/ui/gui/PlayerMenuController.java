package pt.ipp.isep.dei.ui.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.Node;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pt.ipp.isep.dei.controller.AssignTrainToRouteController;
import pt.ipp.isep.dei.controller.SimulatorController;
import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.repository.*;
import pt.ipp.isep.dei.user.User;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class PlayerMenuController {

    private static final String SAVE_DIR =
            System.getProperty("user.dir") + File.separator +
                    "src" + File.separator + "main" + File.separator +
                    "saved-simulations";


    @FXML
    void handleStartSimulation(ActionEvent event) {
        User user = ApplicationSession.getInstance().getCurrentUser();
        if (user == null) {
            showError("You must be logged in to start a simulation.");
            return;
        }
        openScene("SelectMap.fxml", "Select Map to Play", event);
    }

    @FXML
    void handleLoadSimulation(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select saved simulation (.sim)");
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Simulation files", "*.sim"));
        File initialDir = new File(SAVE_DIR);
        if (initialDir.exists()) fileChooser.setInitialDirectory(initialDir);

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File chosenFile = fileChooser.showOpenDialog(currentStage);
        if (chosenFile == null) return;

        try {
            SimulationSnapshot snapshot =
                    SimulatorController.loadFromFile(chosenFile.getAbsolutePath());

            Map map           = snapshot.getMap();
            Time clock        = snapshot.getClock();
            Player player     = snapshot.getPlayer();
            Scenario scenario = snapshot.getScenario();
            player.setOwnedTrains(snapshot.getTrains());

            SimulatorController simCtrl;
            simCtrl = new SimulatorController(
                    map,
                    clock,
                    IndustryRepository.getInstance(),
                    LocomotiveRepository.getInstance(),
                    player, scenario);
            simCtrl.setSelectedScenario(scenario);

            AssignTrainToRouteController routeCtrl =
                    new AssignTrainToRouteController(
                            player,
                            map,
                            map.getStations(),
                            List.of(CargoType.values()));

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/pt/ipp/isep/dei/ui/gui/SimulatorMenu.fxml"));
            Parent root = loader.load();
            SimulatorMenuController menuController = loader.getController();
            menuController.init(simCtrl, player, routeCtrl);

            Stage stage = new Stage();
            stage.setTitle("Simulator – " + chosenFile.getName());
            stage.setScene(new Scene(root));
            stage.show();

            showInfo("Simulation loaded successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            showError("Could not load simulation:\n" + e.getMessage());
        }
    }

    @FXML
    void handleSwitchToEditor(ActionEvent event) {
        openScene("EditorMenu.fxml", "Editor Menu", event);
    }

    @FXML
    void handleCancel(ActionEvent event) {
        openScene("MainMenu.fxml", "Main Menu", event);
    }



    private void openScene(String fxmlFile, String title, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/pt/ipp/isep/dei/ui/gui/" + fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
