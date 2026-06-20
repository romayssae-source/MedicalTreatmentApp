package util;

import model.Patient;
import model.Treatment;

public class Validator {

    public static String validatePatient(Patient patient) {
        if (patient.getNom() == null || patient.getNom().trim().isEmpty()) {
            return "Le nom du patient est obligatoire.";
        }

        if (patient.getPrenom() == null || patient.getPrenom().trim().isEmpty()) {
            return "Le prénom du patient est obligatoire.";
        }

        if (patient.getSexe() == null || patient.getSexe().trim().isEmpty()) {
            return "Le sexe du patient est obligatoire.";
        }

        return null;
    }

    public static String validateTreatment(Treatment treatment) {
        if (treatment.getPatientId() <= 0) {
            return "Veuillez sélectionner un patient.";
        }

        if (treatment.getNom() == null || treatment.getNom().trim().isEmpty()) {
            return "Le nom du traitement est obligatoire.";
        }

        if (treatment.getType() == null || treatment.getType().trim().isEmpty()) {
            return "Le type du traitement est obligatoire.";
        }

        if (treatment.getNombrePrisesParJour() <= 0) {
            return "Le nombre de prises par jour doit être supérieur à 0.";
        }

        if (treatment.getDureeEstimee() <= 0) {
            return "La durée estimée doit être supérieure à 0.";
        }

        if (treatment.getDateDebut() != null && treatment.getDateFin() != null) {
            if (treatment.getDateFin().isBefore(treatment.getDateDebut())) {
                return "La date de fin ne peut pas être avant la date de début.";
            }
        }

        return null;
    }
}
