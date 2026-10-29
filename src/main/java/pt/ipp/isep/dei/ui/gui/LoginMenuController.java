package pt.ipp.isep.dei.ui.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import pt.ipp.isep.dei.controller.LoginController;
import pt.ipp.isep.dei.user.SessionManager;
import pt.ipp.isep.dei.user.User;

public class LoginMenuController {

    private final LoginController loginController = AppContext.LOGIN_CTRL;
    private final SessionManager sessionManager = new SessionManager();

    @FXML
    private TextField idField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    void handleLogin() {
        String id = idField.getText();
        String password = passwordField.getText();

        errorLabel.setText(""); // limpar mensagens anteriores
        errorLabel.setStyle("-fx-text-fill: red;");

        if (id.isBlank() || password.isBlank()) {
            errorLabel.setText("Both ID and password are required.");
            return;
        }

        User user = loginController.login(id, password);
        if (user == null) {
            errorLabel.setText("Invalid credentials. Please try again.");
        } else {
            sessionManager.startSession(user);
            errorLabel.setStyle("-fx-text-fill: green;");
            errorLabel.setText("Welcome, " + user.getUsername() + "!");
        }
    }
}
