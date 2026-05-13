package com.gestionhandicap.model;

import java.time.LocalDateTime;

public class Utilisateur {

    private int id;
    private String email;
    private String motDePasse;
    private String nom;
    private String prenom;
    private String role;
    private LocalDateTime dateCreation;

    public Utilisateur() {}

    public Utilisateur(int id, String email, String motDePasse, String nom, String prenom, String role) {
        this.id = id;
        this.email = email;
        this.motDePasse = motDePasse;
        this.nom = nom;
        this.prenom = prenom;
        this.role = role;
    }

    // Getters
    public int getId() { return id; }
    public String getEmail() { return email; }
    public String getMotDePasse() { return motDePasse; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getRole() { return role; }
    public LocalDateTime getDateCreation() { return dateCreation; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setRole(String role) { this.role = role; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public String getNomComplet() {
        return prenom + " " + nom;
    }

    @Override
    public String toString() {
        return "Utilisateur{" + "id=" + id + ", email='" + email + "', nom='" + getNomComplet() + "', role='" + role + "'}";
    }

    public String getPassword() {
        return "";
    }

    public void setPassword(String password) {
    }
}