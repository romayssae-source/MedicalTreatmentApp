import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MedicalTreatmentApp extends Application {

    @Override
    public void start(Stage stage) {
        Label label = new Label("Application de Suivi de Traitements Médicaux");
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Medical Treatment App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch()
