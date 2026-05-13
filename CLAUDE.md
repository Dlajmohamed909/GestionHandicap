# GestionHandicap

Desktop application for managing requests and complaints from disabled students at a university.

## Tech Stack

- **Java 17**
- **JavaFX** (UI)
- **MySQL** via JDBC — port **6872**
- Architecture: **MVC**

## Roles

| Role | Description |
|---|---|
| `ADMIN` | Administrateur — reviews and processes requests/complaints |
| `HANDICAP` | PersonneHandicap — disabled student who submits requests/complaints |

## Modules

1. **Authentication** — login, session management (`util/Session.java`)
2. **Demandes** — submission and processing of accommodation requests
3. **Reclamations** — submission and processing of complaints
4. **Dashboard** — statistics and overview for admins (`StatistiqueDAO`)
5. **Archivage** — archiving of closed/resolved records

## Project Structure

```
src/main/java/com/gestionhandicap/
├── model/
│   ├── Utilisateur.java          # Base user (id, nom, prenom, email, motDePasse, role, dateCreation)
│   ├── Administrateur.java       # Extends Utilisateur — adds matricule, service
│   ├── PersonneHandicap.java     # Extends Utilisateur — adds numEtudiant, typeHandicap, filiere
│   ├── Demande.java              # idDemande, type, description, statut, dateDemande, idPersonne, idAdmin
│   ├── PieceJustificative.java   # idPiece, nomFichier, descPiece, cheminFichier, dateAjout, idDemande
│   └── Reclamation.java          # idReclamation, description, statut, dateReclamation, idPersonne, idAdmin
├── dao/
│   ├── UtilisateurDAO.java
│   ├── DemandeDAO.java
│   ├── PieceJustificativeDAO.java
│   ├── ReclamationDAO.java
│   └── StatistiqueDAO.java
└── util/
    ├── DatabaseConnection.java   # JDBC connection (port 6872)
    ├── Session.java              # Current logged-in user
    └── FileUtil.java

```

## Database Column Conventions

Snake_case column names mapped to camelCase Java fields:

| Java field | DB column |
|---|---|
| `motDePasse` | `mot_de_passe` |
| `dateCreation` | `date_creation` |
| `idDemande` | `id_demande` |
| `dateDemande` | `date_demande` |
| `idPersonne` | `id_personne` |
| `idAdmin` | `id_admin` |
| `idPiece` | `id_piece` |
| `descPiece` | `desc_piece` |
| `cheminFichier` | `chemin_fichier` |
| `dateAjout` | `date_ajout` |
| `idReclamation` | `id_reclamation` |
| `dateReclamation` | `date_reclamation` |
