package view;

import dao.PatientDAO;
import dao.TreatmentDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import model.Patient;
import model.Treatment;
import util.AlertUtil;

import java.sql.SQLException;
import java.util.List;

public class StatisticsView {

    private final VBox root;

    private final Label totalPatientsLabel;
    private final Label patientsSurveillanceLabel;
    private final Label totalTreatmentsLabel;
    private final Label activeTreatmentsLabel;
    private final Label averageProgressionLabel;

    private final ProgressBar patientsProgressBar;
    private final ProgressBar treatmentsProgressBar;
    private final ProgressBar progressionProgressBar;

    private final PatientDAO patientDAO;
    private final TreatmentDAO treatmentDAO;

    public StatisticsView() {
        this.patientDAO = new PatientDAO();
        this.treatmentDAO = new TreatmentDAO();

        root = new VBox(25);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Tableau de bord et Statistiques");
        title.getStyleClass().add("title");

        totalPatientsLabel = new Label("Total patients : 0");
        patientsSurveillanceLabel = new Label("Patients sous surveillance : 0");
        totalTreatmentsLabel = new Label("Total traitements : 0");
        activeTreatmentsLabel = new Label("Traitements actifs : 0");
        averageProgressionLabel = new Label("Progression moyenne : 0 %");

        patientsProgressBar = new ProgressBar(0);
        treatmentsProgressBar = new ProgressBar(0);
        progressionProgressBar = new ProgressBar(0);

        patientsProgressBar.setPrefWidth(350);
        treatmentsProgressBar.setPrefWidth(350);
        progressionProgressBar.setPrefWidth(350);

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(18);
        grid.setAlignment(Pos.CENTER);
        grid.getStyleClass().add("stats-grid");

        grid.add(new Label("Patients enregistrés :"), 0, 0);
        grid.add(totalPatientsLabel, 1, 0);

        grid.add(new Label("Surveillance active :"), 0, 1);
        grid.add(patientsSurveillanceLabel, 1, 1);
        grid.add(patientsProgressBar, 2, 1);

        grid.add(new Label("Traitements enregistrés :"), 0, 2);
        grid.add(totalTreatmentsLabel, 1, 2);

        grid.add(new Label("Traitements actifs :"), 0, 3);
        grid.add(activeTreatmentsLabel, 1, 3);
        grid.add(treatmentsProgressBar, 2, 3);

        grid.add(new Label("Progression moyenne :"), 0, 4);
        grid.add(averageProgressionLabel, 1, 4);
        grid.add(progressionProgressBar, 2, 4);

        root.getChildren().addAll(title, grid);
    }

    public VBox getRoot() {
        return root;
    }

    public void refresh() {
        try {
            List<Patient> patients = patientDAO.getAllPatients();
            List<Treatment> treatments = treatmentDAO.getAllTreatments();

            int totalPatients = patients.size();
            int patientsSurveillance = 0;

            for (Patient patient : patients) {
                if (patient.isSurveillanceActive()) {
                    patientsSurveillance++;
                }
            }

            int totalTreatments = treatments.size();
            int activeTreatments = 0;
            double totalProgression = 0;

            for (Treatment treatment : treatments) {
                if (treatment.isActif()) {
                    activeTreatments++;
                }

                totalProgression += treatment.getProgression();
            }

            double surveillanceRate = totalPatients == 0 ? 0 : (double) patientsSurveillance / totalPatients;
            double activeRate = totalTreatments == 0 ? 0 : (double) activeTreatments / totalTreatments;
            double averageProgression = totalTreatments == 0 ? 0 : totalProgression / totalTreatments;

            totalPatientsLabel.setText("Total patients : " + totalPatients);
            patientsSurveillanceLabel.setText("Patients sous surveillance : " + patientsSurveillance);
            totalTreatmentsLabel.setText("Total traitements : " + totalTreatments);
            activeTreatmentsLabel.setText("Traitements actifs : " + activeTreatments);
            averageProgressionLabel.setText(String.format("Progression moyenne : %.2f %%", averageProgression));

            patientsProgressBar.setProgress(surveillanceRate);
            treatmentsProgressBar.setProgress(activeRate);
            progressionProgressBar.setProgress(averageProgression / 100.0);

        } catch (SQLException e) {
            AlertUtil.showError("Erreur statistiques", e.getMessage());
        }
    }
}
