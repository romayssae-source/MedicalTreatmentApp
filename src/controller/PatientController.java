package controller;

import dao.PatientDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Patient;
import util.AlertUtil;
import util.CsvExporter;
import util.Validator;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PatientController {

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private DatePicker dateNaissancePicker;

    @FXML
    private RadioButton hommeRadio;

    @FXML
    private RadioButton femmeRadio;

    @FXML
    private ToggleGroup sexeGroup;

    @FXML
    private TextField telephoneField;

    @FXML
    private TextArea remarquesArea;

    @FXML
    private CheckBox surveillanceCheckBox;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> filterSexeComboBox;

    @FXML
    private CheckBox filterSurveillanceCheckBox;

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
    private TableView<Patient> patientTable;

    @FXML
    private TableColumn<Patient, Integer> idColumn;

    @FXML
    private TableColumn<Patient, String> nomColumn;

    @FXML
    private TableColumn<Patient, String> prenomColumn;

    @FXML
    private TableColumn<Patient, LocalDate> dateColumn;

    @FXML
    private TableColumn<Patient, String> sexeColumn;

    @FXML
    private TableColumn<Patient, String> telephoneColumn;

    @FXML
    private TableColumn<Patient, Boolean> surveillanceColumn;

    @FXML
    private ListView<String> patientListView;

    @FXML
    private ProgressIndicator loadingIndicator;

    private final PatientDAO patientDAO = new PatientDAO();
    private Patient selectedPatient;

    @FXML
    public void initialize() {
        configureTable();
        configureControls();
        configureActions();
        refreshData();
    }

    private void configureTable() {
        idColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("prenom"));
        dateColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("dateNaissance"));
        sexeColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("sexe"));
        telephoneColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("telephone"));
        surveillanceColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("surveillanceActive"));
    }

    private void configureControls() {
        hommeRadio.setUserData("Homme");
        femmeRadio.setUserData("Femme");

        filterSexeComboBox.setItems(FXCollections.observableArrayList("Tous", "Homme", "Femme"));
        filterSexeComboBox.setValue("Tous");

        dateNaissancePicker.setValue(LocalDate.now().minusYears(20));

        loadingIndicator.setVisible(false);

        addButton.setTooltip(new Tooltip("Ajouter un patient"));
        updateButton.setTooltip(new Tooltip("Modifier le patient sélectionné"));
        deleteButton.setTooltip(new Tooltip("Supprimer le patient sélectionné"));
        clearButton.setTooltip(new Tooltip("Vider le formulaire"));
        exportButton.setTooltip(new Tooltip("Exporter les patients en CSV"));
    }

    private void configureActions() {
        addButton.setOnAction(event -> addPatient());
        updateButton.setOnAction(event -> updatePatient());
        deleteButton.setOnAction(event -> deletePatient());
        clearButton.setOnAction(event -> clearForm());
        exportButton.setOnAction(event -> exportPatients());

        patientTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    selectedPatient = newValue;
                    if (newValue != null) {
                        fillForm(newValue);
                    }
                }
        );

        searchField.textProperty().addListener((obs, oldValue, newValue) -> searchPatients());
        filterSexeComboBox.valueProperty().addListener((obs, oldValue, newValue) -> searchPatients());
        filterSurveillanceCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> searchPatients());
    }

    private void addPatient() {
        Patient patient = getPatientFromForm();

        String error = Validator.validatePatient(patient);
        if (error != null) {
            AlertUtil.showError("Erreur de validation", error);
            return;
        }

        try {
            patientDAO.addPatient(patient);
            AlertUtil.showInfo("Succès", "Patient ajouté avec succès.");
            clearForm();
            refreshData();
        } catch (SQLException e) {
            AlertUtil.showError("Erreur SQL", e.getMessage());
        }
    }

    private void updatePatient() {
        if (selectedPatient == null) {
            AlertUtil.showError("Erreur", "Veuillez sélectionner un patient à modifier.");
            return;
        }

        Patient patient = getPatientFromForm();
        patient.setId(selectedPatient.getId());

        String error = Validator.validatePatient(patient);
        if (error != null) {
            AlertUtil.showError("Erreur de validation", error);
            return;
        }

        try {
            patientDAO.updatePatient(patient);
            AlertUtil.showInfo("Succès", "Patient modifié avec succès.");
            clearForm();
            refreshData();
        } catch (SQLException e) {
            AlertUtil.showError("Erreur SQL", e.getMessage());
        }
    }

    private void deletePatient() {
        if (selectedPatient == null) {
            AlertUtil.showError("Erreur", "Veuillez sélectionner un patient à supprimer.");
            return;
        }

        boolean confirm = AlertUtil.confirm(
                "Confirmation",
                "Voulez-vous supprimer ce patient ? Les traitements associés seront aussi supprimés."
        );

        if (!confirm) {
            return;
        }

        try {
            patientDAO.deletePatient(selectedPatient.getId());
            AlertUtil.showInfo("Succès", "Patient supprimé avec succès.");
            clearForm();
            refreshData();
        } catch (SQLException e) {
            AlertUtil.showError("Erreur SQL", e.getMessage());
        }
    }

    private void searchPatients() {
        String keyword = searchField.getText() == null ? "" : searchField.getText();
        String sexe = filterSexeComboBox.getValue();
        boolean surveillanceOnly = filterSurveillanceCheckBox.isSelected();

        try {
            List<Patient> patients = patientDAO.searchPatients(keyword, sexe, surveillanceOnly);
            patientTable.setItems(FXCollections.observableArrayList(patients));
            updateListView(patients);
        } catch (SQLException e) {
            AlertUtil.showError("Erreur recherche", e.getMessage());
        }
    }

    private void refreshData() {
        try {
            loadingIndicator.setVisible(true);
            List<Patient> patients = patientDAO.getAllPatients();
            patientTable.setItems(FXCollections.observableArrayList(patients));
            updateListView(patients);
        } catch (SQLException e) {
            AlertUtil.showError("Erreur chargement", e.getMessage());
        } finally {
            loadingIndicator.setVisible(false);
        }
    }

    private void updateListView(List<Patient> patients) {
        List<String> names = new ArrayList<>();
        for (Patient patient : patients) {
            names.add(patient.getNomComplet());
        }

        patientListView.setItems(FXCollections.observableArrayList(names));
    }

    private void exportPatients() {
        try {
            List<Patient> patients = patientDAO.getAllPatients();
            CsvExporter.exportPatients(patients, patientTable.getScene().getWindow());
        } catch (SQLException e) {
            AlertUtil.showError("Erreur export", e.getMessage());
        }
    }

    private Patient getPatientFromForm() {
        String sexe = "";

        if (sexeGroup.getSelectedToggle() != null) {
            sexe = sexeGroup.getSelectedToggle().getUserData().toString();
        }

        return new Patient(
                nomField.getText(),
                prenomField.getText(),
                dateNaissancePicker.getValue(),
                sexe,
                telephoneField.getText(),
                remarquesArea.getText(),
                surveillanceCheckBox.isSelected()
        );
    }

    private void fillForm(Patient patient) {
        nomField.setText(patient.getNom());
        prenomField.setText(patient.getPrenom());
        dateNaissancePicker.setValue(patient.getDateNaissance());
        telephoneField.setText(patient.getTelephone());
        remarquesArea.setText(patient.getRemarques());
        surveillanceCheckBox.setSelected(patient.isSurveillanceActive());

        if ("Homme".equals(patient.getSexe())) {
            hommeRadio.setSelected(true);
        } else if ("Femme".equals(patient.getSexe())) {
            femmeRadio.setSelected(true);
        }
    }

    private void clearForm() {
        selectedPatient = null;
        patientTable.getSelectionModel().clearSelection();

        nomField.clear();
        prenomField.clear();
        dateNaissancePicker.setValue(LocalDate.now().minusYears(20));
        sexeGroup.selectToggle(null);
        telephoneField.clear();
        remarquesArea.clear();
        surveillanceCheckBox.setSelected(false);
    }
}
