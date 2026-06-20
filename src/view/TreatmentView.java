package view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import model.Patient;
import model.Treatment;

import java.time.LocalDate;

public class TreatmentView {

    private final BorderPane root;

    private final ComboBox<Patient> patientComboBox;
    private final TextField nomField;
    private final ComboBox<String> typeComboBox;
    private final TextField posologieField;
    private final TextArea effetsArea;
    private final DatePicker dateDebutPicker;
    private final DatePicker dateFinPicker;
    private final CheckBox actifCheckBox;
    private final Spinner<Integer> dureeSpinner;
    private final Spinner<Integer> prisesSpinner;
    private final Slider progressionSlider;

    private final TextField searchField;
    private final ComboBox<String> filterTypeComboBox;
    private final CheckBox filterActiveCheckBox;

    private final Button addButton;
    private final Button updateButton;
    private final Button deleteButton;
    private final Button clearButton;
    private final Button exportButton;

    private final TableView<Treatment> tableView;

    public TreatmentView() {
        root = new BorderPane();
        root.setPadding(new Insets(10));

        Label title = new Label("Gestion des Traitements Médicaux");
        title.getStyleClass().add("title");

        patientComboBox = new ComboBox<>();
        patientComboBox.setPromptText("Choisir un patient");

        nomField = new TextField();
        nomField.setPromptText("Nom du traitement");

        typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("Antibiotique", "Antalgique", "Anti-inflammatoire", "Cardiologie", "Diabète", "Autre");
        typeComboBox.setPromptText("Type");

        posologieField = new TextField();
        posologieField.setPromptText("Ex : 1 comprimé matin et soir");

        effetsArea = new TextArea();
        effetsArea.setPromptText("Effets secondaires possibles");
        effetsArea.setPrefRowCount(3);

        dateDebutPicker = new DatePicker(LocalDate.now());
        dateFinPicker = new DatePicker(LocalDate.now().plusDays(7));

        actifCheckBox = new CheckBox("Traitement actif");
        actifCheckBox.setSelected(true);

        dureeSpinner = new Spinner<>(1, 365, 7);
        prisesSpinner = new Spinner<>(1, 10, 2);

        progressionSlider = new Slider(0, 100, 0);
        progressionSlider.setShowTickLabels(true);
        progressionSlider.setShowTickMarks(true);
        progressionSlider.setMajorTickUnit(25);

        addButton = new Button("Ajouter");
        updateButton = new Button("Modifier");
        deleteButton = new Button("Supprimer");
        clearButton = new Button("Vider");
        exportButton = new Button("Exporter CSV");

        addButton.setTooltip(new Tooltip("Ajouter un nouveau traitement"));
        updateButton.setTooltip(new Tooltip("Modifier le traitement sélectionné"));
        deleteButton.setTooltip(new Tooltip("Supprimer le traitement sélectionné"));
        clearButton.setTooltip(new Tooltip("Vider le formulaire"));
        exportButton.setTooltip(new Tooltip("Exporter les traitements"));

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(10));

        formGrid.add(new Label("Patient :"), 0, 0);
        formGrid.add(patientComboBox, 1, 0);
        formGrid.add(new Label("Traitement :"), 0, 1);
        formGrid.add(nomField, 1, 1);
        formGrid.add(new Label("Type :"), 0, 2);
        formGrid.add(typeComboBox, 1, 2);
        formGrid.add(new Label("Posologie :"), 0, 3);
        formGrid.add(posologieField, 1, 3);
        formGrid.add(new Label("Effets secondaires :"), 0, 4);
        formGrid.add(effetsArea, 1, 4);
        formGrid.add(new Label("Date début :"), 0, 5);
        formGrid.add(dateDebutPicker, 1, 5);
        formGrid.add(new Label("Date fin :"), 0, 6);
        formGrid.add(dateFinPicker, 1, 6);
        formGrid.add(new Label("Durée estimée :"), 0, 7);
        formGrid.add(dureeSpinner, 1, 7);
        formGrid.add(new Label("Prises / jour :"), 0, 8);
        formGrid.add(prisesSpinner, 1, 8);
        formGrid.add(new Label("Progression :"), 0, 9);
        formGrid.add(progressionSlider, 1, 9);
        formGrid.add(actifCheckBox, 1, 10);

        HBox buttonsBox = new HBox(10, addButton, updateButton, deleteButton, clearButton, exportButton);
        buttonsBox.setPadding(new Insets(10));

        VBox formBox = new VBox(formGrid, buttonsBox);

        TitledPane formPane = new TitledPane("Formulaire Traitement", formBox);
        formPane.setExpanded(true);

        Accordion accordion = new Accordion(formPane);

        searchField = new TextField();
        searchField.setPromptText("Rechercher traitement ou patient");

        filterTypeComboBox = new ComboBox<>();
        filterTypeComboBox.getItems().addAll("Tous", "Antibiotique", "Antalgique", "Anti-inflammatoire", "Cardiologie", "Diabète", "Autre");
        filterTypeComboBox.setValue("Tous");

        filterActiveCheckBox = new CheckBox("Actifs seulement");

        HBox searchBox = new HBox(10,
                new Label("Recherche :"), searchField,
                new Label("Type :"), filterTypeComboBox,
                filterActiveCheckBox
        );
        searchBox.setPadding(new Insets(10));

        tableView = new TableView<>();

        TableColumn<Treatment, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(50);

        TableColumn<Treatment, String> patientColumn = new TableColumn<>("Patient");
        patientColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        patientColumn.setPrefWidth(150);

        TableColumn<Treatment, String> nomColumn = new TableColumn<>("Traitement");
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        nomColumn.setPrefWidth(140);

        TableColumn<Treatment, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeColumn.setPrefWidth(130);

        TableColumn<Treatment, String> posologieColumn = new TableColumn<>("Posologie");
        posologieColumn.setCellValueFactory(new PropertyValueFactory<>("posologie"));
        posologieColumn.setPrefWidth(180);

        TableColumn<Treatment, LocalDate> debutColumn = new TableColumn<>("Début");
        debutColumn.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        debutColumn.setPrefWidth(100);

        TableColumn<Treatment, LocalDate> finColumn = new TableColumn<>("Fin");
        finColumn.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        finColumn.setPrefWidth(100);

        TableColumn<Treatment, Boolean> actifColumn = new TableColumn<>("Actif");
        actifColumn.setCellValueFactory(new PropertyValueFactory<>("actif"));
        actifColumn.setPrefWidth(80);

        TableColumn<Treatment, Double> progressionColumn = new TableColumn<>("Progression");
        progressionColumn.setCellValueFactory(new PropertyValueFactory<>("progression"));
        progressionColumn.setPrefWidth(100);

        tableView.getColumns().addAll(idColumn, patientColumn, nomColumn, typeColumn, posologieColumn,
                debutColumn, finColumn, actifColumn, progressionColumn);

        VBox centerBox = new VBox(10, searchBox, tableView);
        centerBox.setPadding(new Insets(10));

        root.setTop(title);
        root.setLeft(accordion);
        root.setCenter(centerBox);
    }

    public BorderPane getRoot() {
        return root;
    }

    public ComboBox<Patient> getPatientComboBox() {
        return patientComboBox;
    }

    public TextField getNomField() {
        return nomField;
    }

    public ComboBox<String> getTypeComboBox() {
        return typeComboBox;
    }

    public TextField getPosologieField() {
        return posologieField;
    }

    public TextArea getEffetsArea() {
        return effetsArea;
    }

    public DatePicker getDateDebutPicker() {
        return dateDebutPicker;
    }

    public DatePicker getDateFinPicker() {
        return dateFinPicker;
    }

    public CheckBox getActifCheckBox() {
        return actifCheckBox;
    }

    public Spinner<Integer> getDureeSpinner() {
        return dureeSpinner;
    }

    public Spinner<Integer> getPrisesSpinner() {
        return prisesSpinner;
    }

    public Slider getProgressionSlider() {
        return progressionSlider;
    }

    public TextField getSearchField() {
        return searchField;
    }

    public ComboBox<String> getFilterTypeComboBox() {
        return filterTypeComboBox;
    }

    public CheckBox getFilterActiveCheckBox() {
        return filterActiveCheckBox;
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getUpdateButton() {
        return updateButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public Button getClearButton() {
        return clearButton;
    }

    public Button getExportButton() {
        return exportButton;
    }

    public TableView<Treatment> getTableView() {
        return tableView;
    }
}
