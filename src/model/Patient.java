package model;

import java.time.LocalDate;

public class Patient {

    private int id;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String sexe;
    private String telephone;
    private String remarques;
    private boolean surveillanceActive;

    public Patient() {
    }

    public Patient(int id, String nom, String prenom, LocalDate dateNaissance,
                   String sexe, String telephone, String remarques, boolean surveillanceActive) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.sexe = sexe;
        this.telephone = telephone;
        this.remarques = remarques;
        this.surveillanceActive = surveillanceActive;
    }

    public Patient(String nom, String prenom, LocalDate dateNaissance,
                   String sexe, String telephone, String remarques, boolean surveillanceActive) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.sexe = sexe;
        this.telephone = telephone;
        this.remarques = remarques;
        this.surveillanceActive = surveillanceActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }


    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }


    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }


    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }


    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }


    public String getRemarques() {
        return remarques;
    }

    public void setRemarques(String remarques) {
        this.remarques = remarques;
    }


    public boolean isSurveillanceActive() {
        return surveillanceActive;
    }

    public void setSurveillanceActive(boolean surveillanceActive) {
        this.surveillanceActive = surveillanceActive;
    }

    public String getNomComplet() {
        return nom + " " + prenom;
    }

    @Override
    public String toString() {
        return getNomComplet();
    }
}
