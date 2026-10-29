package pt.ipp.isep.dei.ui.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import pt.ipp.isep.dei.controller.ListTrainsController;
import pt.ipp.isep.dei.domain.Player;
import pt.ipp.isep.dei.domain.Train;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ListTrainsViewController {

    @FXML private TableView<Row>            table;
    @FXML private TableColumn<Row,String>   colType;
    @FXML private TableColumn<Row,String>   colName;
    @FXML private TableColumn<Row,String>   colCargo;

    public void init(Player player) {

        ListTrainsController listCtrl = new ListTrainsController(player);
        List<Train> trains = listCtrl.requestTrainList();

        ObservableList<Row> rows = FXCollections.observableArrayList();

        trains.stream()
                .sorted(Comparator.comparing(Train::getLocomotiveType)
                        .thenComparing(Train::getName))
                .forEach(t -> rows.add(new Row(
                        t.getLocomotiveType(),
                        t.getName(),
                        t.getCurrentCargoes().isEmpty()
                                ? "None"
                                : t.getCurrentCargoes().stream()
                                .collect(Collectors.joining(", "))
                )));

        colType.setCellValueFactory (d -> new SimpleStringProperty(d.getValue().type));
        colName.setCellValueFactory (d -> new SimpleStringProperty(d.getValue().name));
        colCargo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().cargo));

        table.setItems(rows);
    }

    @FXML
    private void handleClose() {
        Stage s = (Stage) table.getScene().getWindow();
        s.close();
    }

    private record Row(String type, String name, String cargo) {}
}
