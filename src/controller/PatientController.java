package controller;

import dao.PatientDAO;
import javafx.collections.FXCollections;
import model.Patient;
import util.AlertUtil;
import util.CsvExporter;
import util.Validator;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PatientController {

    private final PatientView view;
    private final PatientDAO patientDAO;
    private Patient selectedPatient;

    public PatientController(PatientView view) {
        this.view = view;
        this.patientDAO = new PatientDAO();

        initializeActions();
        refreshData();
    }

    private void initializeActions() {
        view.getAddButton().setOnAction(e -> addPatient());
        view.getUpdateButton().setOnAction(e -> updatePatient());
        view.getDeleteButton().setOnAction(e -> deletePatient());
        view.getClearButton().setOnAction(e -> clearForm());
        view.getExportButton().setOnAction(e -> exportPatients());

        view.getTableView().getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    selectedPatient = newValue;
                    if (newValue != null) {
                        fillForm(newValue);
                    }
                }
        );

        view.getSearchField().textProperty().addListener((obs, oldVal, newVal) -> searchPatients());
        view.getFilterSexeComboBox().valueProperty().addListener((obs, oldVal, newVal) -> searchPatients());
        view.getFilterSurveillanceCheckBox().selectedProperty().addListener((obs, oldVal, newVal) -> searchPatients());
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
                "Voulez-vous vraiment supprimer ce patient ?\n" +
                        "Tous ses traitements seront aussi supprimés."
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
        String keyword = view.getSearchField().getText();
        String sexe = view.getFilterSexeComboBox().getValue();
        boolean surveillanceOnly = view.getFilterSurveillanceCheckBox().isSelected();

        if (keyword == null) {
            keyword = "";
        }

        try {
            List<Patient> patients = patientDAO.searchPatients(keyword, sexe, surveillanceOnly);
            view.getTableView().setItems(FXCollections.observableArrayList(patients));
        } catch (SQLException e) {
            AlertUtil.showError("Erreur recherche", e.getMessage());
        }
    }

    public void refreshData() {
        try {
            List<Patient> patients = patientDAO.getAllPatients();
            view.getTableView().setItems(FXCollections.observableArrayList(patients));
        } catch (SQLException e) {
            AlertUtil.showError("Erreur chargement", e.getMessage());
        }
    }

    public void exportPatients() {
        try {
            List<Patient> patients = patientDAO.getAllPatients();

            if (view.getRoot().getScene() == null) {
                AlertUtil.showError("Erreur export", "La fenêtre n'est pas encore prête.");
                return;
            }

            CsvExporter.exportPatients(patients, view.getRoot().getScene().getWindow());

        } catch (SQLException e) {
            AlertUtil.showError("Erreur export", e.getMessage());
        }
    }

    private Patient getPatientFromForm() {
        String nom = view.getNomField().getText();
        String prenom = view.getPrenomField().getText();
        LocalDate dateNaissance = view.getDateNaissancePicker().getValue();
        String sexe = view.getSexeComboBox().getValue();
        String telephone = view.getTelephoneField().getText();
        String remarques = view.getRemarquesArea().getText();
        boolean surveillance = view.getSurveillanceCheckBox().isSelected();

        return new Patient(nom, prenom, dateNaissance, sexe, telephone, remarques, surveillance);
    }

    private void fillForm(Patient patient) {
        view.getNomField().setText(patient.getNom());
        view.getPrenomField().setText(patient.getPrenom());
        view.getDateNaissancePicker().setValue(patient.getDateNaissance());
        view.getSexeComboBox().setValue(patient.getSexe());
        view.getTelephoneField().setText(patient.getTelephone());
        view.getRemarquesArea().setText(patient.getRemarques());
        view.getSurveillanceCheckBox().setSelected(patient.isSurveillanceActive());
    }

    private void clearForm() {
        selectedPatient = null;

        view.getTableView().getSelectionModel().clearSelection();

        view.getNomField().clear();
        view.getPrenomField().clear();
        view.getDateNaissancePicker().setValue(LocalDate.now().minusYears(20));
        view.getSexeComboBox().setValue(null);
        view.getTelephoneField().clear();
        view.getRemarquesArea().clear();
        view.getSurveillanceCheckBox().setSelected(false);
    }
}
