CREATE DATABASE IF NOT EXISTS medical_treatment_db;

USE medical_treatment_db;

DROP TABLE IF EXISTS treatments;
DROP TABLE IF EXISTS patients;

CREATE TABLE patients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    date_naissance DATE,
    sexe VARCHAR(20),
    telephone VARCHAR(30),
    remarques TEXT,
    surveillance_active BOOLEAN DEFAULT FALSE
);

CREATE TABLE treatments (
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
    progression DOUBLE DEFAULT 0,
    FOREIGN KEY (patient_id) REFERENCES patients(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

INSERT INTO patients
(nom, prenom, date_naissance, sexe, telephone, remarques, surveillance_active)
VALUES
('Alami', 'Youssef', '2000-05-12', 'Homme', '0600000001', 'Patient sous surveillance régulière.', TRUE),
('Benali', 'Sara', '1998-09-20', 'Femme', '0600000002', 'Aucune remarque particulière.', FALSE),
('El Idrissi', 'Hamza', '1995-02-15', 'Homme', '0600000003', 'Contrôle nécessaire après traitement.', TRUE);

INSERT INTO treatments
(patient_id, nom, type, posologie, effets_secondaires, date_debut, date_fin, actif, duree_estimee, nombre_prises_par_jour, progression)
VALUES
(1, 'Amoxicilline', 'Antibiotique', '1 comprimé matin et soir', 'Troubles digestifs possibles', '2026-01-01', '2026-01-07', TRUE, 7, 2, 45),
(2, 'Paracétamol', 'Antalgique', '1 comprimé si douleur', 'Rare allergie', '2026-01-02', '2026-01-05', TRUE, 3, 3, 70),
(3, 'Ibuprofène', 'Anti-inflammatoire', '1 comprimé après repas', 'Irritation gastrique possible', '2026-01-03', '2026-01-10', FALSE, 7, 2, 100);
