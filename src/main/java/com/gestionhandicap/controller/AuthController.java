package com.gestionhandicap.controller;

import com.gestionhandicap.dao.UtilisateurDAO;
import com.gestionhandicap.model.Administrateur;
import com.gestionhandicap.model.PersonneHandicap;
import com.gestionhandicap.model.Utilisateur;
import com.gestionhandicap.util.Session;

import java.util.List;

public class AuthController {

    private final UtilisateurDAO utilisateurDAO;

    public AuthController() {
        this.utilisateurDAO = new UtilisateurDAO();
    }

    public boolean login(String email, String motDePasse) {
        if (email == null || email.isBlank() || motDePasse == null || motDePasse.isBlank()) {
            return false;
        }
        Utilisateur u = utilisateurDAO.authentifier(email, motDePasse);
        if (u != null) {
            Session.setUtilisateur(u);
            return true;
        }
        return false;
    }

    public void logout() {
        Session.logout();
    }

    public void inscrirePersonneHandicap(PersonneHandicap ph) {
        utilisateurDAO.ajouterUtilisateur(ph);
    }

    public void inscrireAdmin(Administrateur admin) {
        utilisateurDAO.ajouterUtilisateur(admin);
    }

    public Utilisateur getUtilisateurConnecte() {
        return Session.getUtilisateur();
    }

    public boolean isAdmin() {
        Utilisateur u = Session.getUtilisateur();
        return u != null && "ADMIN".equals(u.getRole());
    }

    public boolean isHandicap() {
        Utilisateur u = Session.getUtilisateur();
        return u != null && "HANDICAP".equals(u.getRole());
    }

    public Utilisateur getByEmail(String email) {
        return utilisateurDAO.getByEmail(email);
    }

    /** Updates the user's profile and refreshes the Session if it's the logged-in user. */
    public void modifierProfil(int id, String nom, String prenom, String email, String motDePasse) {
        utilisateurDAO.modifierUtilisateur(id, nom, prenom, email, motDePasse);
        Utilisateur current = Session.getUtilisateur();
        if (current != null && current.getId() == id) {
            current.setNom(nom);
            current.setPrenom(prenom);
            current.setEmail(email);
            current.setMotDePasse(motDePasse);
        }
    }

    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurDAO.getAllUtilisateurs();
    }

    public void supprimerUtilisateur(int id) {
        utilisateurDAO.supprimerUtilisateur(id);
    }
}
