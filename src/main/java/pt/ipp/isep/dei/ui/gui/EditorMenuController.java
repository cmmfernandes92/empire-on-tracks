package pt.ipp.isep.dei.ui.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class EditorMenuController {

    @FXML
    private Button createMapButton;

    private void loadScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pt/ipp/isep/dei/ui/gui/" + fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) createMapButton.getScene().getWindow(); // Usa o botão para obter a Stage
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCreateNewMap(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/pt/ipp/isep/dei/ui/gui/CreateMapForm.fxml")
            );
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Create Map");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showInfo("Could not open Create Map form.\n" + e.getMessage());
        }
    }

    @FXML
    private void handleEditExistingMap() {
        loadScene("SelectMaptoEdit.fxml");
    }

    @FXML
    private void handleSwitchToPlayerMenu() {
        loadScene("PlayerMenu.fxml"); // Menu com UC5–UC12
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) createMapButton.getScene().getWindow();
        stage.close(); // Fecha a janela atual
    }

    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}

