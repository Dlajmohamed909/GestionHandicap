package com.gestionhandicap.model;

import java.time.LocalDateTime;

public class Demande {

    private int idDemande;
    private String type;
    private String description;
    private LocalDateTime dateDemande;
    private String statut;
    private int idPersonne;
    private int idAdmin;

    public Demande() {}

    public Demande(int idDemande, String type, String description, String statut,
                   int idPersonne, int idAdmin) {
        this.idDemande = idDemande;
        this.type = type;
        this.description = description;
        this.statut = statut;
        this.idPersonne = idPersonne;
        this.idAdmin = idAdmin;
    }

    // Getters
    public int getIdDemande() { return idDemande; }
    public String getType() { return type; }
    public String getDescription() { return description; }
    public LocalDateTime getDateDemande() { return dateDemande; }
    public String getStatut() { return statut; }
    public int getIdPersonne() { return idPersonne; }
    public int getIdAdmin() { return idAdmin; }

    // Setters
    public void setIdDemande(int idDemande) { this.idDemande = idDemande; }
    public void setType(String type) { this.type = type; }
    public void setDescription(String description) { this.description = description; }
    public void setDateDemande(LocalDateTime dateDemande) { this.dateDemande = dateDemande; }
    public void setStatut(String statut) { this.statut = statut; }
    public void setIdPersonne(int idPersonne) { this.idPersonne = idPersonne; }
    public void setIdAdmin(int idAdmin) { this.idAdmin = idAdmin; }

    @Override
    public String toString() {
        return "Demande{" + "id=" + idDemande + ", type='" + type + "', statut='" + statut + "', date=" + dateDemande + "}";
    }

    public void setId(int id) {
    }
}