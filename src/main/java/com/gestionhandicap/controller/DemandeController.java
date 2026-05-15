package com.gestionhandicap.controller;

import com.gestionhandicap.dao.DemandeDAO;
import com.gestionhandicap.dao.PieceJustificativeDAO;
import com.gestionhandicap.model.Demande;
import com.gestionhandicap.model.PieceJustificative;
import com.gestionhandicap.util.Session;

import java.util.List;

public class DemandeController {

    private final DemandeDAO demandeDAO;
    private final PieceJustificativeDAO pieceDAO;

    public DemandeController() {
        this.demandeDAO = new DemandeDAO();
        this.pieceDAO = new PieceJustificativeDAO();
    }

    public int soumettreDemande(Demande demande) {
        demande.setStatut("EN_COURS");
        demande.setIdPersonne(Session.getUtilisateur().getId());
        return demandeDAO.ajouterDemande(demande);
    }

    public List<Demande> getAllDemandes() {
        return demandeDAO.getAllDemandes();
    }

    public List<Demande> getMesDemandes() {
        int idPersonne = Session.getUtilisateur().getId();
        return demandeDAO.getDemandesByPersonne(idPersonne);
    }

    public List<Demande> getDemandesByPersonne(int idPersonne) {
        return demandeDAO.getDemandesByPersonne(idPersonne);
    }

    public void modifierDemande(int idDemande, String type, String description) {
        demandeDAO.modifierDemande(idDemande, type, description);
    }

    public void mettreAJourStatut(int idDemande, String statut) {
        demandeDAO.updateStatut(idDemande, statut);
    }

    public void validerDemande(int idDemande) {
        demandeDAO.updateStatut(idDemande, "ACCEPTEE");
    }

    public void rejeterDemande(int idDemande) {
        demandeDAO.updateStatut(idDemande, "REFUSEE");
    }

    public void archiverDemande(int idDemande) {
        demandeDAO.updateStatut(idDemande, "ARCHIVEE");
    }

    public void supprimerDemande(int idDemande) {
        demandeDAO.supprimerDemande(idDemande);
    }

    public void ajouterPiece(PieceJustificative piece) {
        pieceDAO.ajouterPiece(piece);
    }

    public List<PieceJustificative> getPiecesByDemande(int idDemande) {
        return pieceDAO.getPiecesByDemande(idDemande);
    }
}
