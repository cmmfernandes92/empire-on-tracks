package pt.ipp.isep.dei.ui.gui;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import pt.ipp.isep.dei.controller.AddIndustryController;
import pt.ipp.isep.dei.mapper.dto.IndustryDTO;
import pt.ipp.isep.dei.repository.Repositories;
import pt.ipp.isep.dei.domain.Map;

import java.util.List;

public class AddIndustryFormController {

    @FXML private TableView<IndustryDTO> templateTable;
    @FXML private TableColumn<IndustryDTO, Integer> colIdx;
    @FXML private TableColumn<IndustryDTO, String>  colName;
    @FXML private TableColumn<IndustryDTO, String>  colCons;
    @FXML private TableColumn<IndustryDTO, String>  colProd;

    @FXML private TextField xField;
    @FXML private TextField yField;
    @FXML private Label     msgLabel;

    private AddIndustryController ctrl;
    private List<IndustryDTO> templates;

    @FXML
    private void initialize() {
        Map map = AppContext.getInstance().getCurrentMap();
        ctrl = new AddIndustryController(Repositories.getInstance(), map);

        templates = ctrl.getIndustryTypeList();

        colIdx.setCellValueFactory(d ->
                new SimpleIntegerProperty(d.getValue().getIndex()).asObject()
        );
        colName.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getName()));

        /* Formata “consumes”               ex.: “WHEAT:40”  ou “–” */
        colCons.setCellValueFactory(d -> {
            String type = d.getValue().getConsumesType();
            String txt  = type.equals("–")
                    ? "–"
                    : type + ":" + (int) d.getValue().getConsumesQty();
            return new SimpleStringProperty(txt);
        });

        /* Formata “produces”               ex.: “STEEL:20”  */
        colProd.setCellValueFactory(d -> {
            String type = d.getValue().getProducesType();
            String txt  = type + ":" + (int) d.getValue().getProducesQty();
            return new SimpleStringProperty(txt);
        });

        templateTable.setItems(FXCollections.observableArrayList(templates));
    }

    @FXML
    private void onAdd() {
        msgLabel.setText("");

        IndustryDTO dto = templateTable.getSelectionModel().getSelectedItem();
        if (dto == null) {
            msgLabel.setText("Select a template.");
            return;
        }

        int x, y;
        try {
            x = Integer.parseInt(xField.getText().trim());
            y = Integer.parseInt(yField.getText().trim());
        } catch (NumberFormatException e) {
            msgLabel.setText("Coordinates must be integers.");
            return;
        }

        if (!ctrl.isValidPosition(x, y)) {
            msgLabel.setText("Invalid / occupied position.");
            return;
        }

        if (ctrl.addIndustry(dto, x, y)) {
            close();
        } else {
            msgLabel.setText("Error adding industry.");
        }
    }

    @FXML
    private void onCancel() {
        close();
    }

    private void close() {
        Stage s = (Stage) templateTable.getScene().getWindow();
        s.close();
    }
}