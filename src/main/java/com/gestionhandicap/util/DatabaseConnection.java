package com.gestionhandicap.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    private static final String URL  = "jdbc:mysql://localhost:3306/gestion_handicap";
    private static final String USER = "root";
    private static final String PASS = "";

    private static Connection connection;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("[DB] Tentative de connexion à : " + URL);
                System.out.println("[DB] Utilisateur : " + USER);
                connection = DriverManager.getConnection(URL, USER, PASS);
                System.out.println("[DB] Connexion réussie !");
            }
        } catch (Exception e) {
            System.err.println("[DB] ERREUR de connexion");
            System.err.println("[DB] URL    : " + URL);
            System.err.println("[DB] User   : " + USER);
            System.err.println("[DB] Type   : " + e.getClass().getName());
            System.err.println("[DB] Message: " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("[DB] Cause  : " + e.getCause().getMessage());
            }
            e.printStackTrace();
        }
        return connection;
    }

    private DatabaseConnection() {}
}
