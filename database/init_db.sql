CREATE DATABASE IF NOT EXISTS medical_treatment_db;

USE medical_treatment_db;

CREATE TABLE IF NOT EXISTS patients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    date_naissance DATE,
    sexe VARCHAR(20),
    telephone VARCHAR(30),
    remarques TEXT,
    surveillance_active BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS treatments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    nom VARCHAR(100) NOT NULL,
    type VARCHAR(100),
    posologie VARCHAR(255),
    effets_secondaires TEXT,
    date_debut DATE,
    date_fin DATE,
    actif BOOLEAN DEFAULT TRUE,
    duree_estimee INT,
    nombre_prises_par_jour INT,
    FOREIGN KEY (patient_id) REFERENCES patients(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);
