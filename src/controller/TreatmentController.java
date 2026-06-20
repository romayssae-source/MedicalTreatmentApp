package controller;

import dao.PatientDAO;
import dao.TreatmentDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Patient;
import model.Treatment;
import util.AlertUtil;
import util.CsvExporter;
import util.Validator;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class TreatmentController {

    @FXML
    private ComboBox<Patient> patientComboBox;

    @FXML
    private TextField nomField;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private TextField posologieField;

    @FXML
    private TextArea effetsArea;

    @FXML
    private DatePicker dateDebutPicker;

    @FXML
    private DatePicker dateFinPicker;

    @FXML
    private CheckBox actifCheckBox;

    @FXML
    private Spinner<Integer> dureeSpinner;

    @FXML
    private Spinner<Integer> prisesSpinner;

    @FXML
    private Slider progressionSlider;

    @FXML
    private Label progressionLabel;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> filterTypeComboBox;

    @FXML
    private CheckBox filterActiveCheckBox;

    @FXML
    private Button addButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button clearButton;

    @FXML
    private Button exportButton;

    @FXML
    private TableView<Treatment> treatmentTable;

    @FXML
    private TableColumn<Treatment, Integer> idColumn;

    @FXML
    private TableColumn<Treatment, String> patientColumn;

    @FXML
    private TableColumn<Treatment, String> nomColumn;

    @FXML
    private TableColumn<Treatment, String> typeColumn;

    @FXML
    private TableColumn<Treatment, String> posologieColumn;

    @FXML
    private TableColumn<Treatment, LocalDate> debutColumn;

    @FXML
    private TableColumn<Treatment, LocalDate> finColumn;

    @FXML
    private TableColumn<Treatment, Boolean> actifColumn;

    @FXML
    private TableColumn<Treatment, Double> progressionColumn;

    @FXML
    private ListView<String> typeListView;

    @FXML
    private ProgressIndicator loadingIndicator;

    private final TreatmentDAO treatmentDAO = new TreatmentDAO();
    private final PatientDAO patientDAO = new PatientDAO();

    private Treatment selectedTreatment;

    @FXML
    public void initialize() {
        configureTable();
        configureControls();
        configureActions();
        loadPatients();
        refreshData();
    }

    private void configureTable() {
        idColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("id"));
        patientColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("patientName"));
        nomColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("nom"));
        typeColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("type"));
        posologieColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("posologie"));
        debutColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("dateDebut"));
        finColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("dateFin"));
        actifColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("actif"));
        progressionColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("progression"));
    }

    private void configureControls() {
        List<String> types = List.of("Antibiotique", "Antalgique", "Anti-inflammatoire", "Cardiologie", "Diabète", "Autre");

        typeComboBox.setItems(FXCollections.observableArrayList(types));
        filterTypeComboBox.setItems(FXCollections.observableArrayList("Tous", "Antibiotique", "Antalgique", "Anti-inflammatoire", "Cardiologie", "Diabète", "Autre"));
        filterTypeComboBox.setValue("Tous");
        typeListView.setItems(FXCollections.observableArrayList(types));

        dateDebutPicker.setValue(LocalDate.now());
        dateFinPicker.setValue(LocalDate.now().plusDays(7));

        actifCheckBox.setSelected(true);

        dureeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 365, 7));
        prisesSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 2));

        progressionSlider.setMin(0);
        progressionSlider.setMax(100);
        progressionSlider.setValue(0);
        progressionSlider.setShowTickLabels(true);
        progressionSlider.setShowTickMarks(true);
        progressionSlider.setMajorTickUnit(25);

        progressionLabel.setText("0 %");
        loadingIndicator.setVisible(false);

        addButton.setTooltip(new Tooltip("Ajouter un traitement"));
        updateButton.setTooltip(new Tooltip("Modifier le traitement sélectionné"));
        deleteButton.setTooltip(new Tooltip("Supprimer le traitement sélectionné"));
        clearButton.setTooltip(new Tooltip("Vider le formulaire"));
        exportButton.setTooltip(new Tooltip("Exporter les traitements en CSV"));
    }

    private void configureActions() {
        progressionSlider.valueProperty().addListener((obs, oldValue, newValue) ->
                progressionLabel.setText(String.format("%.0f %%", newValue.doubleValue()))
        );

        addButton.setOnAction(event -> addTreatment());
        updateButton.setOnAction(event -> updateTreatment());
        deleteButton.setOnAction(event -> deleteTreatment());
        clearButton.setOnAction(event -> clearForm());
        exportButton.setOnAction(event -> exportTreatments());

        treatmentTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    selectedTreatment = newValue;
                    if (newValue != null) {
                        fillForm(newValue);
                    }
                }
        );

        searchField.textProperty().addListener((obs, oldValue, newValue) -> searchTreatments());
        filterTypeComboBox.valueProperty().addListener((obs, oldValue, newValue) -> searchTreatments());
        filterActiveCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> searchTreatments());
    }

    private void loadPatients() {
        try {
            List<Patient> patients = patientDAO.getAllPatients();
            patientComboBox.setItems(FXCollections.observableArrayList(patients));
        } catch (SQLException e) {
            AlertUtil.showError("Erreur chargement patients", e.getMessage());
        }
    }

    private void addTreatment() {
        Treatment treatment = getTreatmentFromForm();

        String error = Validator.validateTreatment(treatment);
        if (error != null) {
            AlertUtil.showError("Erreur de validation", error);
            return;
        }

        try {
            treatmentDAO.addTreatment(treatment);
            AlertUtil.showInfo("Succès", "Traitement ajouté avec succès.");
            clearForm();
            refreshData();
        } catch (SQLException e) {
            AlertUtil.showError("Erreur SQL", e.getMessage());
        }
    }

    private void updateTreatment() {
        if (selectedTreatment == null) {
            AlertUtil.showError("Erreur", "Veuillez sélectionner un traitement à modifier.");
            return;
        }

        Treatment treatment = getTreatmentFromForm();
        treatment.setId(selectedTreatment.getId());

        String error = Validator.validateTreatment(treatment);
        if (error != null) {
            AlertUtil.showError("Erreur de validation", error);
            return;
        }

        try {
            treatmentDAO.updateTreatment(treatment);
            AlertUtil.showInfo("Succès", "Traitement modifié avec succès.");
            clearForm();
            refreshData();
        } catch (SQLException e) {
            AlertUtil.showError("Erreur SQL", e.getMessage());
        }
    }

    private void deleteTreatment() {
        if (selectedTreatment == null) {
            AlertUtil.showError("Erreur", "Veuillez sélectionner un traitement à supprimer.");
            return;
        }

        boolean confirm = AlertUtil.confirm("Confirmation", "Voulez-vous supprimer ce traitement ?");

        if (!confirm) {
            return;
        }

        try {
            treatmentDAO.deleteTreatment(selectedTreatment.getId());
            AlertUtil.showInfo("Succès", "Traitement supprimé avec succès.");
            clearForm();
            refreshData();
        } catch (SQLException e) {
            AlertUtil.showError("Erreur SQL", e.getMessage());
        }
    }

    private void searchTreatments() {
        String keyword = searchField.getText() == null ? "" : searchField.getText();
        String type = filterTypeComboBox.getValue();
        boolean activeOnly = filterActiveCheckBox.isSelected();

        try {
            List<Treatment> treatments = treatmentDAO.searchTreatments(keyword, type, activeOnly);
            treatmentTable.setItems(FXCollections.observableArrayList(treatments));
        } catch (SQLException e) {
            AlertUtil.showError("Erreur recherche", e.getMessage());
        }
    }

    private void refreshData() {
        try {
            loadingIndicator.setVisible(true);
            List<Treatment> treatments = treatmentDAO.getAllTreatments();
            treatmentTable.setItems(FXCollections.observableArrayList(treatments));
        } catch (SQLException e) {
            AlertUtil.showError("Erreur chargement traitements", e.getMessage());
        } finally {
            loadingIndicator.setVisible(false);
        }
    }

    private void exportTreatments() {
        try {
            List<Treatment> treatments = treatmentDAO.getAllTreatments();
            CsvExporter.exportTreatments(treatments, treatmentTable.getScene().getWindow());
        } catch (SQLException e) {
            AlertUtil.showError("Erreur export", e.getMessage());
        }
    }

    private Treatment getTreatmentFromForm() {
        Patient patient = patientComboBox.getValue();

        int patientId = patient != null ? patient.getId() : 0;
        String patientName = patient != null ? patient.getNomComplet() : "";

        return new Treatment(
                patientId,
                patientName,
                nomField.getText(),
                typeComboBox.getValue(),
                posologieField.getText(),
                effetsArea.getText(),
                dateDebutPicker.getValue(),
                dateFinPicker.getValue(),
                actifCheckBox.isSelected(),
                dureeSpinner.getValue(),
                prisesSpinner.getValue(),
                progressionSlider.getValue()
        );
    }

    private void fillForm(Treatment treatment) {
        selectPatientById(treatment.getPatientId());

        nomField.setText(treatment.getNom());
        typeComboBox.setValue(treatment.getType());
        posologieField.setText(treatment.getPosologie());
        effetsArea.setText(treatment.getEffetsSecondaires());
        dateDebutPicker.setValue(treatment.getDateDebut());
        dateFinPicker.setValue(treatment.getDateFin());
        actifCheckBox.setSelected(treatment.isActif());
        dureeSpinner.getValueFactory().setValue(treatment.getDureeEstimee());
        prisesSpinner.getValueFactory().setValue(treatment.getNombrePrisesParJour());
        progressionSlider.setValue(treatment.getProgression());
    }

    private void selectPatientById(int patientId) {
        for (Patient patient : patientComboBox.getItems()) {
            if (patient.getId() == patientId) {
                patientComboBox.setValue(patient);
                return;
            }
        }
    }

    private void clearForm() {
        selectedTreatment = null;
        treatmentTable.getSelectionModel().clearSelection();

        patientComboBox.setValue(null);
        nomField.clear();
        typeComboBox.setValue(null);
        posologieField.clear();
        effetsArea.clear();
        dateDebutPicker.setValue(LocalDate.now());
        dateFinPicker.setValue(LocalDate.now().plusDays(7));
        actifCheckBox.setSelected(true);
        dureeSpinner.getValueFactory().setValue(7);
        prisesSpinner.getValueFactory().setValue(2);
        progressionSlider.setValue(0);
    }
}
