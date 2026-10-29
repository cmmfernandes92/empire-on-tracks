package pt.ipp.isep.dei.ui.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pt.ipp.isep.dei.controller.AddCityController;
import pt.ipp.isep.dei.domain.City;
import pt.ipp.isep.dei.domain.HouseBlock;
import pt.ipp.isep.dei.domain.Map;

import java.util.ArrayList;
import java.util.List;

public class AddCityFormController {

    @FXML private TextField nameField;
    @FXML private TextField xField;
    @FXML private TextField yField;
    @FXML private TextField blocksField;
    @FXML private RadioButton manualRadio;
    @FXML private RadioButton autoRadio;
    @FXML private VBox manualBox;
    @FXML private TextArea coordsArea;
    @FXML private Label messageLabel;

    private AddCityController controller;

    public void init(Map map) {
        this.controller = new AddCityController(map);
        // default: esconder manualBox até escolher manual
        ToggleGroup grp = new ToggleGroup();
        manualRadio.setToggleGroup(grp);
        autoRadio.setToggleGroup(grp);
        autoRadio.setSelected(true);
        manualBox.setVisible(false);

        grp.selectedToggleProperty().addListener((obs, oldT, newT) -> {
            manualBox.setVisible(manualRadio.isSelected());
        });
    }

    @FXML
    private void onAddCity() {
        String name = nameField.getText().trim();
        int x, y, nBlocks;
        try {
            x = Integer.parseInt(xField.getText().trim());
            y = Integer.parseInt(yField.getText().trim());
            nBlocks = Integer.parseInt(blocksField.getText().trim());
        } catch (NumberFormatException e) {
            messageLabel.setText("X, Y e número de blocos têm de ser inteiros.");
            return;
        }

        if (controller.isCityNameDuplicate(name)) {
            messageLabel.setText("Nome de cidade já existe.");
            return;
        }

        List<Integer> manualX = null, manualY = null;
        String method = autoRadio.isSelected() ? "automatic" : "manual";
        if ("manual".equals(method)) {
            manualX = new ArrayList<>();
            manualY = new ArrayList<>();
            String[] lines = coordsArea.getText().split("\\r?\\n");
            if (lines.length != nBlocks) {
                messageLabel.setText("Insira exatamente " + nBlocks + " linhas de coordenadas.");
                return;
            }
            try {
                for (String ln : lines) {
                    String[] parts = ln.trim().split("\\s*,\\s*");
                    manualX.add(Integer.parseInt(parts[0]));
                    manualY.add(Integer.parseInt(parts[1]));
                }
            } catch (Exception ex) {
                messageLabel.setText("Formato de coordenadas inválido. Use X,Y em cada linha.");
                return;
            }
        }

        City city = controller.createCity(
                name, x, y, nBlocks, method, manualX, manualY
        );
        if (city != null) {
            // fecha janela
            ((Stage) nameField.getScene().getWindow()).close();
        } else {
            messageLabel.setText("Erro ao criar cidade.");
        }
    }

    @FXML
    private void onCancel() {
        ((Stage) nameField.getScene().getWindow()).close();
    }
}
