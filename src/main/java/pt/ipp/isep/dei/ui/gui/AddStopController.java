package pt.ipp.isep.dei.ui.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import pt.ipp.isep.dei.controller.AssignTrainToRouteController;
import pt.ipp.isep.dei.domain.*;

import java.util.*;
import java.util.Map;

public class AddStopController {

    @FXML private Label          title;
    @FXML private ComboBox<Station> stationComboBox;
    @FXML private ListView<CargoType> cargoListView;
    @FXML private ComboBox<CargoMode> modeComboBox;

    private AssignTrainToRouteController controller;
    private Runnable refreshCallback;

    public void init(AssignTrainToRouteController controller,
                     Runnable refreshCallback) {

        this.controller = controller;
        this.refreshCallback = refreshCallback;

        List<Station> selectable;
        List<PointOfRoute> current = controller.getCurrentRoute();
        if (current.isEmpty()) {
            selectable = controller.getAvailableStations();
            title.setText("Add first stop");
        } else {
            Station last = current.get(current.size() - 1).getStation();
            selectable = controller.getAllReachableStations(last);
            title.setText("Add stop after " + last.getStationName());
        }

        stationComboBox.getItems().setAll(selectable);
        modeComboBox.getItems().setAll(CargoMode.values());

        cargoListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        /* ───── Listener: cada vez que muda a estação, repopula os cargos ───── */
        stationComboBox.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldStation, newStation) -> {
                    if (newStation != null) {
                        List<CargoType> cargos =
                                controller.getAvailableCargoTypesForStation(newStation);
                        cargoListView.getItems().setAll(cargos);
                        cargoListView.getSelectionModel().clearSelection();
                    } else {
                        cargoListView.getItems().clear();
                    }
                });
    }

    @FXML
    private void handleConfirm() {

        Station station = stationComboBox.getValue();
        if (station == null) {
            showInfo("Pick a station.");
            return;
        }

        /* Agora SÓ lemos a seleção, não repovoamos a listView */
        List<CargoType> chosen =
                new ArrayList<>(cargoListView.getSelectionModel().getSelectedItems());
        if (chosen.isEmpty()) {
            showInfo("Select at least one cargo type.");
            return;
        }

        Map<CargoType,Integer> carriagesPerCargo = new HashMap<>();
        for (CargoType ct : chosen) {
            TextInputDialog dlg = new TextInputDialog("1");
            dlg.setHeaderText(null);
            dlg.setContentText("How many carriages for " + ct + "?");
            Optional<String> res = dlg.showAndWait();
            if (res.isEmpty()) return;
            try {
                int num = Integer.parseInt(res.get().trim());
                if (num < 1) throw new NumberFormatException();
                carriagesPerCargo.put(ct, num);
            } catch (NumberFormatException e) {
                showInfo("Invalid number. Stop cancelled.");
                return;
            }
        }

        CargoMode mode = modeComboBox.getValue();
        if (mode == null) {
            showInfo("Select cargo mode.");
            return;
        }

        controller.addPointOfRoute(station, carriagesPerCargo, mode);
        refreshCallback.run();
        close();
    }

    
    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void close() {
        Stage s = (Stage) title.getScene().getWindow();
        s.close();
    }
}
