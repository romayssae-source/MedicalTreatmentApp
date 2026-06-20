import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.MainView;

public class MedicalTreatmentApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        MainView mainView = new MainView();

        Scene scene = new Scene(mainView.getLayout(), 1200, 750);

        primaryStage.setTitle("Suivi de Traitements Médicaux");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
