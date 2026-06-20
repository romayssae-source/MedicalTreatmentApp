package controller;

import dao.PatientDAO;
import dao.TreatmentDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import model.Patient;
import model.Treatment;
import util.AlertUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StatisticsController {

    @FXML
    private Label totalPatientsLabel;

    @FXML
    private Label surveillancePatientsLabel;

    @FXML
    private Label totalTreatmentsLabel;

    @FXML
    private Label activeTreatmentsLabel;

    @FXML
    private Label averageProgressLabel;

    @FXML
    private ProgressBar surveillanceProgressBar;

    @FXML
    private ProgressBar activeTreatmentsProgressBar;

    @FXML
    private ProgressBar averageProgressBar;

    @FXML
    private ProgressIndicator loadingIndicator;

    @FXML
    private ListView<String> summaryListView;

    private final PatientDAO patientDAO = new PatientDAO();
    private final TreatmentDAO treatmentDAO = new TreatmentDAO();

    @FXML
    public void initialize() {
        refreshStatistics();
    }

    @FXML
    private void refreshStatistics() {
        try {
            loadingIndicator.setVisible(true);

            List<Patient> patients = patientDAO.getAllPatients();
            List<Treatment> treatments = treatmentDAO.getAllTreatments();

            int totalPatients = patients.size();
            int surveillancePatients = 0;

            for (Patient patient : patients) {
                if (patient.isSurveillanceActive()) {
                    surveillancePatients++;
                }
            }

            int totalTreatments = treatments.size();
            int activeTreatments = 0;
            double totalProgress = 0;

            for (Treatment treatment : treatments) {
                if (treatment.isActif()) {
                    activeTreatments++;
                }

                totalProgress += treatment.getProgression();
            }

            double surveillanceRate = totalPatients == 0 ? 0 : (double) surveillancePatients / totalPatients;
            double activeRate = totalTreatments == 0 ? 0 : (double) activeTreatments / totalTreatments;
            double averageProgress = totalTreatments == 0 ? 0 : totalProgress / totalTreatments;

            totalPatientsLabel.setText(String.valueOf(totalPatients));
            surveillancePatientsLabel.setText(String.valueOf(surveillancePatients));
            totalTreatmentsLabel.setText(String.valueOf(totalTreatments));
            activeTreatmentsLabel.setText(String.valueOf(activeTreatments));
            averageProgressLabel.setText(String.format("%.1f %%", averageProgress));

            surveillanceProgressBar.setProgress(surveillanceRate);
            activeTreatmentsProgressBar.setProgress(activeRate);
            averageProgressBar.setProgress(averageProgress / 100.0);

            List<String> summary = new ArrayList<>();
            summary.add("Nombre total de patients : " + totalPatients);
            summary.add("Patients sous surveillance : " + surveillancePatients);
            summary.add("Nombre total de traitements : " + totalTreatments);
            summary.add("Traitements actifs : " + activeTreatments);
            summary.add(String.format("Progression moyenne des traitements : %.1f %%", averageProgress));

            summaryListView.setItems(FXCollections.observableArrayList(summary));

        } catch (SQLException e) {
            AlertUtil.showError("Erreur statistiques", e.getMessage());
        } finally {
            loadingIndicator.setVisible(false);
        }
    }
}
