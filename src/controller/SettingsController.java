package controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import util.AlertUtil;

public class SettingsController {

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private ComboBox<String> themeComboBox;

    @FXML
    private CheckBox compactModeCheckBox;

    @FXML
    private Slider fontSizeSlider;

    @FXML
    private Label fontSizeLabel;

    @FXML
    private Button applyButton;

    @FXML
    private Button resetButton;

    @FXML
    private Button aboutButton;

    @FXML
    public void initialize() {
        themeComboBox.getItems().addAll("Clair", "Bleu médical", "Gris professionnel");
        themeComboBox.setValue("Bleu médical");

        colorPicker.setValue(Color.web("#1f3b57"));

        fontSizeSlider.setMin(12);
        fontSizeSlider.setMax(20);
        fontSizeSlider.setValue(14);
        fontSizeSlider.setShowTickLabels(true);
        fontSizeSlider.setShowTickMarks(true);

        fontSizeLabel.setText("14 px");

        fontSizeSlider.valueProperty().addListener((obs, oldValue, newValue) ->
                fontSizeLabel.setText(String.format("%.0f px", newValue.doubleValue()))
        );

        applyButton.setOnAction(event -> applySettings());
        resetButton.setOnAction(event -> resetSettings());
        aboutButton.setOnAction(event -> AlertUtil.showAboutDialog());
    }

    private void applySettings() {
        Scene scene = applyButton.getScene();

        if (scene == null) {
            return;
        }

        Color color = colorPicker.getValue();
        String webColor = toWebColor(color);
        int fontSize = (int) fontSizeSlider.getValue();

        String style = "-fx-font-size: " + fontSize + "px;";

        if (compactModeCheckBox.isSelected()) {
            style += "-fx-padding: 4;";
        }

        scene.getRoot().setStyle(style);

        AlertUtil.showInfo(
                "Paramètres appliqués",
                "Couleur choisie : " + webColor + "\nThème : " + themeComboBox.getValue()
        );
    }

    private void resetSettings() {
        themeComboBox.setValue("Bleu médical");
        colorPicker.setValue(Color.web("#1f3b57"));
        compactModeCheckBox.setSelected(false);
        fontSizeSlider.setValue(14);

        if (resetButton.getScene() != null) {
            resetButton.getScene().getRoot().setStyle("");
        }

        AlertUtil.showInfo("Réinitialisation", "Les paramètres ont été réinitialisés.");
    }

    private String toWebColor(Color color) {
        int r = (int) Math.round(color.getRed() * 255);
        int g = (int) Math.round(color.getGreen() * 255);
        int b = (int) Math.round(color.getBlue() * 255);

        return String.format("#%02X%02X%02X", r, g, b);
    }
}
