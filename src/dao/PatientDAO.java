package dao;

import model.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    public void addPatient(Patient patient) throws SQLException {
        String sql = """
                INSERT INTO patients 
                (nom, prenom, date_naissance, sexe, telephone, remarques, surveillance_active)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, patient.getNom());
            ps.setString(2, patient.getPrenom());

            if (patient.getDateNaissance() != null) {
                ps.setDate(3, Date.valueOf(patient.getDateNaissance()));
            } else {
                ps.setNull(3, Types.DATE);
            }

            ps.setString(4, patient.getSexe());
            ps.setString(5, patient.getTelephone());
            ps.setString(6, patient.getRemarques());
            ps.setBoolean(7, patient.isSurveillanceActive());

            ps.executeUpdate();
        }
    }

    public List<Patient> getAllPatients() throws SQLException {
        List<Patient> patients = new ArrayList<>();

        String sql = "SELECT * FROM patients ORDER BY id DESC";

        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                patients.add(mapPatient(rs));
            }
        }

        return patients;
    }

    private Patient mapPatient(ResultSet rs) throws SQLException {
        Date date = rs.getDate("date_naissance");

        return new Patient(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("prenom"),
                date != null ? date.toLocalDate() : null,
                rs.getString("sexe"),
                rs.getString("telephone"),
                rs.getString("remarques"),
                rs.getBoolean("surveillance_active")
        );
    }
}
