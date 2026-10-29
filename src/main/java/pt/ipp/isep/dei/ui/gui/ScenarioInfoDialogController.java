package pt.ipp.isep.dei.ui.gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pt.ipp.isep.dei.controller.CreateScenarioController;
import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.mapper.dto.IndustryDTO1;

import java.util.ArrayList;
import java.util.List;

public class ScenarioInfoDialogController {

    @FXML private TextField nameField;
    @FXML private TextField startYearField;
    @FXML private TextField endYearField;

    @FXML private VBox locoBox;

    @FXML private ComboBox<IndustryDTO1> industryCombo;
    @FXML private TextField rateField;

    @FXML private ComboBox<IndustryDTO1> mixedCombo;
    @FXML private ComboBox<MixedBehavior> behaviourCombo;
    @FXML private ComboBox<CargoType> inputCargoCombo;
    @FXML private ComboBox<CargoType> outputCargoCombo;

    @FXML private Label messageLabel;

    private CreateScenarioController controller;
    private final List<CheckBox> locoChecks = new ArrayList<>();

    @FXML
    private void initialize() {
        Map map = AppContext.getInstance().getCurrentMap();
        controller = new CreateScenarioController(map);

        controller.getLocomotiveTypeNames().forEach(t -> {
            CheckBox cb = new CheckBox(t);
            locoBox.getChildren().add(cb);
            locoChecks.add(cb);
        });

        industryCombo.setItems(
                FXCollections.observableArrayList(controller.getIndustryDTOs()));
        mixedCombo.setItems(
                FXCollections.observableArrayList(controller.getMixedIndustryDTOs()));

        behaviourCombo.setItems(
                FXCollections.observableArrayList(MixedBehavior.values()));

        inputCargoCombo.setItems(
                FXCollections.observableArrayList(CargoType.values()));
        outputCargoCombo.setItems(
                FXCollections.observableArrayList(CargoType.values()));
    }

    @FXML
    private void onAddRate() {
        IndustryDTO1 dto = industryCombo.getValue();
        if (dto == null) {
            warn("Select industry.");
            return;
        }
        try {
            double m = Double.parseDouble(rateField.getText().trim());
            controller.addProductionRateChange(dto.getIndex(), m);
            rateField.clear();
            info("Rate added.");
        } catch (Exception ex) {
            warn("Invalid multiplier.");
        }
    }

    @FXML
    private void onAddMixed() {
        IndustryDTO1 dto = mixedCombo.getValue();
        MixedBehavior beh = behaviourCombo.getValue();
        if (dto == null || beh == null) {
            warn("Choose industry and behaviour.");
            return;
        }
        CargoType in  = inputCargoCombo.getValue();
        CargoType out = outputCargoCombo.getValue();

        if (beh == MixedBehavior.IMPORT)  in  = null;
        if (beh == MixedBehavior.EXPORT)  out = null;

        controller.addMixedIndustryBehavior(dto.getIndex(), beh, in, out);
        info("Mixed behaviour added.");
    }

    @FXML
    private void onConfirm() {
        String name = nameField.getText().trim();
        int s, e;
        try {
            s = Integer.parseInt(startYearField.getText().trim());
            e = Integer.parseInt(endYearField.getText().trim());
        } catch (Exception ex) {
            warn("Years must be numbers.");
            return;
        }
        if (name.isBlank() || !controller.validateYears(s, e)) {
            warn("Fill all fields with valid data.");
            return;
        }
        controller.setScenarioInfo(name, s, e);

        locoChecks.stream().filter(CheckBox::isSelected)
                .forEach(cb -> controller.addLocomotiveType(cb.getText()));

        controller.createScenario();
        close();
    }

    @FXML
    private void onCancel() {
        close();
    }

    private void warn(String m) { messageLabel.setText(m); }
    private void info(String m) { messageLabel.setText(m); }

    private void close() {
        Stage st = (Stage) nameField.getScene().getWindow();
        st.close();
    }
}