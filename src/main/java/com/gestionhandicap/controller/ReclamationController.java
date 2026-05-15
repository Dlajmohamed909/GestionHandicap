package com.gestionhandicap.controller;

import com.gestionhandicap.dao.ReclamationDAO;
import com.gestionhandicap.dao.ReclamationHistoriqueDAO;
import com.gestionhandicap.model.Reclamation;
import com.gestionhandicap.model.ReclamationHistorique;
import com.gestionhandicap.util.Session;

import java.util.List;

public class ReclamationController {

    private final ReclamationDAO reclamationDAO;
    private final ReclamationHistoriqueDAO historiqueDAO;

    public ReclamationController() {
        this.reclamationDAO  = new ReclamationDAO();
        this.historiqueDAO   = new ReclamationHistoriqueDAO();
    }

    public void soumettreReclamation(Reclamation reclamation) {
        reclamation.setStatut("EN_COURS");
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

    public void modifierReclamation(int idReclamation, String description) {
        reclamationDAO.modifierReclamation(idReclamation, description);
    }

    public void mettreAJourStatut(int idReclamation, String ancienStatut, String nouveauStatut) {
        reclamationDAO.updateStatut(idReclamation, nouveauStatut);
        historiqueDAO.ajouterHistorique(idReclamation, ancienStatut, nouveauStatut);
    }

    public List<ReclamationHistorique> getHistorique(int idReclamation) {
        return historiqueDAO.getHistoriqueByReclamation(idReclamation);
    }

    public void traiterReclamation(int idReclamation) {
        reclamationDAO.updateStatut(idReclamation, "EN_COURS");
    }

    public void resoudreReclamation(int idReclamation) {
        reclamationDAO.updateStatut(idReclamation, "TRAITEE");
    }

    public void archiverReclamation(int idReclamation) {
        reclamationDAO.updateStatut(idReclamation, "ARCHIVEE");
    }

    public void supprimerReclamation(int idReclamation) {
        reclamationDAO.supprimerReclamation(idReclamation);
    }
}
