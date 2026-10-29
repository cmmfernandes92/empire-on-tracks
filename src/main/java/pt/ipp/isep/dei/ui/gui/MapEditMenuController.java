package pt.ipp.isep.dei.ui.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.repository.Repositories;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class MapEditMenuController {

    @FXML
    private Label editingLabel;

    private Map map;     // map being edited



    @FXML
    public void initialize() {
        map = AppContext.getInstance().getCurrentMap();
        if (map != null)
            editingLabel.setText("Editing map: " + map.getName());
        else
            editingLabel.setText("No map selected.");
    }



    @FXML private void onAddIndustry() { loadScene("AddIndustry.fxml"); }

    @FXML
    private void onAddCity() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/pt/ipp/isep/dei/ui/gui/AddCityForm.fxml"));
            Parent root = loader.load();
            AddCityFormController c = loader.getController();
            Map current = Repositories.getInstance().getMapRepository().getCurrentMap();
            c.init(current);

            Stage dialog = new Stage();
            dialog.setTitle("Add City");
            dialog.setScene(new Scene(root));
            dialog.initOwner(editingLabel.getScene().getWindow());
            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void onCreateScenario() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/pt/ipp/isep/dei/ui/gui/ScenarioInfoDialog.fxml"));
            Parent root = loader.load();

            Stage dialog = new Stage();
            dialog.setTitle("Create Scenario");
            dialog.setScene(new Scene(root));
            dialog.setResizable(false);
            dialog.initOwner(editingLabel.getScene().getWindow());
            dialog.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void onSaveCurrentMap() {
        // let's serialize the current map to a .map file under a "saved-maps" folder
        Map map = AppContext.getInstance().getCurrentMap();
        File dir = new File(System.getProperty("user.dir"), "saved-maps");
        dir.mkdirs();
        String filename = map.getName() + ".map";
        File out = new File(dir, filename);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(out))) {
            oos.writeObject(map);
            showInfo("Map saved to:\n" + out.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            showInfo("Error saving map:\n" + e.getMessage());
        }
    }

    @FXML private void onBackToEditor() { loadScene("EditorMenu.fxml"); }
    @FXML private void onCancel()       { loadScene("MainMenu.fxml");   }



    private void loadScene(String fxmlName) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/pt/ipp/isep/dei/ui/gui/" + fxmlName));
            Stage stage = (Stage) editingLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }
    private void alert(Alert.AlertType type, String msg) {
        Alert a = new Alert(type);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}