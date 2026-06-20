package model;

import java.time.LocalDate;

public class Treatment {

    private int id;
    private int patientId;
    private String nom;
    private String type;
    private String posologie;
    private String effetsSecondaires;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private boolean actif;
    private int dureeEstimee;
    private int nombrePrisesParJour;

    public Treatment() {
    }

    public Treatment(int id, int patientId, String nom, String type, String posologie,
                     String effetsSecondaires, LocalDate dateDebut, LocalDate dateFin,
                     boolean actif, int dureeEstimee, int nombrePrisesParJour) {
        this.id = id;
        this.patientId = patientId;
        this.nom = nom;
        this.type = type;
        this.posologie = posologie;
        this.effetsSecondaires = effetsSecondaires;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.actif = actif;
        this.dureeEstimee = dureeEstimee;
        this.nombrePrisesParJour = nombrePrisesParJour;
    }

    public Treatment(int patientId, String nom, String type, String posologie,
                     String effetsSecondaires, LocalDate dateDebut, LocalDate dateFin,
                     boolean actif, int dureeEstimee, int nombrePrisesParJour) {
        this.patientId = patientId;
        this.nom = nom;
        this.type = type;
        this.posologie = posologie;
        this.effetsSecondaires = effetsSecondaires;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.actif = actif;
        this.dureeEstimee = dureeEstimee;
        this.nombrePrisesParJour = nombrePrisesParJour;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }


    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getPosologie() {
        return posologie;
    }

    public void setPosologie(String posologie) {
        this.posologie = posologie;
    }


    public String getEffetsSecondaires() {
        return effetsSecondaires;
    }

    public void setEffetsSecondaires(String effetsSecondaires) {
        this.effetsSecondaires = effetsSecondaires;
    }


    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }


    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }


    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }


    public int getDureeEstimee() {
        return dureeEstimee;
    }

    public void setDureeEstimee(int dureeEstimee) {
        this.dureeEstimee = dureeEstimee;
    }


    public int getNombrePrisesParJour() {
        return nombrePrisesParJour;
    }

    public void setNombrePrisesParJour(int nombrePrisesParJour) {
        this.nombrePrisesParJour = nombrePrisesParJour;
    }

    @Override
    public String toString() {
        return nom + " - " + type;
    }
}
