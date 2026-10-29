package pt.ipp.isep.dei.ui.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;

public class DevTeamController {

    @FXML
    void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pt/ipp/isep/dei/ui/gui/MainMenu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Empire on Tracks - Main Menu");
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
