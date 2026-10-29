package pt.ipp.isep.dei.ui.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MainMenuController {
    @FXML
    private VBox rootVBox; //  VBox  fx:id in  FXML

    @FXML
    public void initialize() {
        // Loads logo image from resources
        Image logo = new Image(getClass().getResource("/images/logo.png").toExternalForm());

        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(150);
        logoView.setFitHeight(150);
        logoView.setPreserveRatio(true);

        // Adds the logo at the top of the VBox
        rootVBox.getChildren().add(0, logoView);
    }

    @FXML
    void handleDoLogin(ActionEvent event) {
        openScene("AuthMenu.fxml", "Login Menu");
    }

    @FXML
    void handleDevTeam(ActionEvent event) {
        openScene("DevTeam.fxml", "Development Team");
    }

    @FXML
    void handleEditorRole(ActionEvent event) {
        openScene("EditorMenu.fxml", "Editor Role Menu");
    }

    @FXML
    void handlePlayerRole(ActionEvent event) {
        openScene("PlayerLogin.fxml", "Player Login");
    }

    @FXML
    void handleCancel(ActionEvent event) {
        System.exit(0);
    }

    private void openScene(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/pt/ipp/isep/dei/ui/gui/" + fxmlFile));
            Parent root = loader.load();


            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}