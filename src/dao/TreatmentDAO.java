package dao;

import model.Treatment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TreatmentDAO {

    public void addTreatment(Treatment treatment) throws SQLException {
        String sql = """
                INSERT INTO treatments 
                (patient_id, nom, type, posologie, effets_secondaires, date_debut, date_fin,
                 actif, duree_estimee, nombre_prises_par_jour)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, treatment.getPatientId());
            ps.setString(2, treatment.getNom());
            ps.setString(3, treatment.getType());
            ps.setString(4, treatment.getPosologie());
            ps.setString(5, treatment.getEffetsSecondaires());

            if (treatment.getDateDebut() != null) {
                ps.setDate(6, Date.valueOf(treatment.getDateDebut()));
            } else {
                ps.setNull(6, Types.DATE);
            }

            if (treatment.getDateFin() != null) {
                ps.setDate(7, Date.valueOf(treatment.getDateFin()));
            } else {
                ps.setNull(7, Types.DATE);
            }

            ps.setBoolean(8, treatment.isActif());
            ps.setInt(9, treatment.getDureeEstimee());
            ps.setInt(10, treatment.getNombrePrisesParJour());

            ps.executeUpdate();
        }
    }

    public List<Treatment> getAllTreatments() throws SQLException {
        List<Treatment> treatments = new ArrayList<>();

        String sql = "SELECT * FROM treatments ORDER BY id DESC";

        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                treatments.add(mapTreatment(rs));
            }
        }

        return treatments;
    }

    private Treatment mapTreatment(ResultSet rs) throws SQLException {
        Date dateDebut = rs.getDate("date_debut");
        Date dateFin = rs.getDate("date_fin");

        return new Treatment(
                rs.getInt("id"),
                rs.getInt("patient_id"),
                rs.getString("nom"),
                rs.getString("type"),
                rs.getString("posologie"),
                rs.getString("effets_secondaires"),
                dateDebut != null ? dateDebut.toLocalDate() : null,
                dateFin != null ? dateFin.toLocalDate() : null,
                rs.getBoolean("actif"),
                rs.getInt("duree_estimee"),
                rs.getInt("nombre_prises_par_jour")
        );
    }
}
