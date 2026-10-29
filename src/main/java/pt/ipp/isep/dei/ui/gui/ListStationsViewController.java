package pt.ipp.isep.dei.ui.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import pt.ipp.isep.dei.controller.ListStationController;
import pt.ipp.isep.dei.domain.Station;
import pt.ipp.isep.dei.repository.Repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListStationsViewController {

    @FXML private TableView<Row> table;
    @FXML private TableColumn<Row,String> colIndex;
    @FXML private TableColumn<Row,String> colName;
    @FXML private TableColumn<Row,String> colLocation;
    @FXML private TableColumn<Row,String> colCargo;

    private ListStationController listStationController;
    private final List<Station> stationCache = new ArrayList<>();

    public void init() {

        listStationController = new ListStationController(
                Repositories.getInstance().getMapRepository().getCurrentMap());

        List<String> stationNameList = listStationController.getAllStations();
        ObservableList<Row> rowObservableList = FXCollections.observableArrayList();

        for (int position = 0; position < stationNameList.size(); position++) {
            Station currentStation = listStationController.getStationDetails(stationNameList.get(position));
            stationCache.add(currentStation);

            String cargoSummary = currentStation.getCargoes().stream()
                    .filter(cargo -> cargo.getQuantity() > 0)
                    .map(cargo -> cargo.getType() + ":" + (int) cargo.getQuantity())
                    .collect(Collectors.joining(", "));

            rowObservableList.add(
                    new Row(
                            String.valueOf(position + 1),
                            currentStation.getStationName(),
                            currentStation.getLocation().toString(),
                            cargoSummary.isEmpty() ? "empty" : cargoSummary
                    )
            );
        }

        colIndex.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().index));
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().name));
        colLocation.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().location));
        colCargo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().cargo));
        table.setItems(rowObservableList);
    }

    @FXML
    private void handleShowDetails() {

        Row selectedRow = table.getSelectionModel().getSelectedItem();
        if (selectedRow == null) {
            return;
        }

        int selectedIndex = Integer.parseInt(selectedRow.index) - 1;
        Station selectedStation = stationCache.get(selectedIndex);

        try {
            FXMLLoader detailsLoader = new FXMLLoader(
                    getClass().getResource("/pt/ipp/isep/dei/ui/gui/StationDetailsView.fxml"));
            Parent detailsRoot = detailsLoader.load();

            StationDetailsViewController stationDetailsController = detailsLoader.getController();
            stationDetailsController.init(selectedStation);

            Stage detailsStage = new Stage();
            detailsStage.setTitle("Station Details");
            detailsStage.setScene(new Scene(detailsRoot));
            detailsStage.show();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    private void handleClose() {
        Stage currentStage = (Stage) table.getScene().getWindow();
        currentStage.close();
    }

    private record Row(String index, String name, String location, String cargo) {}
}
