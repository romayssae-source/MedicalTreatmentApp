package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String SERVER_URL =
            "jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/medical_treatment_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }

    public static void initializeDatabase() {
        try (Connection serverConnection = DriverManager.getConnection(SERVER_URL, USER, PASSWORD);
             Statement statement = serverConnection.createStatement()) {

            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS medical_treatment_db");
            System.out.println("Base de données vérifiée.");

        } catch (SQLException e) {
            System.err.println("Erreur création base : " + e.getMessage());
        }

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            String createPatientsTable = """
                    CREATE TABLE IF NOT EXISTS patients (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        nom VARCHAR(100) NOT NULL,
                        prenom VARCHAR(100) NOT NULL,
                        date_naissance DATE,
                        sexe VARCHAR(20),
                        telephone VARCHAR(30),
                        remarques TEXT,
                        surveillance_active BOOLEAN DEFAULT FALSE
                    )
                    """;

            String createTreatmentsTable = """
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
                        progression DOUBLE,
                        FOREIGN KEY (patient_id) REFERENCES patients(id)
                            ON DELETE CASCADE
                            ON UPDATE CASCADE
                    )
                    """;

            statement.executeUpdate(createPatientsTable);
            statement.executeUpdate(createTreatmentsTable);

            System.out.println("Tables vérifiées.");

        } catch (SQLException e) {
            System.err.println("Erreur création tables : " + e.getMessage());
        }
    }
}
