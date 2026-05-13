package com.gestionhandicap.dao;

import com.gestionhandicap.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

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
}