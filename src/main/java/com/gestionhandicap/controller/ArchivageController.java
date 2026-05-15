package com.gestionhandicap.controller;

import com.gestionhandicap.dao.DemandeDAO;
import com.gestionhandicap.dao.ReclamationDAO;
import com.gestionhandicap.dao.UtilisateurDAO;
import com.gestionhandicap.model.ArchiveRecord;
import com.gestionhandicap.model.Demande;
import com.gestionhandicap.model.Reclamation;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ArchivageController {

    private static final Set<String> DEMANDE_DONE   = Set.of("ACCEPTEE", "REFUSEE", "ARCHIVEE");
    private static final Set<String> RECLAM_DONE    = Set.of("TRAITEE",  "REJETEE",  "ARCHIVEE");
    private static final Set<String> DEMANDE_TO_ARC = Set.of("ACCEPTEE", "REFUSEE");
    private static final Set<String> RECLAM_TO_ARC  = Set.of("TRAITEE",  "REJETEE");

    private final DemandeDAO      demandeDAO      = new DemandeDAO();
    private final ReclamationDAO  reclamationDAO  = new ReclamationDAO();
    private final UtilisateurDAO  utilisateurDAO  = new UtilisateurDAO();

    // ── Existing methods ──────────────────────────────────────────────────

    public List<Demande> getDemandesArchivees() {
        return demandeDAO.getAllDemandes().stream()
                .filter(d -> "ARCHIVEE".equals(d.getStatut()))
                .collect(Collectors.toList());
    }

    public List<Reclamation> getReclamationsArchivees() {
        return reclamationDAO.getAllReclamations().stream()
                .filter(r -> "ARCHIVEE".equals(r.getStatut()))
                .collect(Collectors.toList());
    }

    public List<Demande> getHistoriqueDemandesParPersonne(int idPersonne) {
        return demandeDAO.getDemandesByPersonne(idPersonne);
    }

    public List<Reclamation> getHistoriqueReclamationsParPersonne(int idPersonne) {
        return reclamationDAO.getReclamationsByPersonne(idPersonne);
    }

    // ── New methods ───────────────────────────────────────────────────────

    /** Returns all completed demandes + réclamations merged and sorted by date desc. */
    public List<ArchiveRecord> getHistoriqueComplet() {
        List<ArchiveRecord> records = new ArrayList<>();

        demandeDAO.getAllDemandes().stream()
                .filter(d -> DEMANDE_DONE.contains(d.getStatut()))
                .forEach(d -> records.add(new ArchiveRecord(
                        d.getIdDemande(), "Demande",
                        d.getType() != null ? d.getType() : "—",
                        d.getDescription(),
                        d.getDateDemande(), d.getStatut(), d.getIdPersonne()
                )));

        reclamationDAO.getAllReclamations().stream()
                .filter(r -> RECLAM_DONE.contains(r.getStatut()))
                .forEach(r -> records.add(new ArchiveRecord(
                        r.getIdReclamation(), "Réclamation", "—",
                        r.getDescription(),
                        r.getDateReclamation(), r.getStatut(), r.getIdPersonne()
                )));

        records.sort((a, b) -> {
            if (a.getDate() == null) return 1;
            if (b.getDate() == null) return -1;
            return b.getDate().compareTo(a.getDate());
        });
        return records;
    }

    /** Returns id → "Prénom Nom" for all users. */
    public Map<Integer, String> getUtilisateursMap() {
        Map<Integer, String> map = new HashMap<>();
        utilisateurDAO.getAllUtilisateurs()
                .forEach(u -> map.put(u.getId(), u.getPrenom() + " " + u.getNom()));
        return map;
    }

    /**
     * Moves completed demandes/réclamations older than {@code moisSeuil} months
     * to ARCHIVEE status.
     */
    public int archiverAnciensEnregistrements(int moisSeuil) {
        LocalDateTime seuil = LocalDateTime.now().minusMonths(moisSeuil);
        int[] count = {0};

        demandeDAO.getAllDemandes().stream()
                .filter(d -> DEMANDE_TO_ARC.contains(d.getStatut()))
                .filter(d -> d.getDateDemande() != null && d.getDateDemande().isBefore(seuil))
                .forEach(d -> { demandeDAO.updateStatut(d.getIdDemande(), "ARCHIVEE"); count[0]++; });

        reclamationDAO.getAllReclamations().stream()
                .filter(r -> RECLAM_TO_ARC.contains(r.getStatut()))
                .filter(r -> r.getDateReclamation() != null && r.getDateReclamation().isBefore(seuil))
                .forEach(r -> { reclamationDAO.updateStatut(r.getIdReclamation(), "ARCHIVEE"); count[0]++; });

        return count[0];
    }
}
