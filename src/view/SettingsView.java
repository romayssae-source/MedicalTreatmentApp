package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class SettingsView {

    private final VBox root;

    private final ColorPicker colorPicker;
    private final Button applyColorButton;
    private final Button resetButton;
    private final Label infoLabel;

    public SettingsView() {
        root = new VBox(20);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Paramètres de l'application");
        title.getStyleClass().add("title");

        Label colorLabel = new Label("Choisir une couleur de fond :");

        colorPicker = new ColorPicker(Color.LIGHTBLUE);

        applyColorButton = new Button("Appliquer la couleur");
        resetButton = new Button("Réinitialiser");

        infoLabel = new Label("Vous pouvez personnaliser l'apparence de l'application.");

        root.getChildren().addAll(
                title,
                colorLabel,
                colorPicker,
                applyColorButton,
                resetButton,
                infoLabel
        );
    }

    public VBox getRoot() {
        return root;
    }

    public ColorPicker getColorPicker() {
        return colorPicker;
    }

    public Button getApplyColorButton() {
        return applyColorButton;
    }

    public Button getResetButton() {
        return resetButton;
    }

    public Label getInfoLabel() {
        return infoLabel;
    }
}
