package com.gestionhandicap.controller;

import com.gestionhandicap.dao.UtilisateurDAO;
import com.gestionhandicap.model.Administrateur;
import com.gestionhandicap.model.PersonneHandicap;
import com.gestionhandicap.model.Utilisateur;
import com.gestionhandicap.util.Session;

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
}
