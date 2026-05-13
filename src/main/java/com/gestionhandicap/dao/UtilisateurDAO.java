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

        String sql = "INSERT INTO utilisateur(nom,email,password) VALUES (?,?,?)";

        try {

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, u.getNom());
            stmt.setString(2, u.getEmail());
            stmt.setString(3, u.getPassword());

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
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));

                liste.add(u);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return liste;
    }
}