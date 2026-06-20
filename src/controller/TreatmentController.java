package controller;

import dao.PatientDAO;
import dao.TreatmentDAO;
import javafx.collections.FXCollections;
import model.Patient;
import model.Treatment;
import util.AlertUtil;
import util.CsvExporter;
import util.Validator;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class TreatmentController {

    private final TreatmentView view;
    private final TreatmentDAO treatmentDAO;
    private final PatientDAO patientDAO;

    private Treatment selectedTreatment;

    public TreatmentController(TreatmentView view) {
        this.view = view;
        this.treatmentDAO = new TreatmentDAO();
        this.patientDAO = new PatientDAO();

        initializeActions();
        refreshData();
    }

    private void initializeActions() {
        view.getAddButton().setOnAction(e -> addTreatment());
        view.getUpdateButton().setOnAction(e -> updateTreatment());
        view.getDeleteButton().setOnAction(e -> deleteTreatment());
        view.getClearButton().setOnAction(e -> clearForm());
        view.getExportButton().setOnAction(e -> exportTreatments());

        view.getTableView().getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    selectedTreatment = newValue;
                    if (newValue != null) {
                        fillForm(newValue);
                    }
                }
        );

        view.getSearchField().textProperty().addListener((obs, oldVal, newVal) -> searchTreatments());
        view.getFilterTypeComboBox().valueProperty().addListener((obs, oldVal, newVal) -> searchTreatments());
        view.getFilterActiveCheckBox().selectedProperty().addListener((obs, oldVal, newVal) -> searchTreatments());
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

        boolean confirm = AlertUtil.confirm(
                "Confirmation",
                "Voulez-vous vraiment supprimer ce traitement ?"
        );

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
        String keyword = view.getSearchField().getText();
        String type = view.getFilterTypeComboBox().getValue();
        boolean activeOnly = view.getFilterActiveCheckBox().isSelected();

        if (keyword == null) {
            keyword = "";
        }

        try {
            List<Treatment> treatments = treatmentDAO.searchTreatments(keyword, type, activeOnly);
            view.getTableView().setItems(FXCollections.observableArrayList(treatments));
        } catch (SQLException e) {
            AlertUtil.showError("Erreur recherche", e.getMessage());
        }
    }

    public void refreshData() {
        loadPatients();

        try {
            List<Treatment> treatments = treatmentDAO.getAllTreatments();
            view.getTableView().setItems(FXCollections.observableArrayList(treatments));
        } catch (SQLException e) {
            AlertUtil.showError("Erreur chargement traitements", e.getMessage());
        }
    }

    private void loadPatients() {
        try {
            List<Patient> patients = patientDAO.getAllPatients();
            view.getPatientComboBox().setItems(FXCollections.observableArrayList(patients));
        } catch (SQLException e) {
            AlertUtil.showError("Erreur chargement patients", e.getMessage());
        }
    }

    public void exportTreatments() {
        try {
            List<Treatment> treatments = treatmentDAO.getAllTreatments();

            if (view.getRoot().getScene() == null) {
                AlertUtil.showError("Erreur export", "La fenêtre n'est pas encore prête.");
                return;
            }

            CsvExporter.exportTreatments(treatments, view.getRoot().getScene().getWindow());

        } catch (SQLException e) {
            AlertUtil.showError("Erreur export", e.getMessage());
        }
    }

    private Treatment getTreatmentFromForm() {
        Patient patient = view.getPatientComboBox().getValue();

        int patientId = patient != null ? patient.getId() : 0;
        String patientName = patient != null ? patient.getNomComplet() : "";

        String nom = view.getNomField().getText();
        String type = view.getTypeComboBox().getValue();
        String posologie = view.getPosologieField().getText();
        String effetsSecondaires = view.getEffetsArea().getText();
        LocalDate dateDebut = view.getDateDebutPicker().getValue();
        LocalDate dateFin = view.getDateFinPicker().getValue();
        boolean actif = view.getActifCheckBox().isSelected();
        int duree = view.getDureeSpinner().getValue();
        int prises = view.getPrisesSpinner().getValue();
        double progression = view.getProgressionSlider().getValue();

        return new Treatment(
                patientId,
                patientName,
                nom,
                type,
                posologie,
                effetsSecondaires,
                dateDebut,
                dateFin,
                actif,
                duree,
                prises,
                progression
        );
    }

    private void fillForm(Treatment treatment) {
        selectPatientById(treatment.getPatientId());

        view.getNomField().setText(treatment.getNom());
        view.getTypeComboBox().setValue(treatment.getType());
        view.getPosologieField().setText(treatment.getPosologie());
        view.getEffetsArea().setText(treatment.getEffetsSecondaires());
        view.getDateDebutPicker().setValue(treatment.getDateDebut());
        view.getDateFinPicker().setValue(treatment.getDateFin());
        view.getActifCheckBox().setSelected(treatment.isActif());
        view.getDureeSpinner().getValueFactory().setValue(treatment.getDureeEstimee());
        view.getPrisesSpinner().getValueFactory().setValue(treatment.getNombrePrisesParJour());
        view.getProgressionSlider().setValue(treatment.getProgression());
    }

    private void selectPatientById(int patientId) {
        for (Patient patient : view.getPatientComboBox().getItems()) {
            if (patient.getId() == patientId) {
                view.getPatientComboBox().setValue(patient);
                return;
            }
        }
    }

    private void clearForm() {
        selectedTreatment = null;

        view.getTableView().getSelectionModel().clearSelection();

        view.getPatientComboBox().setValue(null);
        view.getNomField().clear();
        view.getTypeComboBox().setValue(null);
        view.getPosologieField().clear();
        view.getEffetsArea().clear();
        view.getDateDebutPicker().setValue(LocalDate.now());
        view.getDateFinPicker().setValue(LocalDate.now().plusDays(7));
        view.getActifCheckBox().setSelected(true);
        view.getDureeSpinner().getValueFactory().setValue(7);
        view.getPrisesSpinner().getValueFactory().setValue(2);
        view.getProgressionSlider().setValue(0);
    }
}
