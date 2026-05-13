package com.gestionhandicap.model;

import java.time.LocalDateTime;

public class PieceJustificative {

    private int idPiece;
    private String nomFichier;
    private String descPiece;
    private String cheminFichier;
    private LocalDateTime dateAjout;
    private int idDemande;

    public PieceJustificative() {}

    public PieceJustificative(int idPiece, String nomFichier, String descPiece,
                              String cheminFichier, int idDemande) {
        this.idPiece = idPiece;
        this.nomFichier = nomFichier;
        this.descPiece = descPiece;
        this.cheminFichier = cheminFichier;
        this.idDemande = idDemande;
    }

    // Getters
    public int getIdPiece() { return idPiece; }
    public String getNomFichier() { return nomFichier; }
    public String getDescPiece() { return descPiece; }
    public String getCheminFichier() { return cheminFichier; }
    public LocalDateTime getDateAjout() { return dateAjout; }
    public int getIdDemande() { return idDemande; }

    // Setters
    public void setIdPiece(int idPiece) { this.idPiece = idPiece; }
    public void setNomFichier(String nomFichier) { this.nomFichier = nomFichier; }
    public void setDescPiece(String descPiece) { this.descPiece = descPiece; }
    public void setCheminFichier(String cheminFichier) { this.cheminFichier = cheminFichier; }
    public void setDateAjout(LocalDateTime dateAjout) { this.dateAjout = dateAjout; }
    public void setIdDemande(int idDemande) { this.idDemande = idDemande; }

    @Override
    public String toString() {
        return "PieceJustificative{" + "id=" + idPiece + ", fichier='" + nomFichier + "', demande=" + idDemande + "}";
    }

    public String getChemin() {
        return "";
    }
}