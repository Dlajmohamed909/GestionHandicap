package com.gestionhandicap.view;

import com.gestionhandicap.controller.DashboardController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.Map;

public class DashboardView {

    private static final String[] PIE_COLORS    = {"#1976D2", "#388E3C", "#C62828", "#E65100", "#7B1FA2", "#0097A7"};
    private static final String[] BAR_COLORS    = {"#1976D2", "#E65100"};
    private static final String[] MONTHS        = {"Jan","Fév","Mar","Avr","Mai","Juin","Juil","Août","Sep","Oct","Nov","Déc"};

    private final DashboardController controller = new DashboardController();

    private DatePicker  dateDebutPicker;
    private DatePicker  dateFinPicker;
    private ComboBox<String> typeCombo;
    private PieChart    pieChart;

    public Node buildView(Stage stage) {
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setStyle("-fx-background-color: white; -fx-background: white;");

        VBox root = new VBox(28);
        root.setPadding(new Insets(36, 48, 48, 48));
        root.setStyle("-fx-background-color: white;");

        Label title = new Label("Tableau de bord");
        title.setStyle(Theme.TITLE_STYLE);

        root.getChildren().addAll(
            title,
            buildStatCards(),
            divider(),
            buildFilterSection(),
            buildPieSection(),
            divider(),
            buildAnnualSection()
        );

        scroll.setContent(root);
        return scroll;
    }

    // ── Stat cards ────────────────────────────────────────────────────────

    private HBox buildStatCards() {
        Map<String, Long> byStatut = controller.getDemandesParStatut();
        long enAttente = byStatut.getOrDefault("EN_ATTENTE", 0L) + byStatut.getOrDefault("EN_COURS", 0L);
        long validees  = byStatut.getOrDefault("VALIDEE",    0L) + byStatut.getOrDefault("ACCEPTEE", 0L);
        long rejetees  = byStatut.getOrDefault("REJETEE",    0L) + byStatut.getOrDefault("REFUSEE",  0L);

        HBox row = new HBox(16);
        VBox[] cards = {
            statCard("Demandes en attente",  (int) enAttente,                    Theme.BLUE),
            statCard("Demandes validées",    (int) validees,                     "#388E3C"),
            statCard("Demandes rejetées",    (int) rejetees,                     "#C62828"),
            statCard("Total demandes",       controller.getNombreTotalDemandes(), "#7B1FA2"),
            statCard("Total réclamations",   controller.getNombreTotalReclamations(), "#E65100")
        };
        for (VBox c : cards) {
            HBox.setHgrow(c, Priority.ALWAYS);
            row.getChildren().add(c);
        }
        return row;
    }

    private VBox statCard(String label, int count, String color) {
        Label numLabel = new Label(String.valueOf(count));
        numLabel.setStyle(
            "-fx-font-family: 'Segoe UI'; -fx-font-size: 34; -fx-font-weight: bold; -fx-text-fill: " + color + ";"
        );

        Label textLabel = new Label(label);
        textLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12; -fx-text-fill: #5A6578;");
        textLabel.setWrapText(true);

        VBox content = new VBox(4, numLabel, textLabel);
        content.setPadding(new Insets(16, 20, 16, 20));

        VBox card = new VBox(content);
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 8;" +
            "-fx-border-color: transparent transparent transparent " + color + ";" +
            "-fx-border-width: 0 0 0 5;" +
            "-fx-border-radius: 0;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.09), 8, 0, 0, 2);"
        );
        return card;
    }

    // ── Filter section ────────────────────────────────────────────────────

    private VBox buildFilterSection() {
        Label sectionTitle = new Label("Filtrer les statistiques");
        sectionTitle.setStyle(Theme.SECTION_TITLE_STYLE);

        dateDebutPicker = new DatePicker();
        dateDebutPicker.setPromptText("Date début");
        dateDebutPicker.setPrefWidth(158);

        dateFinPicker = new DatePicker();
        dateFinPicker.setPromptText("Date fin");
        dateFinPicker.setPrefWidth(158);

        typeCombo = new ComboBox<>();
        typeCombo.setPromptText("Tous les types");
        typeCombo.getItems().add("");
        typeCombo.getItems().addAll(controller.getTypesDemandesDistincts());
        typeCombo.setPrefWidth(210);

        Button applyBtn = new Button("Appliquer");
        applyBtn.setStyle(Theme.BTN_PRIMARY);
        applyBtn.setOnAction(e -> applyFilters());

        Button resetBtn = new Button("Réinitialiser");
        resetBtn.setStyle(Theme.BTN_SECONDARY);
        resetBtn.setOnAction(e -> {
            dateDebutPicker.setValue(null);
            dateFinPicker.setValue(null);
            typeCombo.setValue(null);
            applyFilters();
        });

        HBox filterRow = new HBox(16,
            labeled("Date début",       dateDebutPicker),
            labeled("Date fin",         dateFinPicker),
            labeled("Type de demande",  typeCombo),
            buttonVBox(applyBtn, resetBtn)
        );
        filterRow.setAlignment(Pos.BOTTOM_LEFT);

        return new VBox(10, sectionTitle, filterRow);
    }

    private VBox labeled(String text, Control ctrl) {
        Label l = new Label(text);
        l.setStyle(Theme.LABEL_STYLE);
        return new VBox(4, l, ctrl);
    }

    private VBox buttonVBox(Button primary, Button secondary) {
        Label spacer = new Label(" ");
        spacer.setStyle(Theme.LABEL_STYLE);
        HBox btns = new HBox(8, primary, secondary);
        btns.setAlignment(Pos.CENTER_LEFT);
        return new VBox(4, spacer, btns);
    }

    private void applyFilters() {
        LocalDateTime debut = dateDebutPicker.getValue() != null
                ? dateDebutPicker.getValue().atStartOfDay() : null;
        LocalDateTime fin = dateFinPicker.getValue() != null
                ? dateFinPicker.getValue().atTime(23, 59, 59) : null;
        String type = typeCombo.getValue();
        refreshPieChart(debut, fin, type);
    }

    // ── Pie chart ─────────────────────────────────────────────────────────

    private VBox buildPieSection() {
        Label sectionTitle = new Label("Répartition des demandes par statut");
        sectionTitle.setStyle(Theme.SECTION_TITLE_STYLE);

        pieChart = new PieChart();
        pieChart.setLegendVisible(true);
        pieChart.setLabelsVisible(true);
        pieChart.setAnimated(false);
        pieChart.setPrefHeight(310);
        pieChart.setMaxWidth(520);
        pieChart.setStyle("-fx-background-color: white;");

        refreshPieChart(null, null, null);

        VBox section = new VBox(10, sectionTitle, pieChart);
        section.setAlignment(Pos.TOP_LEFT);
        return section;
    }

    private void refreshPieChart(LocalDateTime debut, LocalDateTime fin, String type) {
        pieChart.getData().clear();
        Map<String, Long> data = controller.getDemandesParStatutFiltre(debut, fin, type);
        if (data.isEmpty()) {
            pieChart.getData().add(new PieChart.Data("Aucune donnée", 1));
            return;
        }
        int[] idx = {0};
        data.forEach((statut, count) -> {
            PieChart.Data slice = new PieChart.Data(statut + " (" + count + ")", count);
            pieChart.getData().add(slice);
            final String color = PIE_COLORS[idx[0] % PIE_COLORS.length];
            slice.nodeProperty().addListener((obs, old, node) -> {
                if (node != null) node.setStyle("-fx-pie-color: " + color + ";");
            });
            idx[0]++;
        });
    }

    // ── Annual bar chart ──────────────────────────────────────────────────

    private VBox buildAnnualSection() {
        int annee = Year.now().getValue();

        Label sectionTitle = new Label("Statistiques annuelles – " + annee);
        sectionTitle.setStyle(Theme.SECTION_TITLE_STYLE);

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis   yAxis = new NumberAxis();
        xAxis.setLabel("Mois");
        yAxis.setLabel("Nombre");
        yAxis.setMinorTickVisible(false);

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setAnimated(false);
        chart.setLegendVisible(true);
        chart.setPrefHeight(310);
        chart.setCategoryGap(14);
        chart.setBarGap(3);
        chart.setStyle("-fx-background-color: white;");

        Map<Integer, Long> demandesM    = controller.getDemandesParMois(annee);
        Map<Integer, Long> reclamationsM = controller.getReclamationsParMois(annee);

        XYChart.Series<String, Number> serDemandes     = new XYChart.Series<>();
        XYChart.Series<String, Number> serReclamations = new XYChart.Series<>();
        serDemandes.setName("Demandes");
        serReclamations.setName("Réclamations");

        for (int m = 1; m <= 12; m++) {
            serDemandes.getData().add(    new XYChart.Data<>(MONTHS[m - 1], demandesM.getOrDefault(m, 0L)));
            serReclamations.getData().add(new XYChart.Data<>(MONTHS[m - 1], reclamationsM.getOrDefault(m, 0L)));
        }

        chart.getData().addAll(serDemandes, serReclamations);

        chart.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                colorBars(chart);
            }
        });

        return new VBox(10, sectionTitle, chart);
    }

    private void colorBars(BarChart<String, Number> chart) {
        for (int s = 0; s < chart.getData().size(); s++) {
            final String color = BAR_COLORS[s % BAR_COLORS.length];
            for (XYChart.Data<String, Number> d : chart.getData().get(s).getData()) {
                if (d.getNode() != null) {
                    d.getNode().setStyle("-fx-bar-fill: " + color + ";");
                }
            }
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private Node divider() {
        Separator sep = new Separator();
        VBox.setMargin(sep, new Insets(4, 0, 4, 0));
        return sep;
    }
}
