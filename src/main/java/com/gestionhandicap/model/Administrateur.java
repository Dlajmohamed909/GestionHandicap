package com.gestionhandicap.model;

public class Administrateur extends Utilisateur {

    private String matricule;
    private String service;

    public Administrateur() {}

    public Administrateur(int id, String email, String motDePasse, String nom, String prenom,
                          String matricule, String service) {
        super(id, email, motDePasse, nom, prenom, "ADMIN");
        this.matricule = matricule;
        this.service = service;
    }

    // Getters
    public String getMatricule() { return matricule; }
    public String getService() { return service; }

    // Setters
    public void setMatricule(String matricule) { this.matricule = matricule; }
    public void setService(String service) { this.service = service; }

    @Override
    public String toString() {
        return "Administrateur{" + "matricule='" + matricule + "', service='" + service + "', " + super.toString() + "}";
    }
}