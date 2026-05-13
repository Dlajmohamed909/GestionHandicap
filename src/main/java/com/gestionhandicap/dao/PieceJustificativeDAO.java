package com.gestionhandicap.dao;

import com.gestionhandicap.model.PieceJustificative;
import com.gestionhandicap.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PieceJustificativeDAO {

    private Connection connection;

    public PieceJustificativeDAO() {
        connection = DatabaseConnection.getConnection();
    }

    public void ajouterPiece(PieceJustificative p) {

        String sql = "INSERT INTO piece_justificative(nom_fichier, desc_piece, chemin_fichier, id_demande) VALUES (?,?,?,?)";

        try {

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, p.getNomFichier());
            stmt.setString(2, p.getDescPiece());
            stmt.setString(3, p.getCheminFichier());
            stmt.setInt(4, p.getIdDemande());

            stmt.executeUpdate();

            System.out.println("Pièce ajoutée");

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public List<PieceJustificative> getAllPieces() {

        List<PieceJustificative> liste = new ArrayList<>();

        String sql = "SELECT * FROM piece_justificative";

        try {

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()) {

                PieceJustificative p = new PieceJustificative();

                p.setIdPiece(rs.getInt("id_piece"));
                p.setNomFichier(rs.getString("nom_fichier"));
                p.setDescPiece(rs.getString("desc_piece"));
                p.setCheminFichier(rs.getString("chemin_fichier"));
                p.setIdDemande(rs.getInt("id_demande"));
                p.setDateAjout(rs.getTimestamp("date_ajout") != null
                        ? rs.getTimestamp("date_ajout").toLocalDateTime() : null);

                liste.add(p);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return liste;
    }

    public List<PieceJustificative> getPiecesByDemande(int idDemande) {
        List<PieceJustificative> liste = new ArrayList<>();
        String sql = "SELECT * FROM piece_justificative WHERE id_demande = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idDemande);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                PieceJustificative p = new PieceJustificative();
                p.setIdPiece(rs.getInt("id_piece"));
                p.setNomFichier(rs.getString("nom_fichier"));
                p.setDescPiece(rs.getString("desc_piece"));
                p.setCheminFichier(rs.getString("chemin_fichier"));
                p.setIdDemande(rs.getInt("id_demande"));
                p.setDateAjout(rs.getTimestamp("date_ajout") != null
                        ? rs.getTimestamp("date_ajout").toLocalDateTime() : null);
                liste.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return liste;
    }
}