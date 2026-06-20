package controller;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import util.AlertUtil;
import view.MainView;
import view.SettingsView;

public class MainController {

    private final MainView mainView;

    private final PatientView patientView;
    private final TreatmentView treatmentView;
    private final StatisticsView statisticsView;
    private final SettingsView settingsView;

    private final PatientController patientController;
    private final TreatmentController treatmentController;

    private String currentSection = "patients";

    public MainController(MainView mainView) {
        this.mainView = mainView;

        this.patientView = new PatientView();
        this.treatmentView = new TreatmentView();
        this.statisticsView = new StatisticsView();
        this.settingsView = new SettingsView();

        this.patientController = new PatientController(patientView);
        this.treatmentController = new TreatmentController(treatmentView);

        initializeActions();
        showPatients();
    }

    private void initializeActions() {
        mainView.getPatientsButton().setOnAction(e -> showPatients());
        mainView.getTreatmentsButton().setOnAction(e -> showTreatments());
        mainView.getStatisticsButton().setOnAction(e -> showStatistics());
        mainView.getSettingsButton().setOnAction(e -> showSettings());

        mainView.getManagePatientsItem().setOnAction(e -> showPatients());
        mainView.getAddPatientItem().setOnAction(e -> {
            showPatients();
            patientView.getNomField().requestFocus();
        });

        mainView.getManageTreatmentsItem().setOnAction(e -> showTreatments());
        mainView.getAddTreatmentItem().setOnAction(e -> {
            showTreatments();
            treatmentView.getNomField().requestFocus();
        });

        mainView.getQuitItem().setOnAction(e -> Platform.exit());

        mainView.getImportItem().setOnAction(e ->
                AlertUtil.showInfo("Importer", "La fonctionnalité d'import sera ajoutée plus tard.")
        );

        mainView.getExportItem().setOnAction(e -> {
            if ("patients".equals(currentSection)) {
                patientController.exportPatients();
            } else if ("treatments".equals(currentSection)) {
                treatmentController.exportTreatments();
            } else {
                AlertUtil.showInfo("Export", "Allez dans Patients ou Traitements pour exporter les données.");
            }
        });

        mainView.getAboutItem().setOnAction(e ->
                AlertUtil.showInfo(
                        "À propos",
                        "Application de Suivi de Traitements Médicaux\n" +
                                "Mini-projet JavaFX - ENSAO GI3\n" +
                                "Technologies : JavaFX, JDBC, MySQL"
                )
        );

        settingsView.getApplyColorButton().setOnAction(e -> applySelectedColor());
        settingsView.getResetButton().setOnAction(e -> {
            mainView.getLayout().setStyle("");
            settingsView.getInfoLabel().setText("Style réinitialisé.");
            mainView.getStatusLabel().setText("Statut : paramètres réinitialisés");
        });
    }

    private void showPatients() {
        currentSection = "patients";
        patientController.refreshData();
        mainView.getContentPane().getChildren().setAll(patientView.getRoot());
        mainView.getStatusLabel().setText("Statut : section Patients");
    }

    private void showTreatments() {
        currentSection = "treatments";
        treatmentController.refreshData();
        mainView.getContentPane().getChildren().setAll(treatmentView.getRoot());
        mainView.getStatusLabel().setText("Statut : section Traitements");
    }

    private void showStatistics() {
        currentSection = "statistics";
        statisticsView.refresh();
        mainView.getContentPane().getChildren().setAll(statisticsView.getRoot());
        mainView.getStatusLabel().setText("Statut : section Statistiques");
    }

    private void showSettings() {
        currentSection = "settings";
        mainView.getContentPane().getChildren().setAll(settingsView.getRoot());
        mainView.getStatusLabel().setText("Statut : section Paramètres");
    }

    private void applySelectedColor() {
        Color color = settingsView.getColorPicker().getValue();
        String webColor = toWebColor(color);

        mainView.getLayout().setStyle("-fx-background-color: " + webColor + ";");
        settingsView.getInfoLabel().setText("Couleur appliquée : " + webColor);
        mainView.getStatusLabel().setText("Statut : couleur personnalisée appliquée");
    }

    private String toWebColor(Color color) {
        int r = (int) Math.round(color.getRed() * 255);
        int g = (int) Math.round(color.getGreen() * 255);
        int b = (int) Math.round(color.getBlue() * 255);

        return String.format("#%02X%02X%02X", r, g, b);
    }
}
