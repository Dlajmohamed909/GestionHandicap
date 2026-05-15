package com.gestionhandicap.dao;

import com.gestionhandicap.model.Utilisateur;
import com.gestionhandicap.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {

    private Connection connection;

    public UtilisateurDAO() {
        connection = DatabaseConnection.getConnection();
    }

    public void ajouterUtilisateur(Utilisateur u) {

        String sql = "INSERT INTO utilisateur(nom, prenom, email, mot_de_passe, role) VALUES (?,?,?,?,?)";

        try {

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, u.getNom());
            stmt.setString(2, u.getPrenom());
            stmt.setString(3, u.getEmail());
            stmt.setString(4, u.getMotDePasse());
            stmt.setString(5, u.getRole());

            stmt.executeUpdate();

            System.out.println("Utilisateur ajouté");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Utilisateur> getAllUtilisateurs() {

        List<Utilisateur> liste = new ArrayList<>();

        String sql = "SELECT * FROM utilisateur";

        try {

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()) {

                Utilisateur u = new Utilisateur();

                u.setId(rs.getInt("id"));
                u.setNom(rs.getString("nom"));
                u.setPrenom(rs.getString("prenom"));
                u.setEmail(rs.getString("email"));
                u.setMotDePasse(rs.getString("mot_de_passe"));
                u.setRole(rs.getString("role"));
                u.setDateCreation(rs.getTimestamp("date_creation") != null
                        ? rs.getTimestamp("date_creation").toLocalDateTime() : null);

                liste.add(u);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return liste;
    }

    public Utilisateur getByEmail(String email) {
        String sql = "SELECT * FROM utilisateur WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Utilisateur u = new Utilisateur();
                u.setId(rs.getInt("id"));
                u.setNom(rs.getString("nom"));
                u.setPrenom(rs.getString("prenom"));
                u.setEmail(rs.getString("email"));
                u.setMotDePasse(rs.getString("mot_de_passe"));
                u.setRole(rs.getString("role"));
                u.setDateCreation(rs.getTimestamp("date_creation") != null
                        ? rs.getTimestamp("date_creation").toLocalDateTime() : null);
                return u;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public void modifierUtilisateur(int id, String nom, String prenom, String email, String motDePasse) {
        String sql = "UPDATE utilisateur SET nom=?, prenom=?, email=?, mot_de_passe=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            stmt.setString(3, email);
            stmt.setString(4, motDePasse);
            stmt.setInt(5, id);
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void supprimerUtilisateur(int id) {
        String sql = "DELETE FROM utilisateur WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public Utilisateur authentifier(String email, String motDePasse) {
        String sql = "SELECT * FROM utilisateur WHERE email = ? AND mot_de_passe = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, motDePasse);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Utilisateur u = new Utilisateur();
                u.setId(rs.getInt("id"));
                u.setNom(rs.getString("nom"));
                u.setPrenom(rs.getString("prenom"));
                u.setEmail(rs.getString("email"));
                u.setMotDePasse(rs.getString("mot_de_passe"));
                u.setRole(rs.getString("role"));
                u.setDateCreation(rs.getTimestamp("date_creation") != null
                        ? rs.getTimestamp("date_creation").toLocalDateTime() : null);
                return u;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}