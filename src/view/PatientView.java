package view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import model.Patient;

import java.time.LocalDate;

public class PatientView {

    private final BorderPane root;

    private final TextField nomField;
    private final TextField prenomField;
    private final DatePicker dateNaissancePicker;
    private final ComboBox<String> sexeComboBox;
    private final TextField telephoneField;
    private final TextArea remarquesArea;
    private final CheckBox surveillanceCheckBox;

    private final TextField searchField;
    private final ComboBox<String> filterSexeComboBox;
    private final CheckBox filterSurveillanceCheckBox;

    private final Button addButton;
    private final Button updateButton;
    private final Button deleteButton;
    private final Button clearButton;
    private final Button exportButton;

    private final TableView<Patient> tableView;

    public PatientView() {
        root = new BorderPane();
        root.setPadding(new Insets(10));

        Label title = new Label("Gestion des Patients");
        title.getStyleClass().add("title");

        nomField = new TextField();
        nomField.setPromptText("Nom");

        prenomField = new TextField();
        prenomField.setPromptText("Prénom");

        dateNaissancePicker = new DatePicker();
        dateNaissancePicker.setValue(LocalDate.now().minusYears(20));

        sexeComboBox = new ComboBox<>();
        sexeComboBox.getItems().addAll("Homme", "Femme");
        sexeComboBox.setPromptText("Sexe");

        telephoneField = new TextField();
        telephoneField.setPromptText("Téléphone");

        remarquesArea = new TextArea();
        remarquesArea.setPromptText("Remarques médicales");
        remarquesArea.setPrefRowCount(3);

        surveillanceCheckBox = new CheckBox("Surveillance active");

        addButton = new Button("Ajouter");
        updateButton = new Button("Modifier");
        deleteButton = new Button("Supprimer");
        clearButton = new Button("Vider");
        exportButton = new Button("Exporter CSV");

        addButton.setTooltip(new Tooltip("Ajouter un nouveau patient"));
        updateButton.setTooltip(new Tooltip("Modifier le patient sélectionné"));
        deleteButton.setTooltip(new Tooltip("Supprimer le patient sélectionné"));
        clearButton.setTooltip(new Tooltip("Vider le formulaire"));
        exportButton.setTooltip(new Tooltip("Exporter la liste des patients"));

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(10));

        formGrid.add(new Label("Nom :"), 0, 0);
        formGrid.add(nomField, 1, 0);
        formGrid.add(new Label("Prénom :"), 0, 1);
        formGrid.add(prenomField, 1, 1);
        formGrid.add(new Label("Date naissance :"), 0, 2);
        formGrid.add(dateNaissancePicker, 1, 2);
        formGrid.add(new Label("Sexe :"), 0, 3);
        formGrid.add(sexeComboBox, 1, 3);
        formGrid.add(new Label("Téléphone :"), 0, 4);
        formGrid.add(telephoneField, 1, 4);
        formGrid.add(new Label("Remarques :"), 0, 5);
        formGrid.add(remarquesArea, 1, 5);
        formGrid.add(surveillanceCheckBox, 1, 6);

        HBox buttonsBox = new HBox(10, addButton, updateButton, deleteButton, clearButton, exportButton);
        buttonsBox.setPadding(new Insets(10));

        VBox formBox = new VBox(formGrid, buttonsBox);

        TitledPane formPane = new TitledPane("Formulaire Patient", formBox);
        formPane.setExpanded(true);

        Accordion accordion = new Accordion(formPane);

        searchField = new TextField();
        searchField.setPromptText("Rechercher par nom, prénom ou téléphone");

        filterSexeComboBox = new ComboBox<>();
        filterSexeComboBox.getItems().addAll("Tous", "Homme", "Femme");
        filterSexeComboBox.setValue("Tous");

        filterSurveillanceCheckBox = new CheckBox("Surveillance seulement");

        HBox searchBox = new HBox(10,
                new Label("Recherche :"), searchField,
                new Label("Sexe :"), filterSexeComboBox,
                filterSurveillanceCheckBox
        );
        searchBox.setPadding(new Insets(10));

        tableView = new TableView<>();

        TableColumn<Patient, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(50);

        TableColumn<Patient, String> nomColumn = new TableColumn<>("Nom");
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        nomColumn.setPrefWidth(120);

        TableColumn<Patient, String> prenomColumn = new TableColumn<>("Prénom");
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        prenomColumn.setPrefWidth(120);

        TableColumn<Patient, LocalDate> birthColumn = new TableColumn<>("Date naissance");
        birthColumn.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        birthColumn.setPrefWidth(130);

        TableColumn<Patient, String> sexeColumn = new TableColumn<>("Sexe");
        sexeColumn.setCellValueFactory(new PropertyValueFactory<>("sexe"));
        sexeColumn.setPrefWidth(90);

        TableColumn<Patient, String> telColumn = new TableColumn<>("Téléphone");
        telColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        telColumn.setPrefWidth(120);

        TableColumn<Patient, Boolean> surveillanceColumn = new TableColumn<>("Surveillance");
        surveillanceColumn.setCellValueFactory(new PropertyValueFactory<>("surveillanceActive"));
        surveillanceColumn.setPrefWidth(120);

        tableView.getColumns().addAll(idColumn, nomColumn, prenomColumn, birthColumn, sexeColumn, telColumn, surveillanceColumn);

        VBox centerBox = new VBox(10, searchBox, tableView);
        centerBox.setPadding(new Insets(10));

        root.setTop(title);
        root.setLeft(accordion);
        root.setCenter(centerBox);
    }

    public BorderPane getRoot() {
        return root;
    }

    public TextField getNomField() {
        return nomField;
    }

    public TextField getPrenomField() {
        return prenomField;
    }

    public DatePicker getDateNaissancePicker() {
        return dateNaissancePicker;
    }

    public ComboBox<String> getSexeComboBox() {
        return sexeComboBox;
    }

    public TextField getTelephoneField() {
        return telephoneField;
    }

    public TextArea getRemarquesArea() {
        return remarquesArea;
    }

    public CheckBox getSurveillanceCheckBox() {
        return surveillanceCheckBox;
    }

    public TextField getSearchField() {
        return searchField;
    }

    public ComboBox<String> getFilterSexeComboBox() {
        return filterSexeComboBox;
    }

    public CheckBox getFilterSurveillanceCheckBox() {
        return filterSurveillanceCheckBox;
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

    public TableView<Patient> getTableView() {
        return tableView;
    }
}
