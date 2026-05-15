package com.gestionhandicap.dao;

import com.gestionhandicap.model.Reclamation;
import com.gestionhandicap.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReclamationDAO {

    private Connection connection;

    public ReclamationDAO() {
        connection = DatabaseConnection.getConnection();
    }

    public void ajouterReclamation(Reclamation r) {

        String sql = "INSERT INTO reclamation(description, statut, id_personne, id_admin) VALUES (?,?,?,?)";

        try {

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, r.getDescription());
            stmt.setString(2, r.getStatut());
            stmt.setInt(3, r.getIdPersonne());
            if (r.getIdAdmin() == 0) {
                stmt.setNull(4, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(4, r.getIdAdmin());
            }

            stmt.executeUpdate();

            System.out.println("Réclamation ajoutée");

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public List<Reclamation> getAllReclamations() {

        List<Reclamation> liste = new ArrayList<>();

        String sql = "SELECT * FROM reclamation";

        try {

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()) {

                Reclamation r = new Reclamation();

                r.setIdReclamation(rs.getInt("id_reclamation"));
                r.setDescription(rs.getString("description"));
                r.setStatut(rs.getString("statut"));
                r.setIdPersonne(rs.getInt("id_personne"));
                r.setIdAdmin(rs.getInt("id_admin"));
                r.setDateReclamation(rs.getTimestamp("date_reclamation") != null
                        ? rs.getTimestamp("date_reclamation").toLocalDateTime() : null);

                liste.add(r);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return liste;
    }

    public List<Reclamation> getReclamationsByPersonne(int idPersonne) {
        List<Reclamation> liste = new ArrayList<>();
        String sql = "SELECT * FROM reclamation WHERE id_personne = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idPersonne);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Reclamation r = new Reclamation();
                r.setIdReclamation(rs.getInt("id_reclamation"));
                r.setDescription(rs.getString("description"));
                r.setStatut(rs.getString("statut"));
                r.setIdPersonne(rs.getInt("id_personne"));
                r.setIdAdmin(rs.getInt("id_admin"));
                r.setDateReclamation(rs.getTimestamp("date_reclamation") != null
                        ? rs.getTimestamp("date_reclamation").toLocalDateTime() : null);
                liste.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return liste;
    }

    public void modifierReclamation(int idReclamation, String description) {
        String sql = "UPDATE reclamation SET description = ? WHERE id_reclamation = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, description);
            stmt.setInt(2, idReclamation);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateStatut(int idReclamation, String statut) {
        String sql = "UPDATE reclamation SET statut = ? WHERE id_reclamation = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, statut);
            stmt.setInt(2, idReclamation);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void supprimerReclamation(int idReclamation) {
        String sql = "DELETE FROM reclamation WHERE id_reclamation = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idReclamation);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}