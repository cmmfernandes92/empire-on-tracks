package pt.ipp.isep.dei.ui.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import pt.ipp.isep.dei.controller.AssignTrainToRouteController;
import pt.ipp.isep.dei.domain.CargoType;
import pt.ipp.isep.dei.domain.PointOfRoute;
import pt.ipp.isep.dei.domain.Train;

import java.util.ArrayList;
import java.util.List;

public class RouteBuilderController {

    @FXML private ComboBox<Train>           trainComboBox;
    @FXML private TableView<Row>            porTable;
    @FXML private TableColumn<Row,String>   colOrder;
    @FXML private TableColumn<Row,String>   colStation;
    @FXML private TableColumn<Row,String>   colCargo;
    @FXML private TableColumn<Row,String>   colMode;

    private AssignTrainToRouteController assignTrainToRouteController;
    private final ObservableList<Row> porRows = FXCollections.observableArrayList();

    public void init(AssignTrainToRouteController assignTrainToRouteController) {
        this.assignTrainToRouteController = assignTrainToRouteController;

        trainComboBox.getItems().setAll(assignTrainToRouteController.getOwnedTrains());

        colOrder  .setCellValueFactory(data -> new SimpleStringProperty(data.getValue().order));
        colStation.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().station));
        colCargo  .setCellValueFactory(data -> new SimpleStringProperty(data.getValue().cargo));
        colMode   .setCellValueFactory(data -> new SimpleStringProperty(data.getValue().mode));

        porTable.setItems(porRows);
    }

    @FXML
    private void handleAddStop() {
        Train selectedTrain = trainComboBox.getValue();
        if (selectedTrain == null) {
            showInfo("Select a train first.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/pt/ipp/isep/dei/ui/gui/AddStopView.fxml"));
            Parent root = loader.load();

            AddStopController addStopController = loader.getController();
            addStopController.init(assignTrainToRouteController, this::refreshTable);

            Stage stage = new Stage();
            stage.setTitle("Add Stop");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    private void handleRemoveLast() {
        List<PointOfRoute> currentRoute = assignTrainToRouteController.getCurrentRoute();
        if (!currentRoute.isEmpty()) {
            currentRoute.remove(currentRoute.size() - 1);
        }
        refreshTable();
    }

    @FXML
    private void handleAssign() {
        Train selectedTrain = trainComboBox.getValue();
        if (selectedTrain == null) {
            showInfo("Select a train first.");
            return;
        }

        boolean assigned = assignTrainToRouteController.validateAndAssignRoute(selectedTrain);
        if (assigned) {
            showInfo("Route assigned successfully!");
            closeWindow();
            return;
        }

        if (assignTrainToRouteController.getCurrentRoute().size() < 2) {
            showInfo("A route must have at least two stops.");
        } else if (selectedTrain.getLocomotive().isElectric()) {
            showInfo("⚠️ Cannot assign route: This train is electric and the route contains non-electrified track sections!");
        } else {
            showInfo("Error: could not assign route.");
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void refreshTable() {
        porRows.clear();
        List<PointOfRoute> currentRoute = assignTrainToRouteController.getCurrentRoute();
        for (int index = 0; index < currentRoute.size(); index++) {
            PointOfRoute point = currentRoute.get(index);
            List<String> cargoPieces = new ArrayList<>();
            point.getCargoCarriages().forEach((cargoType, amount) -> cargoPieces.add(cargoType + ":" + amount));
            porRows.add(new Row(
                    String.valueOf(index + 1),
                    point.getStation().getStationName(),
                    String.join(", ", cargoPieces),
                    point.getCargoMode().name()));
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) trainComboBox.getScene().getWindow();
        stage.close();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private record Row(String order, String station, String cargo, String mode) {}
}