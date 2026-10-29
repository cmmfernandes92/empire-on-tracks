package pt.ipp.isep.dei.ui.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AuthMenuController {

    @FXML
    void handleRegister(ActionEvent event) {
        openScene("RegisterMenu.fxml", "Register");
    }

    @FXML
    void handleLogin(ActionEvent event) {
        openScene("AuthLogin.fxml", "Login");
    }

    private void openScene(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pt/ipp/isep/dei/ui/gui/" + fxmlFile));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
