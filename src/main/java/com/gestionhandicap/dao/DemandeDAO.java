package com.gestionhandicap.dao;

import com.gestionhandicap.model.Demande;
import com.gestionhandicap.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DemandeDAO {

    private Connection connection;

    public DemandeDAO() {
        connection = DatabaseConnection.getConnection();
    }

    public int ajouterDemande(Demande d) {
        String sql = "INSERT INTO demande(type, description, statut, id_personne, id_admin) VALUES (?,?,?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, d.getType());
            stmt.setString(2, d.getDescription());
            stmt.setString(3, d.getStatut());
            stmt.setInt(4, d.getIdPersonne());
            if (d.getIdAdmin() == 0) {
                stmt.setNull(5, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(5, d.getIdAdmin());
            }
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<Demande> getAllDemandes() {

        List<Demande> liste = new ArrayList<>();

        String sql = "SELECT * FROM demande";

        try {

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()) {

                Demande d = new Demande();

                d.setIdDemande(rs.getInt("id_demande"));
                d.setType(rs.getString("type"));
                d.setDescription(rs.getString("description"));
                d.setStatut(rs.getString("statut"));
                d.setIdPersonne(rs.getInt("id_personne"));
                d.setIdAdmin(rs.getInt("id_admin"));
                d.setDateDemande(rs.getTimestamp("date_demande") != null
                        ? rs.getTimestamp("date_demande").toLocalDateTime() : null);

                liste.add(d);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return liste;
    }

    public List<Demande> getDemandesByPersonne(int idPersonne) {
        List<Demande> liste = new ArrayList<>();
        String sql = "SELECT * FROM demande WHERE id_personne = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idPersonne);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Demande d = new Demande();
                d.setIdDemande(rs.getInt("id_demande"));
                d.setType(rs.getString("type"));
                d.setDescription(rs.getString("description"));
                d.setStatut(rs.getString("statut"));
                d.setIdPersonne(rs.getInt("id_personne"));
                d.setIdAdmin(rs.getInt("id_admin"));
                d.setDateDemande(rs.getTimestamp("date_demande") != null
                        ? rs.getTimestamp("date_demande").toLocalDateTime() : null);
                liste.add(d);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return liste;
    }

    public void modifierDemande(int idDemande, String type, String description) {
        String sql = "UPDATE demande SET type = ?, description = ? WHERE id_demande = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, type);
            stmt.setString(2, description);
            stmt.setInt(3, idDemande);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateStatut(int idDemande, String statut) {
        String sql = "UPDATE demande SET statut = ? WHERE id_demande = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, statut);
            stmt.setInt(2, idDemande);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void supprimerDemande(int idDemande) {
        String sql = "DELETE FROM demande WHERE id_demande = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idDemande);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}