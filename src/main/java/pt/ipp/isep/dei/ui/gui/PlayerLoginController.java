package pt.ipp.isep.dei.ui.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Node;
import pt.ipp.isep.dei.ui.gui.AppContext;
import pt.ipp.isep.dei.controller.LoginController;
import pt.ipp.isep.dei.user.SessionManager;
import pt.ipp.isep.dei.user.User;

public class PlayerLoginController {

    private final LoginController loginCtrl = AppContext.LOGIN_CTRL;
    private final SessionManager sessionMgr = new SessionManager();

    @FXML private TextField idField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    void handleLogin(ActionEvent event) {
        errorLabel.setText("");
        String id = idField.getText();
        String pwd = passwordField.getText();

        if (id.isBlank() || pwd.isBlank()) {
            errorLabel.setText("Both ID and password are required.");
            return;
        }

        User u = loginCtrl.login(id, pwd);
        if (u == null) {
            errorLabel.setText("Invalid credentials.");
        } else {
            sessionMgr.startSession(u);
            // redirecciona imediatamente para PlayerMenu.fxml
            switchToPlayerMenu(event);
        }
    }

    private void switchToPlayerMenu(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(
                    "/pt/ipp/isep/dei/ui/gui/PlayerMenu.fxml"));
            Stage stage = new Stage(); // mantém esta janela aberta
            stage.setTitle("Player Menu");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Could not open Player Menu.");
        }
    }
}

