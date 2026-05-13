package com.gestionhandicap.controller;

import com.gestionhandicap.dao.ReclamationDAO;
import com.gestionhandicap.model.Reclamation;
import com.gestionhandicap.util.Session;

import java.util.List;

public class ReclamationController {

    private final ReclamationDAO reclamationDAO;

    public ReclamationController() {
        this.reclamationDAO = new ReclamationDAO();
    }

    public void soumettreReclamation(Reclamation reclamation) {
        reclamation.setStatut("EN_ATTENTE");
        reclamation.setIdPersonne(Session.getUtilisateur().getId());
        reclamationDAO.ajouterReclamation(reclamation);
    }

    public List<Reclamation> getAllReclamations() {
        return reclamationDAO.getAllReclamations();
    }

    public List<Reclamation> getMesReclamations() {
        int idPersonne = Session.getUtilisateur().getId();
        return reclamationDAO.getReclamationsByPersonne(idPersonne);
    }

    public List<Reclamation> getReclamationsByPersonne(int idPersonne) {
        return reclamationDAO.getReclamationsByPersonne(idPersonne);
    }

    public void traiterReclamation(int idReclamation) {
        reclamationDAO.updateStatut(idReclamation, "EN_COURS");
    }

    public void resoudreReclamation(int idReclamation) {
        reclamationDAO.updateStatut(idReclamation, "RESOLUE");
    }

    public void archiverReclamation(int idReclamation) {
        reclamationDAO.updateStatut(idReclamation, "ARCHIVEE");
    }

    public void supprimerReclamation(int idReclamation) {
        reclamationDAO.supprimerReclamation(idReclamation);
    }
}
