package com.gestionhandicap.controller;

import com.gestionhandicap.dao.DemandeDAO;
import com.gestionhandicap.dao.ReclamationDAO;
import com.gestionhandicap.dao.StatistiqueDAO;
import com.gestionhandicap.model.Demande;
import com.gestionhandicap.model.Reclamation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DashboardController {

    private final StatistiqueDAO statistiqueDAO;
    private final DemandeDAO demandeDAO;
    private final ReclamationDAO reclamationDAO;

    public DashboardController() {
        this.statistiqueDAO = new StatistiqueDAO();
        this.demandeDAO = new DemandeDAO();
        this.reclamationDAO = new ReclamationDAO();
    }

    public int getNombreTotalDemandes() {
        return statistiqueDAO.nombreDemandes();
    }

    public int getNombreTotalReclamations() {
        return statistiqueDAO.nombreReclamations();
    }

    public int getNombreDemandesParStatut(String statut) {
        return statistiqueDAO.nombreDemandesParStatut(statut);
    }

    public int getNombreReclamationsParStatut(String statut) {
        return statistiqueDAO.nombreReclamationsParStatut(statut);
    }

    public Map<String, Long> getDemandesParStatut() {
        return demandeDAO.getAllDemandes().stream()
                .collect(Collectors.groupingBy(Demande::getStatut, Collectors.counting()));
    }

    public Map<String, Long> getReclamationsParStatut() {
        return reclamationDAO.getAllReclamations().stream()
                .collect(Collectors.groupingBy(Reclamation::getStatut, Collectors.counting()));
    }

    public List<Demande> filtrerDemandesParStatut(String statut) {
        return demandeDAO.getAllDemandes().stream()
                .filter(d -> statut.equals(d.getStatut()))
                .collect(Collectors.toList());
    }

    public List<Reclamation> filtrerReclamationsParStatut(String statut) {
        return reclamationDAO.getAllReclamations().stream()
                .filter(r -> statut.equals(r.getStatut()))
                .collect(Collectors.toList());
    }

    public List<Demande> filtrerDemandesParPeriode(LocalDateTime debut, LocalDateTime fin) {
        return demandeDAO.getAllDemandes().stream()
                .filter(d -> d.getDateDemande() != null
                        && !d.getDateDemande().isBefore(debut)
                        && !d.getDateDemande().isAfter(fin))
                .collect(Collectors.toList());
    }

    public List<Reclamation> filtrerReclamationsParPeriode(LocalDateTime debut, LocalDateTime fin) {
        return reclamationDAO.getAllReclamations().stream()
                .filter(r -> r.getDateReclamation() != null
                        && !r.getDateReclamation().isBefore(debut)
                        && !r.getDateReclamation().isAfter(fin))
                .collect(Collectors.toList());
    }

    public Map<String, Long> getDemandesParStatutFiltre(LocalDateTime debut, LocalDateTime fin, String type) {
        return demandeDAO.getAllDemandes().stream()
                .filter(d -> debut == null || (d.getDateDemande() != null && !d.getDateDemande().isBefore(debut)))
                .filter(d -> fin   == null || (d.getDateDemande() != null && !d.getDateDemande().isAfter(fin)))
                .filter(d -> type  == null || type.isBlank() || type.equals(d.getType()))
                .collect(Collectors.groupingBy(Demande::getStatut, Collectors.counting()));
    }

    public List<String> getTypesDemandesDistincts() {
        return demandeDAO.getAllDemandes().stream()
                .map(Demande::getType)
                .filter(t -> t != null && !t.isBlank())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public Map<Integer, Long> getDemandesParMois(int annee) {
        return demandeDAO.getAllDemandes().stream()
                .filter(d -> d.getDateDemande() != null && d.getDateDemande().getYear() == annee)
                .collect(Collectors.groupingBy(d -> d.getDateDemande().getMonthValue(), Collectors.counting()));
    }

    public Map<Integer, Long> getReclamationsParMois(int annee) {
        return reclamationDAO.getAllReclamations().stream()
                .filter(r -> r.getDateReclamation() != null && r.getDateReclamation().getYear() == annee)
                .collect(Collectors.groupingBy(r -> r.getDateReclamation().getMonthValue(), Collectors.counting()));
    }
}
