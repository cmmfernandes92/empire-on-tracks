package pt.ipp.isep.dei.ui.gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.repository.Repositories;

import java.io.IOException;
import java.util.List;

public class EditMapController {

    @FXML private ListView<String> mapListView;
    @FXML private Label statusLabel;

    private List<Map> availableMaps;

    @FXML
    public void initialize() {
        availableMaps = Repositories.getInstance().getMapRepository().getAllMaps();
        List<String> mapNames = availableMaps.stream().map(Map::getName).toList();
        mapListView.setItems(FXCollections.observableArrayList(mapNames));
    }

    @FXML
    private void onEditSelected() {
        int selectedIndex = mapListView.getSelectionModel().getSelectedIndex();

        if (selectedIndex < 0) {
            statusLabel.setText("Please select a map to edit.");
            return;
        }

        Map selectedMap = availableMaps.get(selectedIndex);
        Repositories.getInstance().getMapRepository().setCurrentMap(selectedMap);
        AppContext.getInstance().setCurrentMap(selectedMap);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pt/ipp/isep/dei/ui/gui/MapEditMenu.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) mapListView.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            statusLabel.setText("Failed to open the Map Edit Menu.");
            e.printStackTrace();
        }
    }

    @FXML
    private void onCancel() {
        Stage stage = (Stage) mapListView.getScene().getWindow();
        stage.close();
    }
}

