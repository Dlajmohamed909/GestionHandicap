package com.gestionhandicap.dao;

import com.gestionhandicap.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
public class StatistiqueDAO {

    private Connection connection;

    public StatistiqueDAO() {
        connection = DatabaseConnection.getConnection();
    }

    public int nombreDemandes() {

        int total = 0;

        String sql = "SELECT COUNT(*) AS total FROM demande";

        try {

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            if(rs.next()) {
                total = rs.getInt("total");
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return total;
    }

    public int nombreReclamations() {
        int total = 0;
        String sql = "SELECT COUNT(*) AS total FROM reclamation";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) total = rs.getInt("total");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    public int nombreDemandesParStatut(String statut) {
        int total = 0;
        String sql = "SELECT COUNT(*) AS total FROM demande WHERE statut = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, statut);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) total = rs.getInt("total");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    public int nombreReclamationsParStatut(String statut) {
        int total = 0;
        String sql = "SELECT COUNT(*) AS total FROM reclamation WHERE statut = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, statut);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) total = rs.getInt("total");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }
}