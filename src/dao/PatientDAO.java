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

    public void updatePatient(Patient patient) throws SQLException {
        String sql = """
                UPDATE patients 
                SET nom = ?, prenom = ?, date_naissance = ?, sexe = ?, telephone = ?, 
                    remarques = ?, surveillance_active = ?
                WHERE id = ?
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
            ps.setInt(8, patient.getId());

            ps.executeUpdate();
        }
    }

    public void deletePatient(int id) throws SQLException {
        String sql = "DELETE FROM patients WHERE id = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
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

    public List<Patient> searchPatients(String keyword, String sexe, Boolean surveillanceOnly) throws SQLException {
        List<Patient> patients = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
                SELECT * FROM patients 
                WHERE (nom LIKE ? OR prenom LIKE ? OR telephone LIKE ?)
                """);

        if (sexe != null && !sexe.equals("Tous")) {
            sql.append(" AND sexe = ? ");
        }

        if (surveillanceOnly != null && surveillanceOnly) {
            sql.append(" AND surveillance_active = TRUE ");
        }

        sql.append(" ORDER BY id DESC ");

        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql.toString())) {

            String pattern = "%" + keyword + "%";

            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);

            int index = 4;

            if (sexe != null && !sexe.equals("Tous")) {
                ps.setString(index++, sexe);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    patients.add(mapPatient(rs));
                }
            }
        }

        return patients;
    }

    private Patient mapPatient(ResultSet rs) throws SQLException {
        Date birthDate = rs.getDate("date_naissance");

        return new Patient(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("prenom"),
                birthDate != null ? birthDate.toLocalDate() : null,
                rs.getString("sexe"),
                rs.getString("telephone"),
                rs.getString("remarques"),
                rs.getBoolean("surveillance_active")
        );
    }
}
