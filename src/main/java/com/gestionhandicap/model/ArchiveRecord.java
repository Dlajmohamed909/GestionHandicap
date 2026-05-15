package com.gestionhandicap.model;

import java.time.LocalDateTime;

public class ArchiveRecord {

    private final int id;
    private final String source;       // "Demande" or "Réclamation"
    private final String type;         // demande type, or "—" for réclamation
    private final String description;
    private final LocalDateTime date;
    private final String statut;
    private final int idPersonne;

    public ArchiveRecord(int id, String source, String type, String description,
                         LocalDateTime date, String statut, int idPersonne) {
        this.id          = id;
        this.source      = source;
        this.type        = type;
        this.description = description;
        this.date        = date;
        this.statut      = statut;
        this.idPersonne  = idPersonne;
    }

    public int            getId()          { return id; }
    public String         getSource()      { return source; }
    public String         getType()        { return type; }
    public String         getDescription() { return description; }
    public LocalDateTime  getDate()        { return date; }
    public String         getStatut()      { return statut; }
    public int            getIdPersonne()  { return idPersonne; }
}
