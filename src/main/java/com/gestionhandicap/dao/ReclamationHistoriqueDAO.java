package com.gestionhandicap.dao;

import com.gestionhandicap.model.ReclamationHistorique;
import com.gestionhandicap.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReclamationHistoriqueDAO {

    private final Connection connection;

    public ReclamationHistoriqueDAO() {
        connection = DatabaseConnection.getConnection();
    }

    public void ajouterHistorique(int idReclamation, String ancienStatut, String nouveauStatut) {
        String sql = "INSERT INTO reclamation_historique (id_reclamation, ancien_statut, nouveau_statut) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idReclamation);
            if (ancienStatut != null) {
                stmt.setString(2, ancienStatut);
            } else {
                stmt.setNull(2, Types.VARCHAR);
            }
            stmt.setString(3, nouveauStatut);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ReclamationHistorique> getHistoriqueByReclamation(int idReclamation) {
        List<ReclamationHistorique> liste = new ArrayList<>();
        String sql = "SELECT * FROM reclamation_historique WHERE id_reclamation = ? ORDER BY date_changement DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idReclamation);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ReclamationHistorique h = new ReclamationHistorique();
                h.setId(rs.getInt("id"));
                h.setIdReclamation(rs.getInt("id_reclamation"));
                h.setAncienStatut(rs.getString("ancien_statut"));
                h.setNouveauStatut(rs.getString("nouveau_statut"));
                Timestamp ts = rs.getTimestamp("date_changement");
                if (ts != null) h.setDateChangement(ts.toLocalDateTime());
                liste.add(h);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return liste;
    }
}
