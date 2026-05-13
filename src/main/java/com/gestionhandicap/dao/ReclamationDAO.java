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

        String sql = "INSERT INTO reclamation(sujet,message) VALUES (?,?)";

        try {

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, r.getSujet());
            stmt.setString(2, r.getMessage());

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

                r.setId(rs.getInt("id"));
                r.setSujet(rs.getString("sujet"));
                r.setMessage(rs.getString("message"));

                liste.add(r);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return liste;
    }
}