import controller.MainController;
import dao.Database;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.MainView;

import java.net.URL;

public class MedicalTreatmentApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            Database.initializeDatabase();

            MainView mainView = new MainView();
            new MainController(mainView);

            Scene scene = new Scene(mainView.getLayout(), 1200, 750);

            URL cssUrl = getClass().getResource("/resources/style.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }

            primaryStage.setTitle("Suivi de Traitements Médicaux");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur au lancement de l'application : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
