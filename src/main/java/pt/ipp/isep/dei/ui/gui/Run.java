package pt.ipp.isep.dei.ui.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Run extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/pt/ipp/isep/dei/ui/gui/MainMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400); // largura e altura ajustáveis conforme preferires
        stage.setTitle("Empire on Tracks - Main Menu");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}