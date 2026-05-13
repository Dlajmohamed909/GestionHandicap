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

    public void ajouterDemande(Demande d) {

        String sql = "INSERT INTO demande(type,description) VALUES (?,?)";

        try {

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, d.getType());
            stmt.setString(2, d.getDescription());

            stmt.executeUpdate();

            System.out.println("Demande ajoutée");

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public List<Demande> getAllDemandes() {

        List<Demande> liste = new ArrayList<>();

        String sql = "SELECT * FROM demande";

        try {

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()) {

                Demande d = new Demande();

                d.setId(rs.getInt("id"));
                d.setType(rs.getString("type"));
                d.setDescription(rs.getString("description"));

                liste.add(d);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return liste;
    }
}