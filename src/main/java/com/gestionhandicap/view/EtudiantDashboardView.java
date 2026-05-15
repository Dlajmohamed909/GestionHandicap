package com.gestionhandicap.view;

import com.gestionhandicap.controller.DemandeController;
import com.gestionhandicap.controller.ReclamationController;
import com.gestionhandicap.model.Demande;
import com.gestionhandicap.model.Reclamation;
import com.gestionhandicap.util.Session;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EtudiantDashboardView {

    private static final DateTimeFormatter DATE_FMT   = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter DATE_SHORT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final DemandeController    demandeCtrl = new DemandeController();
    private final ReclamationController reclamCtrl  = new ReclamationController();

    public Node buildView(Stage stage, Runnable navDemandes, Runnable navReclamations) {
        List<Demande>     demandes = demandeCtrl.getMesDemandes();
        List<Reclamation> reclams  = reclamCtrl.getMesReclamations();

        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setStyle("-fx-background-color: white; -fx-background: white;");

        VBox root = new VBox(28);
        root.setPadding(new Insets(36, 50, 40, 50));
        root.setStyle("-fx-background-color: white;");

        root.getChildren().addAll(
            buildWelcomeCard(navDemandes, navReclamations),
            buildStatCards(demandes, reclams),
            buildNotifications(demandes, reclams),
            buildMiniTables(demandes, reclams)
        );

        scroll.setContent(root);
        return scroll;
    }

    // ── Welcome card ──────────────────────────────────────────────────────

    private VBox buildWelcomeCard(Runnable navDemandes, Runnable navReclamations) {
        var user = Session.getUtilisateur();

        Label welcome = new Label("Bienvenue, " + user.getPrenom() + " " + user.getNom() + " !");
        welcome.setStyle(
            "-fx-font-family: 'Segoe UI'; -fx-font-size: 22; -fx-font-weight: bold; -fx-text-fill: white;"
        );

        Label sub = new Label("Étudiant · Université Internationale de Rabat");
        sub.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 13; -fx-text-fill: #BBDEFB;");

        Button btnDemande = new Button("+ Nouvelle demande");
        btnDemande.setStyle(
            "-fx-background-color: white; -fx-text-fill: #1565C0; -fx-font-family: 'Segoe UI';" +
            " -fx-font-size: 13; -fx-font-weight: bold; -fx-padding: 9 20 9 20;" +
            " -fx-background-radius: 6; -fx-cursor: hand;"
        );
        btnDemande.setOnAction(e -> navDemandes.run());

        Button btnReclam = new Button("+ Nouvelle réclamation");
        btnReclam.setStyle(
            "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-family: 'Segoe UI';" +
            " -fx-font-size: 13; -fx-font-weight: bold; -fx-padding: 9 20 9 20;" +
            " -fx-background-radius: 6; -fx-cursor: hand;" +
            " -fx-border-color: white; -fx-border-radius: 6; -fx-border-width: 1.5;"
        );
        btnReclam.setOnAction(e -> navReclamations.run());

        HBox buttons = new HBox(12, btnDemande, btnReclam);
        buttons.setAlignment(Pos.CENTER_LEFT);

        Region gap = new Region();
        gap.setPrefHeight(6);

        VBox card = new VBox(6, welcome, sub, gap, buttons);
        card.setPadding(new Insets(28, 32, 28, 32));
        card.setStyle(
            "-fx-background-color: #1565C0;" +
            "-fx-background-radius: 10;" +
            "-fx-effect: dropshadow(gaussian, rgba(21,101,192,0.28), 14, 0, 0, 4);"
        );
        return card;
    }

    // ── Stat cards ────────────────────────────────────────────────────────

    private HBox buildStatCards(List<Demande> demandes, List<Reclamation> reclams) {
        long total     = demandes.size();
        long enCours   = demandes.stream().filter(d -> "EN_COURS".equals(d.getStatut())).count();
        long acceptee  = demandes.stream().filter(d -> "ACCEPTEE".equals(d.getStatut())).count();
        long refusee   = demandes.stream().filter(d -> "REFUSEE".equals(d.getStatut())).count();
        long totalRecl = reclams.size();

        HBox row = new HBox(14);
        row.setAlignment(Pos.CENTER_LEFT);

        for (HBox card : List.of(
            statCard("Total demandes",  total,     "#1976D2"),
            statCard("En cours",        enCours,   "#F57C00"),
            statCard("Acceptées",       acceptee,  "#388E3C"),
            statCard("Refusées",        refusee,   "#C62828"),
            statCard("Réclamations",    totalRecl, "#7B1FA2")
        )) {
            HBox.setHgrow(card, Priority.ALWAYS);
            row.getChildren().add(card);
        }
        return row;
    }

    private HBox statCard(String label, long value, String color) {
        Region bar = new Region();
        bar.setPrefWidth(5);
        bar.setMinWidth(5);
        bar.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 8 0 0 8;");

        Label numLbl = new Label(String.valueOf(value));
        numLbl.setStyle(
            "-fx-font-family: 'Segoe UI'; -fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: " + color + ";"
        );

        Label txtLbl = new Label(label);
        txtLbl.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12; -fx-text-fill: #5A6578;");

        VBox content = new VBox(4, numLbl, txtLbl);
        content.setPadding(new Insets(16, 16, 16, 14));
        HBox.setHgrow(content, Priority.ALWAYS);

        HBox card = new HBox(bar, content);
        card.setStyle(
            "-fx-background-color: white; -fx-background-radius: 8;" +
            "-fx-border-color: #E0E6EF; -fx-border-radius: 8; -fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 8, 0, 0, 2);"
        );
        return card;
    }

    // ── Notifications ─────────────────────────────────────────────────────

    private VBox buildNotifications(List<Demande> demandes, List<Reclamation> reclams) {
        VBox section = new VBox(10);

        Label title = new Label("Notifications");
        title.setStyle(Theme.SECTION_TITLE_STYLE);
        section.getChildren().add(title);

        record NotifEntry(LocalDateTime date, Node node) {}
        List<NotifEntry> notifs = new ArrayList<>();

        for (Demande d : demandes) {
            String ds = d.getStatut();
            if (ds != null && !"EN_COURS".equals(ds)) {
                notifs.add(new NotifEntry(
                    d.getDateDemande(),
                    notifRow(demandeStatutColor(ds), notifTextDemande(d), d.getDateDemande())
                ));
            }
        }
        for (Reclamation r : reclams) {
            String rs = r.getStatut();
            if (rs != null && !"EN_COURS".equals(rs)) {
                notifs.add(new NotifEntry(
                    r.getDateReclamation(),
                    notifRow(reclamStatutColor(rs), notifTextReclam(r), r.getDateReclamation())
                ));
            }
        }

        notifs.sort((a, b) -> {
            LocalDateTime da = a.date() != null ? a.date() : LocalDateTime.MIN;
            LocalDateTime db = b.date() != null ? b.date() : LocalDateTime.MIN;
            return db.compareTo(da);
        });

        if (notifs.isEmpty()) {
            Label empty = new Label("Aucune notification pour le moment.");
            empty.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 13; -fx-text-fill: #9E9E9E;");
            section.getChildren().add(empty);
        } else {
            notifs.stream().limit(5).forEach(e -> section.getChildren().add(e.node()));
        }
        return section;
    }

    private HBox notifRow(String dotColor, String text, LocalDateTime date) {
        Region dot = new Region();
        dot.setPrefSize(10, 10);
        dot.setMinSize(10, 10);
        dot.setStyle("-fx-background-color: " + dotColor + "; -fx-background-radius: 5;");

        Label textLbl = new Label(text);
        textLbl.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 13; -fx-text-fill: #2D3746;");
        textLbl.setWrapText(true);
        HBox.setHgrow(textLbl, Priority.ALWAYS);

        Label dateLbl = new Label(date != null ? date.format(DATE_FMT) : "");
        dateLbl.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 11; -fx-text-fill: #9E9E9E;");
        dateLbl.setMinWidth(130);

        HBox row = new HBox(10, dot, textLbl, dateLbl);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(10, 16, 10, 16));
        row.setStyle(
            "-fx-background-color: white; -fx-background-radius: 6;" +
            "-fx-border-color: #E0E6EF; -fx-border-radius: 6; -fx-border-width: 1;"
        );
        return row;
    }

    private static String notifTextDemande(Demande d) {
        String type = d.getType() != null ? " (" + d.getType() + ")" : "";
        return switch (d.getStatut()) {
            case "ACCEPTEE" -> "Votre demande #" + d.getIdDemande() + type + " a été acceptée.";
            case "REFUSEE"  -> "Votre demande #" + d.getIdDemande() + type + " a été refusée.";
            case "ARCHIVEE" -> "Votre demande #" + d.getIdDemande() + type + " a été archivée.";
            default         -> "Votre demande #" + d.getIdDemande() + type + " : " + d.getStatut();
        };
    }

    private static String notifTextReclam(Reclamation r) {
        return switch (r.getStatut()) {
            case "TRAITEE"  -> "Votre réclamation #" + r.getIdReclamation() + " a été traitée.";
            case "REJETEE"  -> "Votre réclamation #" + r.getIdReclamation() + " a été rejetée.";
            case "ARCHIVEE" -> "Votre réclamation #" + r.getIdReclamation() + " a été archivée.";
            default         -> "Votre réclamation #" + r.getIdReclamation() + " : " + r.getStatut();
        };
    }

    // ── Mini tables ───────────────────────────────────────────────────────

    private HBox buildMiniTables(List<Demande> demandes, List<Reclamation> reclams) {
        List<Demande> recentDemandes = demandes.stream()
            .sorted((a, b) -> {
                LocalDateTime da = a.getDateDemande() != null ? a.getDateDemande() : LocalDateTime.MIN;
                LocalDateTime db = b.getDateDemande() != null ? b.getDateDemande() : LocalDateTime.MIN;
                return db.compareTo(da);
            })
            .limit(5)
            .toList();

        List<Reclamation> recentReclams = reclams.stream()
            .sorted((a, b) -> {
                LocalDateTime da = a.getDateReclamation() != null ? a.getDateReclamation() : LocalDateTime.MIN;
                LocalDateTime db = b.getDateReclamation() != null ? b.getDateReclamation() : LocalDateTime.MIN;
                return db.compareTo(da);
            })
            .limit(5)
            .toList();

        VBox leftSection  = buildDemandesTable(recentDemandes);
        VBox rightSection = buildReclamationsTable(recentReclams);
        HBox.setHgrow(leftSection,  Priority.ALWAYS);
        HBox.setHgrow(rightSection, Priority.ALWAYS);

        return new HBox(20, leftSection, rightSection);
    }

    private VBox buildDemandesTable(List<Demande> demandes) {
        VBox section = new VBox(10);

        Label title = new Label("Dernières demandes");
        title.setStyle(Theme.SECTION_TITLE_STYLE);

        TableView<Demande> table = new TableView<>(FXCollections.observableArrayList(demandes));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(200);
        table.setMaxHeight(200);
        table.setPlaceholder(new Label("Aucune demande."));

        TableColumn<Demande, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getIdDemande()));
        idCol.setMaxWidth(45); idCol.setMinWidth(45);

        TableColumn<Demande, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getType()));

        TableColumn<Demande, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(c -> {
            var d = c.getValue().getDateDemande();
            return new SimpleStringProperty(d != null ? d.format(DATE_SHORT) : "—");
        });
        dateCol.setMinWidth(90);

        TableColumn<Demande, String> statutCol = new TableColumn<>("Statut");
        statutCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStatut()));
        statutCol.setMinWidth(90);
        statutCol.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String s, boolean empty) {
                super.updateItem(s, empty);
                if (empty || s == null) { setGraphic(null); return; }
                Label badge = new Label(s);
                badge.setStyle(
                    "-fx-background-color: " + demandeStatutColor(s) + ";" +
                    "-fx-text-fill: white; -fx-padding: 2 6 2 6;" +
                    "-fx-background-radius: 4; -fx-font-family: 'Segoe UI';" +
                    "-fx-font-size: 10; -fx-font-weight: bold;"
                );
                setGraphic(badge); setText(null);
            }
        });

        table.getColumns().addAll(idCol, typeCol, dateCol, statutCol);
        section.getChildren().addAll(title, table);
        return section;
    }

    private VBox buildReclamationsTable(List<Reclamation> reclams) {
        VBox section = new VBox(10);

        Label title = new Label("Dernières réclamations");
        title.setStyle(Theme.SECTION_TITLE_STYLE);

        TableView<Reclamation> table = new TableView<>(FXCollections.observableArrayList(reclams));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(200);
        table.setMaxHeight(200);
        table.setPlaceholder(new Label("Aucune réclamation."));

        TableColumn<Reclamation, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getIdReclamation()));
        idCol.setMaxWidth(45); idCol.setMinWidth(45);

        TableColumn<Reclamation, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(c -> {
            String desc = c.getValue().getDescription();
            return new SimpleStringProperty(
                desc != null && desc.length() > 40 ? desc.substring(0, 40) + "…" : desc
            );
        });

        TableColumn<Reclamation, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(c -> {
            var d = c.getValue().getDateReclamation();
            return new SimpleStringProperty(d != null ? d.format(DATE_SHORT) : "—");
        });
        dateCol.setMinWidth(90);

        TableColumn<Reclamation, String> statutCol = new TableColumn<>("Statut");
        statutCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStatut()));
        statutCol.setMinWidth(90);
        statutCol.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String s, boolean empty) {
                super.updateItem(s, empty);
                if (empty || s == null) { setGraphic(null); return; }
                Label badge = new Label(s);
                badge.setStyle(
                    "-fx-background-color: " + reclamStatutColor(s) + ";" +
                    "-fx-text-fill: white; -fx-padding: 2 6 2 6;" +
                    "-fx-background-radius: 4; -fx-font-family: 'Segoe UI';" +
                    "-fx-font-size: 10; -fx-font-weight: bold;"
                );
                setGraphic(badge); setText(null);
            }
        });

        table.getColumns().addAll(idCol, descCol, dateCol, statutCol);
        section.getChildren().addAll(title, table);
        return section;
    }

    // ── Color helpers ─────────────────────────────────────────────────────

    private static String demandeStatutColor(String s) {
        return switch (s) {
            case "EN_COURS" -> "#F57C00";
            case "ACCEPTEE" -> "#388E3C";
            case "REFUSEE"  -> "#C62828";
            case "ARCHIVEE" -> "#607D8B";
            default         -> "#9E9E9E";
        };
    }

    private static String reclamStatutColor(String s) {
        return switch (s) {
            case "EN_COURS" -> "#1976D2";
            case "TRAITEE"  -> "#388E3C";
            case "REJETEE"  -> "#C62828";
            case "ARCHIVEE" -> "#607D8B";
            default         -> "#9E9E9E";
        };
    }
}
