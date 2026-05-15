package com.gestionhandicap.view;

import com.gestionhandicap.controller.ArchivageController;
import com.gestionhandicap.model.ArchiveRecord;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ArchivageView {

    private static final DateTimeFormatter DATE_FMT  = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final String[] SOURCE_OPTIONS     = {"Tous", "Demande", "Réclamation"};
    private static final String[] STATUT_OPTIONS     = {"Tous", "ACCEPTEE", "REFUSEE", "TRAITEE", "REJETEE", "ARCHIVEE"};

    private final ArchivageController controller = new ArchivageController();
    private final ObservableList<ArchiveRecord> displayedItems = FXCollections.observableArrayList();
    private List<ArchiveRecord> allRecords;
    private Map<Integer, String> userMap;

    private DatePicker       dateDebutPicker;
    private DatePicker       dateFinPicker;
    private ComboBox<String> sourceCombo;
    private ComboBox<String> statutCombo;
    private TextField        searchField;
    private TableView<ArchiveRecord> table;

    public Node buildView(Stage stage) {
        VBox root = new VBox(0);
        root.setStyle("-fx-background-color: white;");
        VBox.setVgrow(root, Priority.ALWAYS);

        VBox header = buildHeader();
        VBox filterSection = buildFilterSection(stage);
        HBox actionsBar = buildActionsBar(stage);
        VBox tableSection = buildTableSection();
        VBox.setVgrow(tableSection, Priority.ALWAYS);

        root.getChildren().addAll(header, filterSection, actionsBar, tableSection);

        loadAll();
        return root;
    }

    // ── Header ────────────────────────────────────────────────────────────

    private VBox buildHeader() {
        Label title = new Label("Archivage — Historique complet");
        title.setStyle(Theme.TITLE_STYLE);
        Label subtitle = new Label("Consulter, filtrer et exporter les demandes et réclamations terminées");
        subtitle.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 13; -fx-text-fill: #5A6578;");

        VBox box = new VBox(4, title, subtitle);
        box.setPadding(new Insets(30, 48, 20, 48));
        return box;
    }

    // ── Filter section ────────────────────────────────────────────────────

    private VBox buildFilterSection(Stage stage) {
        Label sectionTitle = new Label("Filtres et recherche");
        sectionTitle.setStyle(Theme.SECTION_TITLE_STYLE);

        // Row 1: date range + type + statut
        dateDebutPicker = new DatePicker();
        dateDebutPicker.setPromptText("Date début");
        dateDebutPicker.setPrefWidth(148);

        dateFinPicker = new DatePicker();
        dateFinPicker.setPromptText("Date fin");
        dateFinPicker.setPrefWidth(148);

        sourceCombo = new ComboBox<>();
        sourceCombo.getItems().addAll(SOURCE_OPTIONS);
        sourceCombo.setValue("Tous");
        sourceCombo.setPrefWidth(155);

        statutCombo = new ComboBox<>();
        statutCombo.getItems().addAll(STATUT_OPTIONS);
        statutCombo.setValue("Tous");
        statutCombo.setPrefWidth(140);

        // Row 2: search + buttons
        searchField = new TextField();
        searchField.setPromptText("Recherche multicritère : description, type, statut, étudiant…");
        searchField.setPrefWidth(380);
        searchField.setOnAction(e -> applyFilters());

        Button applyBtn = new Button("Appliquer");
        applyBtn.setStyle(Theme.BTN_PRIMARY);
        applyBtn.setOnAction(e -> applyFilters());

        Button resetBtn = new Button("Réinitialiser");
        resetBtn.setStyle(Theme.BTN_SECONDARY);
        resetBtn.setOnAction(e -> {
            dateDebutPicker.setValue(null);
            dateFinPicker.setValue(null);
            sourceCombo.setValue("Tous");
            statutCombo.setValue("Tous");
            searchField.clear();
            applyFilters();
        });

        HBox row1 = new HBox(12,
            labeled("Date début",   dateDebutPicker),
            labeled("Date fin",     dateFinPicker),
            labeled("Type",         sourceCombo),
            labeled("Statut",       statutCombo)
        );
        row1.setAlignment(Pos.BOTTOM_LEFT);

        HBox row2 = new HBox(12,
            labeled("Recherche",    searchField),
            buttonGroup(applyBtn, resetBtn)
        );
        row2.setAlignment(Pos.BOTTOM_LEFT);

        VBox box = new VBox(12, sectionTitle, row1, row2);
        box.setPadding(new Insets(0, 48, 16, 48));
        box.setStyle(
            "-fx-background-color: " + Theme.LIGHT_GRAY + ";" +
            "-fx-border-color: transparent transparent #E0E6EF transparent;" +
            "-fx-border-width: 1;"
        );
        box.setPadding(new Insets(16, 48, 20, 48));
        return box;
    }

    // ── Actions bar ───────────────────────────────────────────────────────

    private HBox buildActionsBar(Stage stage) {
        Label countLabel = new Label();
        countLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12; -fx-text-fill: #5A6578;");
        displayedItems.addListener((javafx.collections.ListChangeListener<ArchiveRecord>) c ->
            countLabel.setText(displayedItems.size() + " enregistrement(s) affiché(s)"));

        Button printBtn = new Button("Imprimer");
        printBtn.setStyle(Theme.BTN_SECONDARY);
        printBtn.setOnAction(e -> imprimerResultats(stage));

        Button csvBtn = new Button("Télécharger CSV");
        csvBtn.setStyle(Theme.BTN_SUCCESS);
        csvBtn.setOnAction(e -> exporterCSV(stage));

        Button archiveBtn = new Button("Archiver automatiquement…");
        archiveBtn.setStyle(Theme.BTN_WARNING);
        archiveBtn.setOnAction(e -> archiverAnciens(stage));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox bar = new HBox(10, printBtn, csvBtn, archiveBtn, spacer, countLabel);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(12, 48, 12, 48));
        bar.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: transparent transparent #E0E6EF transparent;" +
            "-fx-border-width: 1;"
        );
        return bar;
    }

    // ── Table ─────────────────────────────────────────────────────────────

    private VBox buildTableSection() {
        table = new TableView<>(displayedItems);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);
        table.setPlaceholder(new Label("Aucun enregistrement correspondant aux critères."));

        TableColumn<ArchiveRecord, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getId()));
        idCol.setMaxWidth(55); idCol.setMinWidth(55);

        TableColumn<ArchiveRecord, String> sourceCol = new TableColumn<>("Source");
        sourceCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getSource()));
        sourceCol.setMinWidth(100);
        sourceCol.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String v, boolean empty) {
                super.updateItem(v, empty);
                if (empty || v == null) { setGraphic(null); return; }
                Label badge = new Label(v);
                String color = "Demande".equals(v) ? Theme.BLUE : "#E65100";
                badge.setStyle(
                    "-fx-background-color: " + color + "; -fx-text-fill: white;" +
                    "-fx-padding: 2 8 2 8; -fx-background-radius: 4;" +
                    "-fx-font-family: 'Segoe UI'; -fx-font-size: 11; -fx-font-weight: bold;"
                );
                setGraphic(badge); setText(null);
            }
        });

        TableColumn<ArchiveRecord, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getType()));
        typeCol.setMinWidth(145);

        TableColumn<ArchiveRecord, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDescription()));
        descCol.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String v, boolean empty) {
                super.updateItem(v, empty);
                if (empty || v == null) { setText(null); setTooltip(null); return; }
                String truncated = v.length() > 60 ? v.substring(0, 57) + "…" : v;
                setText(truncated);
                setTooltip(v.length() > 60 ? new Tooltip(v) : null);
            }
        });

        TableColumn<ArchiveRecord, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(c -> {
            var d = c.getValue().getDate();
            return new SimpleStringProperty(d != null ? d.format(DATE_FMT) : "—");
        });
        dateCol.setMinWidth(125);

        TableColumn<ArchiveRecord, String> statutCol = new TableColumn<>("Statut");
        statutCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStatut()));
        statutCol.setMinWidth(105);
        statutCol.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String statut, boolean empty) {
                super.updateItem(statut, empty);
                if (empty || statut == null) { setGraphic(null); return; }
                Label badge = new Label(statut);
                badge.setStyle(
                    "-fx-background-color: " + statutColor(statut) + "; -fx-text-fill: white;" +
                    "-fx-padding: 3 8 3 8; -fx-background-radius: 4;" +
                    "-fx-font-family: 'Segoe UI'; -fx-font-size: 11; -fx-font-weight: bold;"
                );
                setGraphic(badge); setText(null);
            }
        });

        TableColumn<ArchiveRecord, String> etudiantCol = new TableColumn<>("Étudiant");
        etudiantCol.setCellValueFactory(c ->
            new SimpleStringProperty(
                userMap != null ? userMap.getOrDefault(c.getValue().getIdPersonne(), "—") : "—"
            )
        );
        etudiantCol.setMinWidth(130);

        table.getColumns().addAll(idCol, sourceCol, typeCol, descCol, dateCol, statutCol, etudiantCol);

        VBox section = new VBox(table);
        VBox.setVgrow(section, Priority.ALWAYS);
        section.setPadding(new Insets(0, 48, 24, 48));
        return section;
    }

    // ── Data loading & filtering ──────────────────────────────────────────

    private void loadAll() {
        allRecords = controller.getHistoriqueComplet();
        userMap    = controller.getUtilisateursMap();
        applyFilters();
    }

    private void applyFilters() {
        if (allRecords == null) return;

        String  sourceSel  = sourceCombo.getValue();
        String  statutSel  = statutCombo.getValue();
        String  keyword    = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();
        var     debut      = dateDebutPicker.getValue();
        var     fin        = dateFinPicker.getValue();

        List<ArchiveRecord> result = allRecords.stream()
            .filter(r -> "Tous".equals(sourceSel)  || sourceSel.equals(r.getSource()))
            .filter(r -> "Tous".equals(statutSel)  || statutSel.equals(r.getStatut()))
            .filter(r -> debut == null || (r.getDate() != null
                && !r.getDate().toLocalDate().isBefore(debut)))
            .filter(r -> fin   == null || (r.getDate() != null
                && !r.getDate().toLocalDate().isAfter(fin)))
            .filter(r -> keyword.isEmpty() || matchesKeyword(r, keyword))
            .collect(Collectors.toList());

        displayedItems.setAll(result);
    }

    private boolean matchesKeyword(ArchiveRecord r, String kw) {
        String etudiant = userMap != null ? userMap.getOrDefault(r.getIdPersonne(), "") : "";
        return contains(r.getSource(),      kw)
            || contains(r.getType(),        kw)
            || contains(r.getDescription(), kw)
            || contains(r.getStatut(),      kw)
            || contains(etudiant,           kw);
    }

    private static boolean contains(String field, String kw) {
        return field != null && field.toLowerCase().contains(kw);
    }

    // ── Print ─────────────────────────────────────────────────────────────

    private void imprimerResultats(Stage stage) {
        if (displayedItems.isEmpty()) {
            showAlert(stage, Alert.AlertType.WARNING, "Aucun enregistrement à imprimer.");
            return;
        }
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job == null) {
            showAlert(stage, Alert.AlertType.ERROR, "Aucune imprimante disponible sur ce système.");
            return;
        }
        if (job.showPrintDialog(stage)) {
            boolean ok = job.printPage(table);
            if (ok) job.endJob();
        }
    }

    // ── CSV export ────────────────────────────────────────────────────────

    private void exporterCSV(Stage stage) {
        if (displayedItems.isEmpty()) {
            showAlert(stage, Alert.AlertType.WARNING, "Aucun enregistrement à exporter.");
            return;
        }
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Enregistrer le fichier CSV");
        chooser.setInitialFileName("historique_" + LocalDate.now() + ".csv");
        chooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Fichiers CSV (*.csv)", "*.csv")
        );
        File file = chooser.showSaveDialog(stage);
        if (file == null) return;

        try (PrintWriter pw = new PrintWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            pw.println("ID,Source,Type,Description,Date,Statut,Étudiant");
            for (ArchiveRecord r : displayedItems) {
                String etudiant = userMap != null ? userMap.getOrDefault(r.getIdPersonne(), "") : "";
                pw.printf("%d,%s,%s,%s,%s,%s,%s%n",
                    r.getId(),
                    csv(r.getSource()),
                    csv(r.getType()),
                    csv(r.getDescription()),
                    r.getDate() != null ? r.getDate().format(DATE_FMT) : "",
                    r.getStatut(),
                    csv(etudiant)
                );
            }
            showAlert(stage, Alert.AlertType.INFORMATION,
                displayedItems.size() + " enregistrement(s) exporté(s) vers :\n" + file.getAbsolutePath());
        } catch (IOException e) {
            showAlert(stage, Alert.AlertType.ERROR, "Erreur lors de l'export : " + e.getMessage());
        }
    }

    private static String csv(String v) {
        if (v == null || v.isEmpty()) return "";
        if (v.contains(",") || v.contains("\"") || v.contains("\n")) {
            return "\"" + v.replace("\"", "\"\"") + "\"";
        }
        return v;
    }

    // ── Auto-archive ──────────────────────────────────────────────────────

    private void archiverAnciens(Stage stage) {
        TextInputDialog dlg = new TextInputDialog("6");
        dlg.setTitle("Archivage automatique");
        dlg.setHeaderText("Archiver les enregistrements terminés anciens");
        dlg.setContentText("Seuil en mois (archiver les enregistrements terminés depuis plus de X mois) :");
        dlg.initOwner(stage);

        dlg.showAndWait().ifPresent(input -> {
            int mois;
            try {
                mois = Integer.parseInt(input.trim());
                if (mois < 1) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                showAlert(stage, Alert.AlertType.WARNING, "Veuillez saisir un nombre entier ≥ 1.");
                return;
            }
            int count = controller.archiverAnciensEnregistrements(mois);
            loadAll();
            showAlert(stage, Alert.AlertType.INFORMATION,
                count + " enregistrement(s) archivé(s) (seuil : " + mois + " mois).");
        });
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private static String statutColor(String statut) {
        return switch (statut) {
            case "ACCEPTEE"  -> "#388E3C";
            case "REFUSEE"   -> "#C62828";
            case "TRAITEE"   -> "#388E3C";
            case "REJETEE"   -> "#C62828";
            case "ARCHIVEE"  -> "#607D8B";
            default          -> "#9E9E9E";
        };
    }

    private static void showAlert(Stage owner, Alert.AlertType type, String message) {
        Alert a = new Alert(type);
        a.setHeaderText(null);
        a.setContentText(message);
        a.initOwner(owner);
        a.showAndWait();
    }

    private static VBox labeled(String text, Control ctrl) {
        Label l = new Label(text);
        l.setStyle(Theme.LABEL_STYLE);
        return new VBox(4, l, ctrl);
    }

    private static VBox buttonGroup(Button primary, Button secondary) {
        Label spacer = new Label(" ");
        spacer.setStyle(Theme.LABEL_STYLE);
        HBox btns = new HBox(8, primary, secondary);
        return new VBox(4, spacer, btns);
    }
}
