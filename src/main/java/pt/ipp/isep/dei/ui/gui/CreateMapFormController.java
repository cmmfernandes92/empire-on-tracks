package pt.ipp.isep.dei.ui.gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pt.ipp.isep.dei.controller.CreateMapController;
import pt.ipp.isep.dei.mapper.dto.SizeDTO;
import pt.ipp.isep.dei.repository.Repositories;

public class CreateMapFormController {

    @FXML private TextField nameField;
    @FXML private CheckBox usePredefinedCheck;
    @FXML private VBox predefinedBox;
    @FXML private ComboBox<SizeDTO> sizeCombo;
    @FXML private VBox customBox;
    @FXML private TextField widthField;
    @FXML private TextField heightField;
    @FXML private Label messageLabel;

    private CreateMapController ctrl;

    @FXML
    public void initialize() {
        // creates controller instance
        this.ctrl = new CreateMapController();

        // Populates combo with predefined sizes
        var sizes = ctrl.getAvailableSizes();
        sizeCombo.setItems(FXCollections.observableArrayList(sizes));

        // How to show each size DTO with combo "WxH"
        sizeCombo.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(SizeDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null
                        ? null
                        : item.getWidth() + " x " + item.getHeight());
            }
        });
        sizeCombo.setButtonCell(sizeCombo.getCellFactory().call(null));

        // Listener to alternate between predefinedBox/customBox
        usePredefinedCheck.selectedProperty().addListener((obs, was, isNow) -> {
            predefinedBox.setVisible(isNow);
            predefinedBox.setManaged(isNow);
            customBox.setVisible(!isNow);
            customBox.setManaged(!isNow);
        });
    }

    @FXML
    private void onCreateMap() {
        messageLabel.setText("");

        String name = nameField.getText().trim();
        boolean usePre = usePredefinedCheck.isSelected();

        int width, height;
        if (usePre) {
            var dto = sizeCombo.getValue();
            if (dto == null) {
                messageLabel.setText("Select a predefined size");
                return;
            }
            width = dto.getWidth();
            height = dto.getHeight();
        } else {
            try {
                width = Integer.parseInt(widthField.getText().trim());
                height = Integer.parseInt(heightField.getText().trim());
            } catch (NumberFormatException e) {
                messageLabel.setText("Width and height must be an integer");
                return;
            }
        }

        if (!ctrl.validateDimensions(width, height)) {
            messageLabel.setText("Dimensions must be positive");
            return;
        }
        if (!ctrl.validateMapName(name)) {
            messageLabel.setText("Empty name or it already exists");
            return;
        }

        // Creates and saves
        var draft = ctrl.createMapDraft(name, width, height);
        ctrl.saveMap(draft);

        // Closes
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onCancel() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}
