package pt.ipp.isep.dei.ui.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import pt.ipp.isep.dei.controller.AssignTrainToRouteController;
import pt.ipp.isep.dei.controller.SimulatorController;
import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.domain.Player;
import pt.ipp.isep.dei.domain.Station;
import pt.ipp.isep.dei.repository.Repositories;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SimulatorMenuController implements BudgetRefresh {

    /* ──────────────────────────── FXML ───────────────────────────── */

    @FXML private Label    dateLabel;
    @FXML private Label    budgetLabel;
    @FXML private TextArea eventLog;

    /* ──────────────────────────── state ──────────────────────────── */

    private SimulatorController               simCtrl;
    private Player                            player;
    private AssignTrainToRouteController      routeCtrl;

    private final ScheduledExecutorService guiScheduler =
            Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> guiTask;

    private static final String SAVE_DIR =
            System.getProperty("user.dir") + File.separator +
                    "src" + File.separator + "main" + File.separator +
                    "saved-simulations";

    /* ───────────────────────── init/header ───────────────────────── */

    public void init(SimulatorController simCtrl,
                     Player player,
                     AssignTrainToRouteController routeCtrl) {
        this.simCtrl   = simCtrl;
        this.player    = player;
        this.routeCtrl = routeCtrl;
        updateHeader();
    }

    @Override
    public void refreshBudget() { updateHeader(); }

    private void updateHeader() {
        if (dateLabel != null && simCtrl != null)
            dateLabel.setText("Date: " +
                    simCtrl.getCurrentDate()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        if (budgetLabel != null && player != null)
            budgetLabel.setText(String.format("Budget: $%,.2f", player.getBudget()));
    }

    /* ───────────────────────── simulation ctrl ───────────────────── */

    @FXML
    void handleStartAuto(ActionEvent event) {
        if (simCtrl == null) { showInfo("Simulator not initialized."); return; }

        if (guiTask != null && !guiTask.isCancelled()) return;    // já em execução

        guiTask = guiScheduler.scheduleAtFixedRate(() -> {
            List<String> ev = simCtrl.stepWithEvents();           // avança 1 mês
            Platform.runLater(() -> {
                ev.forEach(line -> eventLog.appendText(line + "\n"));
                updateHeader();
            });
        }, 0, 5, TimeUnit.SECONDS);                               // 5 s ≈ 1 mês

        showInfo("Automatic simulation started.");
    }

    @FXML
    void handlePauseAuto(ActionEvent event) {
        if (simCtrl == null) { showInfo("Simulator not initialized."); return; }

        if (guiTask != null) guiTask.cancel(false);
        guiTask = null;

        showInfo("Simulation paused.");
        updateHeader();
    }

    @FXML
    void handleAdvanceOneMonth(ActionEvent event) {
        if (simCtrl == null) { showInfo("Simulator not initialized."); return; }

        List<String> events = simCtrl.stepWithEvents();
        events.forEach(e -> eventLog.appendText(e + "\n"));
        updateHeader();

        showInfo(events.isEmpty()
                ? "One month passed. No events."
                : "One month passed.\nEvents:\n" + String.join("\n", events));
    }

    /* ───────────────────────── status / edit ─────────────────────── */

    @FXML
    void handleStatus(ActionEvent event) {
        if (simCtrl == null || player == null) {
            showInfo("Simulator not initialized.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/pt/ipp/isep/dei/ui/gui/SimulatorStatus.fxml"));
            Parent root = loader.load();
            SimulatorStatusController controller = loader.getController();
            controller.init(simCtrl, player);

            Stage stage = new Stage();
            stage.setTitle("Simulation Status");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showInfo("Could not show simulation status.\n" + e.getMessage());
        }
    }

    @FXML
    void handleEdit(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/pt/ipp/isep/dei/ui/gui/EditSimulationMenu.fxml"));
            Parent root = loader.load();

            Map currentMap = Repositories.getInstance().getMapRepository().getCurrentMap();
            if (currentMap == null) { showInfo("No map is currently loaded."); return; }

            List<Station> stationList = currentMap.getStations();
            EditSimulationMenuController controller = loader.getController();
            controller.init(player, stationList, this, simCtrl);

            Stage stage = new Stage();
            stage.setTitle("Edit Simulation Menu");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showInfo("Could not open the Edit Simulation Menu.\n" + e.getMessage());
        }
    }

    /* ───────────────────────── save / help / exit ────────────────── */

    @FXML
    void handleSaveSimulation(ActionEvent event) {
        if (simCtrl == null) { showInfo("Simulator not initialized."); return; }

        TextInputDialog dialog = new TextInputDialog("mysave");
        dialog.setHeaderText(null);
        dialog.setContentText("File name:");
        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty()) return;

        String fileName = result.get().trim();
        if (fileName.isBlank()) { showInfo("Invalid name."); return; }

        File dir = new File(SAVE_DIR);
        dir.mkdirs();

        String path = SAVE_DIR + File.separator + fileName + ".sim";
        simCtrl.saveToFile(path);
        showInfo("Simulation saved at:\n" + path);
    }

    @FXML
    void handleHelp(ActionEvent event) {
        showInfo("""
                1 - Start automatic simulation
                2 - Pause simulation
                3 - Advance manually
                4 - Show status
                5 - Edit (UC5–UC12)
                6 - Select scenario
                7 - Help
                8 - Exit
                0 - Cancel
                """);
    }

    @FXML
    void handleExit(ActionEvent event) {
        showInfo("Exiting simulation.");
    }

    @FXML
    void handleCancel(ActionEvent event) {
        if (guiTask != null) guiTask.cancel(false);
        guiScheduler.shutdownNow();
        ((Stage) ((javafx.scene.Node) event.getSource())
                .getScene().getWindow()).close();
    }

    /* ───────────────────────── util ──────────────────────────────── */

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}