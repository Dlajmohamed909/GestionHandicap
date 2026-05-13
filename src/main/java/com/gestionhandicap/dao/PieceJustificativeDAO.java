package com.gestionhandicap.dao;

import com.gestionhandicap.model.PieceJustificative;
import com.gestionhandicap.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class PieceJustificativeDAO {

    private Connection connection;

    public PieceJustificativeDAO() {
        connection = DatabaseConnection.getConnection();
    }

    public void ajouterPiece(PieceJustificative p) {

        String sql = "INSERT INTO piece_justificative(nom_fichier,chemin) VALUES (?,?)";

        try {

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, p.getNomFichier());
            stmt.setString(2, p.getChemin());

            stmt.executeUpdate();

            System.out.println("Pièce ajoutée");

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}