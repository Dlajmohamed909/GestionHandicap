package com.gestionhandicap.model;

import java.time.LocalDateTime;

public class ReclamationHistorique {

    private int id;
    private int idReclamation;
    private String ancienStatut;
    private String nouveauStatut;
    private LocalDateTime dateChangement;

    public ReclamationHistorique() {}

    public ReclamationHistorique(int id, int idReclamation,
                                  String ancienStatut, String nouveauStatut,
                                  LocalDateTime dateChangement) {
        this.id = id;
        this.idReclamation = idReclamation;
        this.ancienStatut = ancienStatut;
        this.nouveauStatut = nouveauStatut;
        this.dateChangement = dateChangement;
    }

    public int getId()                          { return id; }
    public void setId(int id)                   { this.id = id; }

    public int getIdReclamation()               { return idReclamation; }
    public void setIdReclamation(int v)         { this.idReclamation = v; }

    public String getAncienStatut()             { return ancienStatut; }
    public void setAncienStatut(String v)       { this.ancienStatut = v; }

    public String getNouveauStatut()            { return nouveauStatut; }
    public void setNouveauStatut(String v)      { this.nouveauStatut = v; }

    public LocalDateTime getDateChangement()    { return dateChangement; }
    public void setDateChangement(LocalDateTime v) { this.dateChangement = v; }
}
