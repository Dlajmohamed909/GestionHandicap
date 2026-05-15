package com.gestionhandicap.view;

import com.gestionhandicap.controller.DemandeController;
import com.gestionhandicap.model.Demande;
import com.gestionhandicap.model.PieceJustificative;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class DemandeView {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final String[] TYPES = {"Aménagement d'examen", "Accessibilité", "Accompagnement"};
    private static final String[] ADMIN_STATUTS = {"EN_COURS", "ACCEPTEE", "REFUSEE"};

    private final DemandeController controller = new DemandeController();
    private final ObservableList<Demande> items = FXCollections.observableArrayList();
    private boolean isAdmin;

    public Node buildView(Stage stage) {
        isAdmin = Session.isAdmin();

        VBox root = new VBox(24);
        root.setPadding(new Insets(35, 50, 35, 50));
        root.setStyle("-fx-background-color: white;");
        VBox.setVgrow(root, Priority.ALWAYS);

        Label title = new Label("Gestion des demandes");
        title.setStyle(Theme.TITLE_STYLE);
        root.getChildren().add(title);

        if (!isAdmin) {
            root.getChildren().add(buildSubmitForm(stage));
        }

        VBox tableSection = buildTable(stage);
        VBox.setVgrow(tableSection, Priority.ALWAYS);
        root.getChildren().add(tableSection);

        loadData();
        return root;
    }

    // ── Submit form (students only) ───────────────────────────────────────

    private VBox buildSubmitForm(Stage stage) {
        VBox section = new VBox(12);
        section.setStyle("-fx-background-color: " + Theme.LIGHT_GRAY +
                         "; -fx-background-radius: 8; -fx-padding: 20;");

        Label sectionTitle = new Label("Soumettre une demande");
        sectionTitle.setStyle(Theme.SECTION_TITLE_STYLE);

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(12);
        ColumnConstraints lCol = new ColumnConstraints(155);
        ColumnConstraints fCol = new ColumnConstraints();
        fCol.setHgrow(Priority.ALWAYS);
        fCol.setFillWidth(true);
        grid.getColumnConstraints().addAll(lCol, fCol);

        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll(TYPES);
        typeBox.setPromptText("Sélectionner un type");
        typeBox.setMaxWidth(Double.MAX_VALUE);

        TextArea descArea = new TextArea();
        descArea.setPromptText("Décrivez votre demande…");
        descArea.setPrefRowCount(3);
        descArea.setWrapText(true);

        final File[] selectedFile = {null};
        Label fileLabel = new Label("Aucun fichier sélectionné");
        fileLabel.setStyle(Theme.NORMAL_STYLE);
        Button fileBtn = new Button("Parcourir…");
        fileBtn.setStyle(Theme.BTN_SECONDARY);
        fileBtn.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Sélectionner une pièce justificative");
            chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Documents", "*.pdf", "*.jpg", "*.jpeg", "*.png", "*.doc", "*.docx")
            );
            File f = chooser.showOpenDialog(stage);
            if (f != null) { selectedFile[0] = f; fileLabel.setText(f.getName()); }
        });
        HBox fileRow = new HBox(10, fileBtn, fileLabel);
        fileRow.setAlignment(Pos.CENTER_LEFT);

        Label errLabel = new Label();
        errLabel.setStyle("-fx-text-fill: #C62828; -fx-font-size: 12; -fx-font-family: 'Segoe UI';");
        errLabel.setVisible(false);
        errLabel.setManaged(false);

        Button submitBtn = new Button("Soumettre la demande");
        submitBtn.setStyle(Theme.BTN_PRIMARY);
        submitBtn.setOnAction(e -> {
            String type = typeBox.getValue();
            String desc = descArea.getText().trim();
            if (type == null || desc.isEmpty()) {
                errLabel.setText("Veuillez remplir le type et la description.");
                errLabel.setVisible(true); errLabel.setManaged(true);
                return;
            }
            errLabel.setVisible(false); errLabel.setManaged(false);

            Demande d = new Demande();
            d.setType(type); d.setDescription(desc);
            int newId = controller.soumettreDemande(d);

            if (newId > 0 && selectedFile[0] != null) {
                PieceJustificative piece = new PieceJustificative();
                piece.setNomFichier(selectedFile[0].getName());
                piece.setCheminFichier(selectedFile[0].getAbsolutePath());
                piece.setDescPiece("Pièce justificative");
                piece.setIdDemande(newId);
                controller.ajouterPiece(piece);
            }

            typeBox.setValue(null); descArea.clear();
            fileLabel.setText("Aucun fichier sélectionné"); selectedFile[0] = null;
            loadData();

            alert(stage, "Demande soumise avec succès.");
        });

        grid.add(fieldLabel("Type de demande"),    0, 0); grid.add(typeBox,  1, 0);
        grid.add(fieldLabel("Description"),         0, 1); grid.add(descArea, 1, 1);
        grid.add(fieldLabel("Pièce justificative"), 0, 2); grid.add(fileRow,  1, 2);

        section.getChildren().addAll(sectionTitle, grid, errLabel, submitBtn);
        return section;
    }

    // ── Table ─────────────────────────────────────────────────────────────

    private VBox buildTable(Stage stage) {
        VBox section = new VBox(10);
        VBox.setVgrow(section, Priority.ALWAYS);

        Label sectionTitle = new Label(isAdmin ? "Toutes les demandes" : "Mes demandes");
        sectionTitle.setStyle(Theme.SECTION_TITLE_STYLE);

        TableView<Demande> table = new TableView<>(items);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);
        table.setPlaceholder(new Label("Aucune demande."));

        TableColumn<Demande, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getIdDemande()));
        idCol.setMaxWidth(55); idCol.setMinWidth(55);

        TableColumn<Demande, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getType()));
        typeCol.setMinWidth(160);

        TableColumn<Demande, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDescription()));

        TableColumn<Demande, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(c -> {
            var d = c.getValue().getDateDemande();
            return new SimpleStringProperty(d != null ? d.format(DATE_FMT) : "—");
        });
        dateCol.setMinWidth(125);

        TableColumn<Demande, String> statutCol = new TableColumn<>("Statut");
        statutCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStatut()));
        statutCol.setMinWidth(105);
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

        TableColumn<Demande, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setMinWidth(isAdmin ? 380 : 160);
        actionsCol.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Void v, boolean empty) {
                super.updateItem(v, empty);
                if (empty || getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null); return;
                }
                Demande d = getTableView().getItems().get(getIndex());
                setGraphic(buildActionBox(d, stage));
            }
        });

        table.getColumns().addAll(idCol, typeCol, descCol, dateCol, statutCol, actionsCol);
        section.getChildren().addAll(sectionTitle, table);
        return section;
    }

    private HBox buildActionBox(Demande d, Stage stage) {
        HBox box = new HBox(5);
        box.setAlignment(Pos.CENTER_LEFT);

        if (isAdmin) {
            // Statut dropdown + Appliquer
            ComboBox<String> statutBox = new ComboBox<>();
            statutBox.getItems().addAll(ADMIN_STATUTS);
            statutBox.setValue(d.getStatut());
            statutBox.setPrefWidth(115);

            Button appliquerBtn = new Button("Appliquer");
            appliquerBtn.setStyle(Theme.BTN_PRIMARY);
            appliquerBtn.setOnAction(e -> {
                String newStatut = statutBox.getValue();
                if (newStatut != null) {
                    controller.mettreAJourStatut(d.getIdDemande(), newStatut);
                    loadData();
                }
            });

            Separator sep = new Separator();
            sep.setOrientation(javafx.geometry.Orientation.VERTICAL);
            sep.setPadding(new Insets(0, 2, 0, 2));

            Button modifierBtn  = new Button("Modifier");   modifierBtn.setStyle(Theme.BTN_SECONDARY);
            Button supprimerBtn = new Button("Supprimer");  supprimerBtn.setStyle(Theme.BTN_DANGER);

            modifierBtn.setOnAction(e  -> openEditDialog(d, stage));
            supprimerBtn.setOnAction(e -> confirmSupprimer(d.getIdDemande(), stage));

            box.getChildren().addAll(statutBox, appliquerBtn, sep, modifierBtn, supprimerBtn);
        } else {
            Button modifierBtn  = new Button("Modifier");   modifierBtn.setStyle(Theme.BTN_SECONDARY);
            Button supprimerBtn = new Button("Supprimer");  supprimerBtn.setStyle(Theme.BTN_DANGER);

            modifierBtn.setOnAction(e  -> openEditDialog(d, stage));
            supprimerBtn.setOnAction(e -> confirmSupprimer(d.getIdDemande(), stage));

            box.getChildren().addAll(modifierBtn, supprimerBtn);
        }
        return box;
    }

    // ── Edit dialog ───────────────────────────────────────────────────────

    private void openEditDialog(Demande d, Stage stage) {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Modifier la demande #" + d.getIdDemande());
        dlg.initOwner(stage);
        dlg.getDialogPane().setPrefWidth(440);

        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll(TYPES);
        typeBox.setValue(d.getType());
        typeBox.setMaxWidth(Double.MAX_VALUE);

        TextArea descArea = new TextArea(d.getDescription());
        descArea.setPrefRowCount(4);
        descArea.setWrapText(true);

        Label errLbl = new Label("Type et description requis.");
        errLbl.setStyle("-fx-text-fill: #C62828; -fx-font-size: 12;");
        errLbl.setVisible(false); errLbl.setManaged(false);

        GridPane grid = new GridPane();
        grid.setHgap(12); grid.setVgap(10);
        grid.setPadding(new Insets(16, 20, 4, 20));
        ColumnConstraints lc = new ColumnConstraints(130);
        ColumnConstraints fc = new ColumnConstraints();
        fc.setHgrow(Priority.ALWAYS); fc.setFillWidth(true);
        grid.getColumnConstraints().addAll(lc, fc);
        grid.add(fieldLabel("Type :"),        0, 0); grid.add(typeBox,  1, 0);
        grid.add(fieldLabel("Description :"), 0, 1); grid.add(descArea, 1, 1);
        grid.add(errLbl, 1, 2);

        dlg.getDialogPane().setContent(grid);

        ButtonType saveType   = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelType = new ButtonType("Annuler",     ButtonBar.ButtonData.CANCEL_CLOSE);
        dlg.getDialogPane().getButtonTypes().addAll(saveType, cancelType);

        Node saveNode = dlg.getDialogPane().lookupButton(saveType);
        saveNode.addEventFilter(ActionEvent.ACTION, ae -> {
            if (typeBox.getValue() == null || descArea.getText().isBlank()) {
                errLbl.setVisible(true); errLbl.setManaged(true);
                ae.consume();
            }
        });

        dlg.setResultConverter(bt -> bt);
        Optional<ButtonType> result = dlg.showAndWait();
        result.ifPresent(bt -> {
            if (bt == saveType) {
                controller.modifierDemande(d.getIdDemande(), typeBox.getValue(), descArea.getText().trim());
                loadData();
            }
        });
    }

    private void confirmSupprimer(int idDemande, Stage stage) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
            "Supprimer cette demande ?", ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);
        confirm.initOwner(stage);
        confirm.showAndWait()
            .filter(r -> r == ButtonType.YES)
            .ifPresent(r -> { controller.supprimerDemande(idDemande); loadData(); });
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private void loadData() {
        items.setAll(isAdmin ? controller.getAllDemandes() : controller.getMesDemandes());
    }

    private static String statutColor(String statut) {
        return switch (statut) {
            case "EN_COURS"  -> "#1976D2";
            case "ACCEPTEE"  -> "#388E3C";
            case "REFUSEE"   -> "#C62828";
            case "ARCHIVEE"  -> "#607D8B";
            default          -> "#9E9E9E";
        };
    }

    private static void alert(Stage owner, String message) {
        Alert ok = new Alert(Alert.AlertType.INFORMATION);
        ok.setTitle("Succès"); ok.setHeaderText(null); ok.setContentText(message);
        ok.initOwner(owner); ok.showAndWait();
    }

    private static Label fieldLabel(String text) {
        Label l = new Label(text); l.setStyle(Theme.LABEL_STYLE); return l;
    }
}
