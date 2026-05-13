package com.gestionhandicap.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ArchivageView {

    public Parent getView() {
        VBox page = new VBox(25);
        page.getStyleClass().add("page");
        page.setPadding(new Insets(45, 55, 45, 55));

        Label title = new Label("Archivage");
        title.getStyleClass().add("page-title");

        Label subtitle = new Label("Historique complet avec recherche multicritère.");
        subtitle.getStyleClass().add("page-subtitle");

        HBox searchBox = new HBox(12);

        TextField search = new TextField();
        search.setPromptText("Rechercher...");
        search.setPrefWidth(350);

        ComboBox<String> filter = new ComboBox<>();
        filter.getItems().addAll("Tous", "Demande", "Réclamation");
        filter.setValue("Tous");

        Button btn = new Button("Rechercher");
        btn.getStyleClass().add("primary-button");

        searchBox.getChildren().addAll(search, filter, btn);

        TableView<ArchiveItem> table = new TableView<>();

        TableColumn<ArchiveItem, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().id()));

        TableColumn<ArchiveItem, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().type()));

        TableColumn<ArchiveItem, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().date()));

        TableColumn<ArchiveItem, String> statutCol = new TableColumn<>("Statut");
        statutCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().statut()));

        table.getColumns().addAll(idCol, typeCol, dateCol, statutCol);
        table.setItems(FXCollections.observableArrayList(
                new ArchiveItem("1", "Demande", "2026-05-13", "Acceptée"),
                new ArchiveItem("2", "Réclamation", "2026-05-12", "En cours"),
                new ArchiveItem("3", "Demande", "2026-05-10", "Refusée")
        ));

        page.getChildren().addAll(title, subtitle, searchBox, table);
        return page;
    }

    public record ArchiveItem(String id, String type, String date, String statut) {}
}
