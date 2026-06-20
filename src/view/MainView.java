package view;

import controller.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;

public class MainView {

    private BorderPane layout;
    private MainController controller;

    public MainView() {
        loadView();
    }

    private void loadView() {
        try {
            URL fxmlUrl = getClass().getResource("/resources/fxml/MainView.fxml");

            if (fxmlUrl == null) {
                throw new IllegalStateException("Fichier MainView.fxml introuvable dans /resources/fxml/");
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            layout = loader.load();
            controller = loader.getController();

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du chargement de MainView.fxml", e);
        }
    }

    public BorderPane getLayout() {
        return layout;
    }

    public MainController getController() {
        return controller;
    }
}
