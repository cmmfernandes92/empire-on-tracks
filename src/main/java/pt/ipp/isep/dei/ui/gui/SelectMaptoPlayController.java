package pt.ipp.isep.dei.ui.gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import pt.ipp.isep.dei.controller.AssignTrainToRouteController;
import pt.ipp.isep.dei.controller.SimulatorController;
import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.repository.*;
import pt.ipp.isep.dei.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class SelectMaptoPlayController {

    @FXML private ListView<String> mapListView;

    private List<Map> availableMaps;

    @FXML
    private void initialize() {
        MapRepository mapRepo = Repositories.getInstance().getMapRepository();
        availableMaps = mapRepo.getAllMaps();
        mapListView.setItems(FXCollections.observableArrayList(
                availableMaps.stream().map(Map::getName).collect(Collectors.toList())
        ));
    }

    @FXML
    private void handleStart() {

        int idx = mapListView.getSelectionModel().getSelectedIndex();
        if (idx < 0) {
            alert(Alert.AlertType.WARNING, "Please select a map first.");
            return;
        }

        User current = ApplicationSession.getInstance().getCurrentUser();
        if (current == null) {
            alert(Alert.AlertType.ERROR, "No user is logged in. Log in first.");
            return;
        }

        Map selectedMap = availableMaps.get(idx);
        Repositories.getInstance().getMapRepository().setCurrentMap(selectedMap);
        AppContext.getInstance().setCurrentMap(selectedMap);

        List<Scenario> scenarios = selectedMap.getScenarios();
        if (scenarios.isEmpty()) {
            alert(Alert.AlertType.ERROR, "No scenarios available for this map.");
            return;
        }

        ChoiceDialog<Scenario> dialog = new ChoiceDialog<>(scenarios.get(0), scenarios);
        dialog.setTitle("Select Scenario");
        dialog.setHeaderText("Select a scenario to run:");
        dialog.setContentText("Scenario:");
        dialog.showAndWait();
        Scenario selectedScenario = dialog.getResult();

        if (selectedScenario == null) {
            alert(Alert.AlertType.WARNING, "No scenario selected.");
            return;
        }

        int startYear = selectedScenario.getPeriod().getStartDate();
        int endYear   = selectedScenario.getPeriod().getEndDate();

        LocalDateTime start = LocalDateTime.of(startYear, 1, 1, 0, 0);
        LocalDateTime end   = LocalDateTime.of(endYear, 12, 31, 23, 59);
        Time clock = new Time(start, end);

        Player player = new Player(5_000_000);

        SimulatorController simCtrl = new SimulatorController(
                selectedMap,
                clock,
                IndustryRepository.getInstance(),
                LocomotiveRepository.getInstance(),
                player,
                selectedScenario
        );

        AssignTrainToRouteController routeCtrl = new AssignTrainToRouteController(
                player,
                selectedMap,
                selectedMap.getStations(),
                List.of(CargoType.values())
        );

        simCtrl.startSimulation(30L);
        openSimulatorUI(simCtrl, player, routeCtrl);
    }

    @FXML
    private void handleCancel() {
        ((Stage) mapListView.getScene().getWindow()).close();
    }

    private void openSimulatorUI(SimulatorController sim,
                                 Player p,
                                 AssignTrainToRouteController routeCtrl) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/pt/ipp/isep/dei/ui/gui/SimulatorMenu.fxml"));
            Parent root = loader.load();

            SimulatorMenuController controller = loader.getController();
            controller.init(sim, p, routeCtrl);

            Stage stage = new Stage();
            stage.setTitle("Simulation");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            alert(Alert.AlertType.ERROR, "Could not open Simulator GUI:\n" + e.getMessage());
        }
    }

    private void alert(Alert.AlertType type, String msg) {
        Alert a = new Alert(type);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}