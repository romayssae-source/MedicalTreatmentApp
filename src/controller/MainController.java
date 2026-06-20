package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import util.AlertUtil;

import java.io.IOException;
import java.net.URL;

public class MainController {

    @FXML
    private Button patientsButton;

    @FXML
    private Button treatmentsButton;

    @FXML
    private Button statisticsButton;

    @FXML
    private Button settingsButton;

    @FXML
    private StackPane contentPane;

    @FXML
    private Label statusLabel;

    @FXML
    private MenuItem importItem;

    @FXML
    private MenuItem exportItem;

    @FXML
    private MenuItem quitItem;

    @FXML
    private MenuItem addPatientItem;

    @FXML
    private MenuItem managePatientsItem;

    @FXML
    private MenuItem addTreatmentItem;

    @FXML
    private MenuItem manageTreatmentsItem;

    @FXML
    private MenuItem aboutItem;

    @FXML
    public void initialize() {
        configureTooltips();
        configureActions();
        showWelcomePage();
    }

    private void configureTooltips() {
        patientsButton.setTooltip(new Tooltip("Gérer les patients : ajout, modification, suppression, recherche"));
        treatmentsButton.setTooltip(new Tooltip("Gérer les traitements associés aux patients"));
        statisticsButton.setTooltip(new Tooltip("Consulter les indicateurs et statistiques"));
        settingsButton.setTooltip(new Tooltip("Personnaliser l'apparence de l'application"));
    }

    private void configureActions() {
        patientsButton.setOnAction(event -> loadPage("PatientView.fxml", "Statut : section Patients"));
        treatmentsButton.setOnAction(event -> loadPage("TreatmentView.fxml", "Statut : section Traitements"));
        statisticsButton.setOnAction(event -> loadPage("StatisticsView.fxml", "Statut : section Statistiques"));
        settingsButton.setOnAction(event -> loadPage("SettingsView.fxml", "Statut : section Paramètres"));

        managePatientsItem.setOnAction(event -> loadPage("PatientView.fxml", "Statut : gestion des patients"));
        addPatientItem.setOnAction(event -> loadPage("PatientView.fxml", "Statut : ajout d'un patient"));

        manageTreatmentsItem.setOnAction(event -> loadPage("TreatmentView.fxml", "Statut : gestion des traitements"));
        addTreatmentItem.setOnAction(event -> loadPage("TreatmentView.fxml", "Statut : ajout d'un traitement"));

        importItem.setOnAction(event ->
                AlertUtil.showInfo("Importation", "L'importation de fichiers sera ajoutée comme amélioration.")
        );

        exportItem.setOnAction(event ->
                AlertUtil.showInfo("Export CSV", "Utilisez les boutons Exporter CSV dans les sections Patients ou Traitements.")
        );

        aboutItem.setOnAction(event -> AlertUtil.showAboutDialog());

        quitItem.setOnAction(event -> {
            boolean confirm = AlertUtil.confirm("Quitter", "Voulez-vous vraiment quitter l'application ?");
            if (confirm) {
                Platform.exit();
            }
        });
    }

    private void showWelcomePage() {
        Label title = new Label("Suivi de Traitements Médicaux");
        title.getStyleClass().add("welcome-title");

        Label subtitle = new Label("Application JavaFX avec FXML, CSS, MVC, DAO, JDBC et MySQL");
        subtitle.getStyleClass().add("welcome-subtitle");

        Label info = new Label("Choisissez une section dans le menu latéral.");
        info.getStyleClass().add("welcome-info");

        VBox box = new VBox(15, title, subtitle, info);
        box.setAlignment(Pos.CENTER);

        contentPane.getChildren().setAll(box);
        statusLabel.setText("Statut : application lancée");
    }

    private void loadPage(String fxmlFileName, String statusMessage) {
        try {
            URL pageUrl = getClass().getResource("/resources/fxml/" + fxmlFileName);

            if (pageUrl == null) {
                throw new IllegalStateException("Fichier FXML introuvable : " + fxmlFileName);
            }

            Parent page = FXMLLoader.load(pageUrl);
            contentPane.getChildren().setAll(page);
            statusLabel.setText(statusMessage);

        } catch (IOException e) {
            AlertUtil.showError("Erreur de chargement", "Impossible de charger la page : " + fxmlFileName);
        } catch (IllegalStateException e) {
            AlertUtil.showError("Fichier introuvable", e.getMessage());
        }
    }
}
