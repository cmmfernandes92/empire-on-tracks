package pt.ipp.isep.dei.ui.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import pt.ipp.isep.dei.controller.AuthenticationController;
import pt.ipp.isep.dei.controller.LoginController;
import pt.ipp.isep.dei.user.PasswordValidator;
import pt.ipp.isep.dei.user.User;

public class RegisterMenuController {

    private final LoginController loginController = AppContext.LOGIN_CTRL;
    private final PasswordValidator passwordValidator = new PasswordValidator();

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private Label statusLabel;

    @FXML
    void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Limpar mensagens anteriores
        errorLabel.setText("");
        statusLabel.setText("");

        if (username.isBlank() || password.isBlank()) {
            errorLabel.setText("Both fields are required.");
            return;
        }

        if (!passwordValidator.validate(password)) {
            errorLabel.setText("Password must have ≥7 characters, ≥3 uppercase letters, and ≥2 digits.");
            return;
        }

        try {
            User u = loginController.register(username, password);
            statusLabel.setText("Registered successfully! ID = " + u.getId());

            // Copy ID to clipboard
            javafx.scene.input.Clipboard clipboard = javafx.scene.input.Clipboard.getSystemClipboard();
            javafx.scene.input.ClipboardContent content = new javafx.scene.input.ClipboardContent();
            content.putString(u.getId());
            clipboard.setContent(content);

        } catch (Exception e) {
            errorLabel.setText("Failed: " + e.getMessage());
        }
    }
}
