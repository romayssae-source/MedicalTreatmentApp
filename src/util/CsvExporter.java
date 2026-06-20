package util;

import javafx.stage.FileChooser;
import javafx.stage.Window;
import model.Patient;
import model.Treatment;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

public class CsvExporter {

    public static void exportPatients(List<Patient> patients, Window owner) {
        File file = chooseFile(owner, "patients.csv");

        if (file == null) {
            return;
        }

        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("ID,Nom,Prenom,DateNaissance,Sexe,Telephone,Surveillance,Remarques");

            for (Patient p : patients) {
                writer.printf("%d,%s,%s,%s,%s,%s,%s,%s%n",
                        p.getId(),
                        safe(p.getNom()),
                        safe(p.getPrenom()),
                        p.getDateNaissance(),
                        safe(p.getSexe()),
                        safe(p.getTelephone()),
                        p.isSurveillanceActive(),
                        safe(p.getRemarques()));
            }

            AlertUtil.showInfo("Export réussi", "La liste des patients a été exportée avec succès.");

        } catch (Exception e) {
            AlertUtil.showError("Erreur export", e.getMessage());
        }
    }

    public static void exportTreatments(List<Treatment> treatments, Window owner) {
        File file = chooseFile(owner, "treatments.csv");

        if (file == null) {
            return;
        }

        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("ID,Patient,Traitement,Type,Posologie,EffetsSecondaires,DateDebut,DateFin,Actif,Duree,PrisesParJour,Progression");

            for (Treatment t : treatments) {
                writer.printf("%d,%s,%s,%s,%s,%s,%s,%s,%s,%d,%d,%.2f%n",
                        t.getId(),
                        safe(t.getPatientName()),
                        safe(t.getNom()),
                        safe(t.getType()),
                        safe(t.getPosologie()),
                        safe(t.getEffetsSecondaires()),
                        t.getDateDebut(),
                        t.getDateFin(),
                        t.isActif(),
                        t.getDureeEstimee(),
                        t.getNombrePrisesParJour(),
                        t.getProgression());
            }

            AlertUtil.showInfo("Export réussi", "La liste des traitements a été exportée avec succès.");

        } catch (Exception e) {
            AlertUtil.showError("Erreur export", e.getMessage());
        }
    }

    private static File chooseFile(Window owner, String defaultFileName) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter en CSV");
        fileChooser.setInitialFileName(defaultFileName);
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichier CSV", "*.csv")
        );

        return fileChooser.showSaveDialog(owner);
    }

    private static String safe(String value) {
        if (value == null) {
            return "";
        }

        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
