package pt.ipp.isep.dei.ui.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import pt.ipp.isep.dei.ui.gui.AppContext;
import pt.ipp.isep.dei.controller.LoginController;
import pt.ipp.isep.dei.user.SessionManager;
import pt.ipp.isep.dei.user.User;

public class AuthLoginController {

    private final LoginController loginCtrl = AppContext.LOGIN_CTRL;
    private final SessionManager sessionMgr = new SessionManager();

    @FXML private TextField idField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    void handleLogin() {
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
            new Alert(Alert.AlertType.INFORMATION, "Login successful.").showAndWait();
        }
    }
}

