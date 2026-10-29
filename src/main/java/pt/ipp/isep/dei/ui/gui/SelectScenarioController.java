package pt.ipp.isep.dei.ui.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.domain.Scenario;

import java.util.function.Consumer;

public class SelectScenarioController {

    @FXML private ListView<Scenario> list;
    private Consumer<Scenario> onChosen;

    public void init(Map map, Consumer<Scenario> onChosen) {
        this.onChosen = onChosen;
        list.getItems().setAll(map.getScenarios());
    }

    @FXML
    private void handleStart() {
        Scenario s = list.getSelectionModel().getSelectedItem();
        if (s == null) { show("Select a scenario."); return; }
        onChosen.accept(s);
        ((Stage) list.getScene().getWindow()).close();
    }

    private void show(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }
}
