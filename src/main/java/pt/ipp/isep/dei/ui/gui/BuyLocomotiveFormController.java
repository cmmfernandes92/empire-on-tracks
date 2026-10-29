package pt.ipp.isep.dei.ui.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import pt.ipp.isep.dei.controller.BuyLocomotiveController;
import pt.ipp.isep.dei.domain.Player;
import pt.ipp.isep.dei.domain.Train;
import pt.ipp.isep.dei.mapper.dto.LocomotiveDTO;

import java.util.List;

public class BuyLocomotiveFormController {

    @FXML private TableView<Row> locomotiveTable;
    @FXML private TableColumn<Row,String> colIndex;
    @FXML private TableColumn<Row,String> colId;
    @FXML private TableColumn<Row,String> colCost;
    @FXML private TableColumn<Row,String> colType;
    @FXML private TextField trainNameField;
    @FXML private Label budgetLabel;

    private Player player;
    private BuyLocomotiveController buyLocomotiveController;
    private BudgetRefresh budgetRefresh;
    private List<LocomotiveDTO> locomotiveDTOList;

    public void init(Player player,
                     BuyLocomotiveController buyLocomotiveController,
                     BudgetRefresh budgetRefresh) {

        this.player = player;
        this.buyLocomotiveController = buyLocomotiveController;
        this.budgetRefresh = budgetRefresh;

        locomotiveDTOList = buyLocomotiveController.getAvailableLocomotivesDTO();
        ObservableList<Row> rows = FXCollections.observableArrayList();

        for (int index = 0; index < locomotiveDTOList.size(); index++) {
            LocomotiveDTO dto = locomotiveDTOList.get(index);
            rows.add(new Row(
                    String.valueOf(index + 1),
                    dto.getId(),
                    String.format("$%,.2f", dto.getCost()),
                    dto.getType()));
        }

        colIndex.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().index));
        colId.setCellValueFactory   (d -> new SimpleStringProperty(d.getValue().id));
        colCost.setCellValueFactory (d -> new SimpleStringProperty(d.getValue().cost));
        colType.setCellValueFactory (d -> new SimpleStringProperty(d.getValue().type));

        locomotiveTable.setItems(rows);
        updateBudgetLabel();
    }

    @FXML
    private void handleBuy() {

        Row selectedRow = locomotiveTable.getSelectionModel().getSelectedItem();
        if (selectedRow == null) {
            showInfo("Select a locomotive to buy.");
            return;
        }
        String trainName = trainNameField.getText().trim();
        if (trainName.isBlank()) {
            showInfo("Enter a name for the new train.");
            return;
        }

        int indexSelected = Integer.parseInt(selectedRow.index) - 1;
        LocomotiveDTO selectedDTO = locomotiveDTOList.get(indexSelected);

        int resultCode = buyLocomotiveController
                .buyLocomotiveAsTrain(selectedDTO.getId(), trainName);

        switch (resultCode) {
            case 0 -> {
                Train newTrain = buyLocomotiveController.getPlayer().getTrainByName(trainName);
                showInfo("Successfully purchased locomotive and created train: "
                        + trainName + " with plate number " + newTrain.getPlateNumber() + ".");
                updateBudgetLabel();
                if (budgetRefresh != null) budgetRefresh.refreshBudget();
                close();
            }
            case 1 -> showInfo("Insufficient budget to purchase this locomotive.");
            case 2 -> showInfo("You already have a train with that name. Choose another name.");
            default -> showInfo("Locomotive not found (unexpected error).");
        }
    }

    @FXML
    private void handleCancel() {
        close();
    }

    private void updateBudgetLabel() {
        budgetLabel.setText("Current budget: " + String.format("%,.2f", player.getBudget()));
    }

    private void close() {
        Stage stage = (Stage) locomotiveTable.getScene().getWindow();
        stage.close();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private record Row(String index, String id, String cost, String type) {}
}
