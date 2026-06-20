package view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class MainView {

    private final BorderPane root;
    private final StackPane contentPane;
    private final Label statusLabel;

    private final Button patientsButton;
    private final Button treatmentsButton;
    private final Button statisticsButton;
    private final Button settingsButton;

    private final MenuItem importItem;
    private final MenuItem exportItem;
    private final MenuItem quitItem;
    private final MenuItem addPatientItem;
    private final MenuItem managePatientsItem;
    private final MenuItem addTreatmentItem;
    private final MenuItem manageTreatmentsItem;
    private final MenuItem aboutItem;

    public MainView() {
        root = new BorderPane();

        MenuBar menuBar = new MenuBar();

        Menu fichierMenu = new Menu("Fichier");
        importItem = new MenuItem("Importer");
        exportItem = new MenuItem("Exporter");
        quitItem = new MenuItem("Quitter");
        fichierMenu.getItems().addAll(importItem, exportItem, new SeparatorMenuItem(), quitItem);

        Menu patientsMenu = new Menu("Patients");
        addPatientItem = new MenuItem("Ajouter un patient");
        managePatientsItem = new MenuItem("Gérer les patients");
        patientsMenu.getItems().addAll(addPatientItem, managePatientsItem);

        Menu treatmentsMenu = new Menu("Traitements");
        addTreatmentItem = new MenuItem("Ajouter un traitement");
        manageTreatmentsItem = new MenuItem("Gérer les traitements");
        treatmentsMenu.getItems().addAll(addTreatmentItem, manageTreatmentsItem);

        Menu aideMenu = new Menu("Aide");
        aboutItem = new MenuItem("A propos");
        aideMenu.getItems().add(aboutItem);

        menuBar.getMenus().addAll(fichierMenu, patientsMenu, treatmentsMenu, aideMenu);

        patientsButton = new Button("Patients");
        treatmentsButton = new Button("Traitements");
        statisticsButton = new Button("Statistiques");
        settingsButton = new Button("Paramètres");

        patientsButton.setTooltip(new Tooltip("Gérer les informations des patients"));
        treatmentsButton.setTooltip(new Tooltip("Gérer les traitements médicaux"));
        statisticsButton.setTooltip(new Tooltip("Voir les statistiques de l'application"));
        settingsButton.setTooltip(new Tooltip("Personnaliser les paramètres"));

        patientsButton.setMaxWidth(Double.MAX_VALUE);
        treatmentsButton.setMaxWidth(Double.MAX_VALUE);
        statisticsButton.setMaxWidth(Double.MAX_VALUE);
        settingsButton.setMaxWidth(Double.MAX_VALUE);

        VBox navigation = new VBox(15, patientsButton, treatmentsButton, statisticsButton, settingsButton);
        navigation.setPadding(new Insets(20));
        navigation.setPrefWidth(190);
        navigation.getStyleClass().add("navigation");

        contentPane = new StackPane();
        contentPane.setPadding(new Insets(15));

        statusLabel = new Label("Statut");
        HBox statusBar = new HBox(statusLabel);
        statusBar.setPadding(new Insets(8));
        statusBar.getStyleClass().add("status-bar");

        root.setTop(menuBar);
        root.setLeft(navigation);
        root.setCenter(contentPane);
        root.setBottom(statusBar);
    }

    public BorderPane getLayout() {
        return root;
    }

    public StackPane getContentPane() {
        return contentPane;
    }

    public Label getStatusLabel() {
        return statusLabel;
    }

    public Button getPatientsButton() {
        return patientsButton;
    }

    public Button getTreatmentsButton() {
        return treatmentsButton;
    }

    public Button getStatisticsButton() {
        return statisticsButton;
    }

    public Button getSettingsButton() {
        return settingsButton;
    }

    public MenuItem getImportItem() {
        return importItem;
    }

    public MenuItem getExportItem() {
        return exportItem;
    }

    public MenuItem getQuitItem() {
        return quitItem;
    }

    public MenuItem getAddPatientItem() {
        return addPatientItem;
    }

    public MenuItem getManagePatientsItem() {
        return managePatientsItem;
    }

    public MenuItem getAddTreatmentItem() {
        return addTreatmentItem;
    }

    public MenuItem getManageTreatmentsItem() {
        return manageTreatmentsItem;
    }

    public MenuItem getAboutItem() {
        return aboutItem;
    }
}
