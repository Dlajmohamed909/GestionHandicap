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

    public void soumettreDemande(Demande demande) {
        demande.setStatut("EN_ATTENTE");
        demande.setIdPersonne(Session.getUtilisateur().getId());
        demandeDAO.ajouterDemande(demande);
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

    public void validerDemande(int idDemande) {
        demandeDAO.updateStatut(idDemande, "VALIDEE");
    }

    public void rejeterDemande(int idDemande) {
        demandeDAO.updateStatut(idDemande, "REJETEE");
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
