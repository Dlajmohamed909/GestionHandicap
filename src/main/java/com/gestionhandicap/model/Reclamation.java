package com.gestionhandicap.model;

import java.time.LocalDateTime;

public class Reclamation {

    private int idReclamation;
    private String description;
    private LocalDateTime dateReclamation;
    private String statut;
    private int idPersonne;
    private int idAdmin;

    public Reclamation() {}

    public Reclamation(int idReclamation, String description, String statut,
                       int idPersonne, int idAdmin) {
        this.idReclamation = idReclamation;
        this.description = description;
        this.statut = statut;
        this.idPersonne = idPersonne;
        this.idAdmin = idAdmin;
    }

    // Getters
    public int getIdReclamation() { return idReclamation; }
    public String getDescription() { return description; }
    public LocalDateTime getDateReclamation() { return dateReclamation; }
    public String getStatut() { return statut; }
    public int getIdPersonne() { return idPersonne; }
    public int getIdAdmin() { return idAdmin; }

    // Setters
    public void setIdReclamation(int idReclamation) { this.idReclamation = idReclamation; }
    public void setDescription(String description) { this.description = description; }
    public void setDateReclamation(LocalDateTime dateReclamation) { this.dateReclamation = dateReclamation; }
    public void setStatut(String statut) { this.statut = statut; }
    public void setIdPersonne(int idPersonne) { this.idPersonne = idPersonne; }
    public void setIdAdmin(int idAdmin) { this.idAdmin = idAdmin; }

    @Override
    public String toString() {
        return "Reclamation{" + "id=" + idReclamation + ", statut='" + statut + "', date=" + dateReclamation + "}";
    }
}