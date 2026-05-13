package com.gestionhandicap.model;

public class PersonneHandicap extends Utilisateur {

    private String numEtudiant;
    private String typeHandicap;
    private String filiere;

    public PersonneHandicap() {}

    public PersonneHandicap(int id, String email, String motDePasse, String nom, String prenom,
                            String numEtudiant, String typeHandicap, String filiere) {
        super(id, email, motDePasse, nom, prenom, "HANDICAP");
        this.numEtudiant = numEtudiant;
        this.typeHandicap = typeHandicap;
        this.filiere = filiere;
    }

    // Getters
    public String getNumEtudiant() { return numEtudiant; }
    public String getTypeHandicap() { return typeHandicap; }
    public String getFiliere() { return filiere; }

    // Setters
    public void setNumEtudiant(String numEtudiant) { this.numEtudiant = numEtudiant; }
    public void setTypeHandicap(String typeHandicap) { this.typeHandicap = typeHandicap; }
    public void setFiliere(String filiere) { this.filiere = filiere; }

    @Override
    public String toString() {
        return "PersonneHandicap{" + "numEtudiant='" + numEtudiant + "', typeHandicap='" + typeHandicap + "', filiere='" + filiere + "'}";
    }
}