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
                 actif, duree_estimee, nombre_prises_par_jour, progression)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            fillStatement(ps, treatment, false);
            ps.executeUpdate();
        }
    }

    public void updateTreatment(Treatment treatment) throws SQLException {
        String sql = """
                UPDATE treatments
                SET patient_id = ?, nom = ?, type = ?, posologie = ?, effets_secondaires = ?,
                    date_debut = ?, date_fin = ?, actif = ?, duree_estimee = ?,
                    nombre_prises_par_jour = ?, progression = ?
                WHERE id = ?
                """;

        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            fillStatement(ps, treatment, true);
            ps.executeUpdate();
        }
    }

    public void deleteTreatment(int id) throws SQLException {
        String sql = "DELETE FROM treatments WHERE id = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Treatment> getAllTreatments() throws SQLException {
        List<Treatment> treatments = new ArrayList<>();

        String sql = """
                SELECT t.*, CONCAT(p.nom, ' ', p.prenom) AS patient_name
                FROM treatments t
                JOIN patients p ON t.patient_id = p.id
                ORDER BY t.id DESC
                """;

        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                treatments.add(mapTreatment(rs));
            }
        }

        return treatments;
    }

    public List<Treatment> searchTreatments(String keyword, String type, boolean activeOnly) throws SQLException {
        List<Treatment> treatments = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
                SELECT t.*, CONCAT(p.nom, ' ', p.prenom) AS patient_name
                FROM treatments t
                JOIN patients p ON t.patient_id = p.id
                WHERE (t.nom LIKE ? OR t.type LIKE ? OR t.posologie LIKE ?
                       OR p.nom LIKE ? OR p.prenom LIKE ?)
                """);

        if (type != null && !type.equals("Tous")) {
            sql.append(" AND t.type = ? ");
        }

        if (activeOnly) {
            sql.append(" AND t.actif = TRUE ");
        }

        sql.append(" ORDER BY t.id DESC ");

        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql.toString())) {

            String pattern = "%" + keyword + "%";

            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);
            ps.setString(4, pattern);
            ps.setString(5, pattern);

            int index = 6;

            if (type != null && !type.equals("Tous")) {
                ps.setString(index, type);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    treatments.add(mapTreatment(rs));
                }
            }
        }

        return treatments;
    }

    private void fillStatement(PreparedStatement ps, Treatment treatment, boolean update) throws SQLException {
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
        ps.setDouble(11, treatment.getProgression());

        if (update) {
            ps.setInt(12, treatment.getId());
        }
    }

    private Treatment mapTreatment(ResultSet rs) throws SQLException {
        Date dateDebut = rs.getDate("date_debut");
        Date dateFin = rs.getDate("date_fin");

        return new Treatment(
                rs.getInt("id"),
                rs.getInt("patient_id"),
                rs.getString("patient_name"),
                rs.getString("nom"),
                rs.getString("type"),
                rs.getString("posologie"),
                rs.getString("effets_secondaires"),
                dateDebut != null ? dateDebut.toLocalDate() : null,
                dateFin != null ? dateFin.toLocalDate() : null,
                rs.getBoolean("actif"),
                rs.getInt("duree_estimee"),
                rs.getInt("nombre_prises_par_jour"),
                rs.getDouble("progression")
        );
    }
}

   
