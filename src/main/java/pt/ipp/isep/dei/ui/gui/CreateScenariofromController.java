package pt.ipp.isep.dei.ui.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import pt.ipp.isep.dei.controller.CreateScenarioController;
import pt.ipp.isep.dei.domain.Map;

import java.io.IOException;

public class CreateScenariofromController {

    @FXML private TextField nameField;
    @FXML private TextField startYearField;
    @FXML private TextField endYearField;
    @FXML private Label messageLabel;
    @FXML private Button finalizeButton;

    private CreateScenarioController controller;

    public void initialize() {
        Map map = AppContext.getInstance().getCurrentMap();
        controller = new CreateScenarioController(map);
    }

    @FXML
    private void onSelectLocomotiveTypes() {
        loadScene("AvailableLocomotivesScenario.fxml");
    }

    @FXML
    private void onSetProductionRates() {
        loadScene("SetProductionRateScenario.fxml");
    }

    @FXML
    private void onSetMixedBehaviours() {
        loadScene("MixedIndustriesBehaviourScenario.fxml");
    }

    @FXML
    private void onFinalizeScenario() {
        String name = nameField.getText().trim();
        int start, end;

        try {
            start = Integer.parseInt(startYearField.getText().trim());
            end = Integer.parseInt(endYearField.getText().trim());
        } catch (NumberFormatException e) {
            messageLabel.setText("Start and end year must be valid integers.");
            return;
        }

        if (!controller.validateYears(start, end)) {
            messageLabel.setText("Invalid years. Start must be >= 1804 and <= End.");
            return;
        }

        controller.setScenarioInfo(name, start, end);
        controller.createScenario();
        messageLabel.setText("Scenario '" + name + "' created successfully!");
        messageLabel.setVisible(true);
    }

    private void loadScene(String fxmlName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pt/ipp/isep/dei/ui/gui/" + fxmlName));
            Parent root = loader.load();
            Stage stage = (Stage) finalizeButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Error loading scene: " + fxmlName);
        }
    }
}
