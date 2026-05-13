package com.gestionhandicap.controller;

import com.gestionhandicap.dao.DemandeDAO;
import com.gestionhandicap.dao.ReclamationDAO;
import com.gestionhandicap.model.Demande;
import com.gestionhandicap.model.Reclamation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ArchivageController {

    private static final String STATUT_ARCHIVEE = "ARCHIVEE";

    private final DemandeDAO demandeDAO;
    private final ReclamationDAO reclamationDAO;

    public ArchivageController() {
        this.demandeDAO = new DemandeDAO();
        this.reclamationDAO = new ReclamationDAO();
    }

    public List<Demande> getDemandesArchivees() {
        return demandeDAO.getAllDemandes().stream()
                .filter(d -> STATUT_ARCHIVEE.equals(d.getStatut()))
                .collect(Collectors.toList());
    }

    public List<Reclamation> getReclamationsArchivees() {
        return reclamationDAO.getAllReclamations().stream()
                .filter(r -> STATUT_ARCHIVEE.equals(r.getStatut()))
                .collect(Collectors.toList());
    }

    public List<Demande> rechercherDemandes(String motCle) {
        String cle = motCle == null ? "" : motCle.toLowerCase();
        return demandeDAO.getAllDemandes().stream()
                .filter(d -> d.getType().toLowerCase().contains(cle)
                        || d.getDescription().toLowerCase().contains(cle))
                .collect(Collectors.toList());
    }

    public List<Reclamation> rechercherReclamations(String motCle) {
        String cle = motCle == null ? "" : motCle.toLowerCase();
        return reclamationDAO.getAllReclamations().stream()
                .filter(r -> r.getDescription().toLowerCase().contains(cle))
                .collect(Collectors.toList());
    }

    public List<Demande> getHistoriqueDemandesParPersonne(int idPersonne) {
        return demandeDAO.getDemandesByPersonne(idPersonne);
    }

    public List<Reclamation> getHistoriqueReclamationsParPersonne(int idPersonne) {
        return reclamationDAO.getReclamationsByPersonne(idPersonne);
    }

    public List<Demande> rechercherDemandesParPeriode(LocalDateTime debut, LocalDateTime fin) {
        return demandeDAO.getAllDemandes().stream()
                .filter(d -> d.getDateDemande() != null
                        && !d.getDateDemande().isBefore(debut)
                        && !d.getDateDemande().isAfter(fin))
                .collect(Collectors.toList());
    }

    public List<Reclamation> rechercherReclamationsParPeriode(LocalDateTime debut, LocalDateTime fin) {
        return reclamationDAO.getAllReclamations().stream()
                .filter(r -> r.getDateReclamation() != null
                        && !r.getDateReclamation().isBefore(debut)
                        && !r.getDateReclamation().isAfter(fin))
                .collect(Collectors.toList());
    }
}
