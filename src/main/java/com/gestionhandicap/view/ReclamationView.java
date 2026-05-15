package com.gestionhandicap.view;

import com.gestionhandicap.controller.ReclamationController;
import com.gestionhandicap.model.Reclamation;
import com.gestionhandicap.model.ReclamationHistorique;
import com.gestionhandicap.util.Session;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class ReclamationView {

    private static final DateTimeFormatter DATE_FMT     = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter DATE_SHORT   = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final String[] ADMIN_STATUTS = {"EN_COURS", "TRAITEE", "REJETEE"};

    private final ReclamationController controller = new ReclamationController();
    private final ObservableList<Reclamation> items  = FXCollections.observableArrayList();
    private boolean isAdmin;

    private TableView<Reclamation> table;
    private VBox historyPanel;

    public Node buildView(Stage stage) {
        isAdmin = Session.isAdmin();

        VBox root = new VBox(24);
        root.setPadding(new Insets(35, 50, 35, 50));
        root.setStyle("-fx-background-color: white;");
        VBox.setVgrow(root, Priority.ALWAYS);

        Label title = new Label("Gestion des réclamations");
        title.setStyle(Theme.TITLE_STYLE);
        root.getChildren().add(title);

        if (!isAdmin) {
            root.getChildren().add(buildSubmitForm(stage));
        }

        VBox tableSection = buildTable(stage);
        VBox.setVgrow(tableSection, Priority.ALWAYS);
        root.getChildren().add(tableSection);

        if (!isAdmin) {
            historyPanel = buildEmptyHistoryPanel();
            root.getChildren().add(historyPanel);

            table.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, selected) -> {
                    if (selected != null) {
                        refreshHistoryPanel(selected);
                        historyPanel.setVisible(true);
                        historyPanel.setManaged(true);
                    } else {
                        historyPanel.setVisible(false);
                        historyPanel.setManaged(false);
                    }
                }
            );
        }

        loadData();
        return root;
    }

    // ── Submit form (students only) ───────────────────────────────────────

    private VBox buildSubmitForm(Stage stage) {
        VBox section = new VBox(12);
        section.setStyle("-fx-background-color: " + Theme.LIGHT_GRAY +
                         "; -fx-background-radius: 8; -fx-padding: 20;");

        Label sectionTitle = new Label("Soumettre une réclamation");
        sectionTitle.setStyle(Theme.SECTION_TITLE_STYLE);

        TextArea descArea = new TextArea();
        descArea.setPromptText("Décrivez votre réclamation…");
        descArea.setPrefRowCount(4);
        descArea.setWrapText(true);

        Label errLabel = new Label();
        errLabel.setStyle("-fx-text-fill: #C62828; -fx-font-size: 12; -fx-font-family: 'Segoe UI';");
        errLabel.setVisible(false); errLabel.setManaged(false);

        Button submitBtn = new Button("Envoyer la réclamation");
        submitBtn.setStyle(Theme.BTN_PRIMARY);
        submitBtn.setOnAction(e -> {
            String desc = descArea.getText().trim();
            if (desc.isEmpty()) {
                errLabel.setText("Veuillez saisir une description.");
                errLabel.setVisible(true); errLabel.setManaged(true);
                return;
            }
            errLabel.setVisible(false); errLabel.setManaged(false);

            Reclamation r = new Reclamation();
            r.setDescription(desc);
            controller.soumettreReclamation(r);
            descArea.clear();
            loadData();
            alert(stage, "Réclamation envoyée avec succès.");
        });

        section.getChildren().addAll(sectionTitle, descArea, errLabel, submitBtn);
        return section;
    }

    // ── Table ─────────────────────────────────────────────────────────────

    private VBox buildTable(Stage stage) {
        VBox section = new VBox(10);
        VBox.setVgrow(section, Priority.ALWAYS);

        Label sectionTitle = new Label(isAdmin ? "Toutes les réclamations" : "Mes réclamations");
        sectionTitle.setStyle(Theme.SECTION_TITLE_STYLE);

        if (!isAdmin) {
            Label hint = new Label(
                "Cliquez sur une réclamation pour voir l'historique de ses changements de statut."
            );
            hint.setStyle(
                "-fx-font-family: 'Segoe UI'; -fx-font-size: 12; -fx-text-fill: #1565C0;" +
                "-fx-background-color: #E3F2FD; -fx-background-radius: 6; -fx-padding: 8 12 8 12;"
            );
            hint.setWrapText(true);
            section.getChildren().add(hint);
        }

        table = new TableView<>(items);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);
        table.setPlaceholder(new Label("Aucune réclamation."));

        TableColumn<Reclamation, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getIdReclamation()));
        idCol.setMaxWidth(55); idCol.setMinWidth(55);

        TableColumn<Reclamation, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDescription()));

        TableColumn<Reclamation, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(c -> {
            var d = c.getValue().getDateReclamation();
            return new SimpleStringProperty(d != null ? d.format(DATE_FMT) : "—");
        });
        dateCol.setMinWidth(125);

        TableColumn<Reclamation, String> statutCol = new TableColumn<>("Statut");
        statutCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStatut()));
        statutCol.setMinWidth(110);
        statutCol.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String statut, boolean empty) {
                super.updateItem(statut, empty);
                if (empty || statut == null) { setGraphic(null); return; }
                Label badge = new Label(statut);
                badge.setStyle(
                    "-fx-background-color: " + statutColor(statut) + ";" +
                    "-fx-text-fill: white; -fx-padding: 3 8 3 8;" +
                    "-fx-background-radius: 4; -fx-font-family: 'Segoe UI';" +
                    "-fx-font-size: 11; -fx-font-weight: bold;"
                );
                setGraphic(badge); setText(null);
            }
        });

        TableColumn<Reclamation, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setMinWidth(isAdmin ? 370 : 160);
        actionsCol.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Void v, boolean empty) {
                super.updateItem(v, empty);
                if (empty || getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null); return;
                }
                Reclamation r = getTableView().getItems().get(getIndex());
                setGraphic(buildActionBox(r, stage));
            }
        });

        table.getColumns().addAll(idCol, descCol, dateCol, statutCol, actionsCol);
        section.getChildren().addAll(sectionTitle, table);
        return section;
    }

    private HBox buildActionBox(Reclamation r, Stage stage) {
        HBox box = new HBox(5);
        box.setAlignment(Pos.CENTER_LEFT);

        if (isAdmin) {
            ComboBox<String> statutBox = new ComboBox<>();
            statutBox.getItems().addAll(ADMIN_STATUTS);
            statutBox.setValue(r.getStatut());
            statutBox.setPrefWidth(110);

            Button appliquerBtn = new Button("Appliquer");
            appliquerBtn.setStyle(Theme.BTN_PRIMARY);
            appliquerBtn.setOnAction(e -> {
                String newStatut = statutBox.getValue();
                if (newStatut != null && !newStatut.equals(r.getStatut())) {
                    controller.mettreAJourStatut(r.getIdReclamation(), r.getStatut(), newStatut);
                    loadData();
                }
            });

            Separator sep = new Separator();
            sep.setOrientation(javafx.geometry.Orientation.VERTICAL);
            sep.setPadding(new Insets(0, 2, 0, 2));

            Button modifierBtn  = new Button("Modifier");  modifierBtn.setStyle(Theme.BTN_SECONDARY);
            Button supprimerBtn = new Button("Supprimer"); supprimerBtn.setStyle(Theme.BTN_DANGER);

            modifierBtn.setOnAction(e  -> openEditDialog(r, stage));
            supprimerBtn.setOnAction(e -> confirmSupprimer(r.getIdReclamation(), stage));

            box.getChildren().addAll(statutBox, appliquerBtn, sep, modifierBtn, supprimerBtn);
        } else {
            Button modifierBtn  = new Button("Modifier");  modifierBtn.setStyle(Theme.BTN_SECONDARY);
            Button supprimerBtn = new Button("Supprimer"); supprimerBtn.setStyle(Theme.BTN_DANGER);

            modifierBtn.setOnAction(e  -> openEditDialog(r, stage));
            supprimerBtn.setOnAction(e -> confirmSupprimer(r.getIdReclamation(), stage));

            box.getChildren().addAll(modifierBtn, supprimerBtn);
        }
        return box;
    }

    // ── History panel (students only) ─────────────────────────────────────

    private VBox buildEmptyHistoryPanel() {
        VBox panel = new VBox(12);
        panel.setStyle(
            "-fx-background-color: #F8FAFF; -fx-background-radius: 8;" +
            "-fx-border-color: #BBDEFB; -fx-border-radius: 8; -fx-border-width: 1;" +
            "-fx-padding: 20;"
        );
        panel.setVisible(false);
        panel.setManaged(false);
        return panel;
    }

    private void refreshHistoryPanel(Reclamation r) {
        historyPanel.getChildren().clear();

        Label title = new Label("Historique — Réclamation #" + r.getIdReclamation());
        title.setStyle(
            "-fx-font-family: 'Segoe UI'; -fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: #1565C0;"
        );

        Label currentLabel = new Label("Statut actuel : ");
        currentLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12; -fx-text-fill: #5A6578;");
        Label currentBadge = new Label(r.getStatut());
        currentBadge.setStyle(
            "-fx-background-color: " + statutColor(r.getStatut()) + ";" +
            "-fx-text-fill: white; -fx-padding: 3 10 3 10; -fx-background-radius: 4;" +
            "-fx-font-family: 'Segoe UI'; -fx-font-size: 11; -fx-font-weight: bold;"
        );
        HBox currentRow = new HBox(6, currentLabel, currentBadge);
        currentRow.setAlignment(Pos.CENTER_LEFT);

        historyPanel.getChildren().addAll(title, currentRow);

        List<ReclamationHistorique> historique = controller.getHistorique(r.getIdReclamation());

        if (historique.isEmpty()) {
            Label empty = new Label("Aucun changement de statut enregistré.");
            empty.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12; -fx-text-fill: #9E9E9E;");
            historyPanel.getChildren().add(empty);
            return;
        }

        Separator sep = new Separator();
        sep.setPadding(new Insets(4, 0, 4, 0));
        historyPanel.getChildren().add(sep);

        Label changesTitle = new Label("Changements de statut :");
        changesTitle.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12; -fx-text-fill: #5A6578;");
        historyPanel.getChildren().add(changesTitle);

        for (ReclamationHistorique h : historique) {
            HBox entry = buildHistoryEntry(h);
            historyPanel.getChildren().add(entry);
        }
    }

    private HBox buildHistoryEntry(ReclamationHistorique h) {
        Label dateLbl = new Label(
            h.getDateChangement() != null ? h.getDateChangement().format(DATE_SHORT) : "—"
        );
        dateLbl.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 11; -fx-text-fill: #9E9E9E;");
        dateLbl.setMinWidth(120);

        Label arrow = new Label("→");
        arrow.setStyle("-fx-font-size: 13; -fx-text-fill: #90A4AE; -fx-padding: 0 6 0 6;");

        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(6, 10, 6, 10));
        row.setStyle(
            "-fx-background-color: white; -fx-background-radius: 6;" +
            "-fx-border-color: #E0E6EF; -fx-border-radius: 6; -fx-border-width: 1;"
        );

        if (h.getAncienStatut() != null) {
            Label oldBadge = badge(h.getAncienStatut());
            row.getChildren().addAll(dateLbl, oldBadge, arrow, badge(h.getNouveauStatut()));
        } else {
            Label created = new Label("Création");
            created.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 11; -fx-text-fill: #9E9E9E;");
            row.getChildren().addAll(dateLbl, created, arrow, badge(h.getNouveauStatut()));
        }

        return row;
    }

    private static Label badge(String statut) {
        Label b = new Label(statut);
        b.setStyle(
            "-fx-background-color: " + statutColor(statut) + ";" +
            "-fx-text-fill: white; -fx-padding: 3 8 3 8; -fx-background-radius: 4;" +
            "-fx-font-family: 'Segoe UI'; -fx-font-size: 11; -fx-font-weight: bold;"
        );
        return b;
    }

    // ── Edit dialog ───────────────────────────────────────────────────────

    private void openEditDialog(Reclamation r, Stage stage) {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Modifier la réclamation #" + r.getIdReclamation());
        dlg.initOwner(stage);
        dlg.getDialogPane().setPrefWidth(440);

        TextArea descArea = new TextArea(r.getDescription());
        descArea.setPrefRowCount(5);
        descArea.setWrapText(true);

        Label errLbl = new Label("La description ne peut pas être vide.");
        errLbl.setStyle("-fx-text-fill: #C62828; -fx-font-size: 12;");
        errLbl.setVisible(false); errLbl.setManaged(false);

        VBox content = new VBox(8,
            new Label("Description :") {{ setStyle(Theme.LABEL_STYLE); }},
            descArea, errLbl
        );
        content.setPadding(new Insets(16, 20, 4, 20));
        dlg.getDialogPane().setContent(content);

        ButtonType saveType   = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelType = new ButtonType("Annuler",     ButtonBar.ButtonData.CANCEL_CLOSE);
        dlg.getDialogPane().getButtonTypes().addAll(saveType, cancelType);

        Node saveNode = dlg.getDialogPane().lookupButton(saveType);
        saveNode.addEventFilter(ActionEvent.ACTION, ae -> {
            if (descArea.getText().isBlank()) {
                errLbl.setVisible(true); errLbl.setManaged(true);
                ae.consume();
            }
        });

        dlg.setResultConverter(bt -> bt);
        Optional<ButtonType> result = dlg.showAndWait();
        result.ifPresent(bt -> {
            if (bt == saveType) {
                controller.modifierReclamation(r.getIdReclamation(), descArea.getText().trim());
                loadData();
            }
        });
    }

    private void confirmSupprimer(int idReclamation, Stage stage) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
            "Supprimer cette réclamation ?", ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);
        confirm.initOwner(stage);
        confirm.showAndWait()
            .filter(res -> res == ButtonType.YES)
            .ifPresent(res -> {
                controller.supprimerReclamation(idReclamation);
                if (historyPanel != null) {
                    historyPanel.setVisible(false);
                    historyPanel.setManaged(false);
                }
                loadData();
            });
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private void loadData() {
        items.setAll(isAdmin ? controller.getAllReclamations() : controller.getMesReclamations());
    }

    private static String statutColor(String statut) {
        return switch (statut) {
            case "EN_COURS"  -> "#1976D2";
            case "TRAITEE"   -> "#388E3C";
            case "REJETEE"   -> "#C62828";
            case "ARCHIVEE"  -> "#607D8B";
            default          -> "#9E9E9E";
        };
    }

    private static void alert(Stage owner, String message) {
        Alert ok = new Alert(Alert.AlertType.INFORMATION);
        ok.setTitle("Succès"); ok.setHeaderText(null); ok.setContentText(message);
        ok.initOwner(owner); ok.showAndWait();
    }
}
