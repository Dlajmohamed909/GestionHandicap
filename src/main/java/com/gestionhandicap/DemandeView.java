package com.gestionhandicap.view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class DemandeView {

    public Parent getView() {
        VBox page = new VBox(25);
        page.getStyleClass().add("page");
        page.setPadding(new Insets(45, 55, 45, 55));

        Label title = new Label("Gestion des demandes");
        title.getStyleClass().add("page-title");

        Label subtitle = new Label("Ajouter et suivre les demandes d’aménagement.");
        subtitle.getStyleClass().add("page-subtitle");

        GridPane form = new GridPane();
        form.getStyleClass().add("form-box");
        form.setHgap(18);
        form.setVgap(16);

        ComboBox<String> type = new ComboBox<>();
        type.getItems().addAll("Aménagement d'examen", "Accessibilité", "Accompagnement");
        type.setPromptText("Choisir le type");
        type.setMaxWidth(Double.MAX_VALUE);

        TextArea description = new TextArea();
        description.setPromptText("Décrire la demande...");
        description.setPrefRowCount(4);

        TextField file = new TextField();
        file.setPromptText("Pièce justificative");
        file.setEditable(false);

        Button choose = new Button("Choisir fichier");
        choose.getStyleClass().add("secondary-button");

        ComboBox<String> statut = new ComboBox<>();
        statut.getItems().addAll("En cours", "Acceptée", "Refusée");
        statut.setPromptText("Statut");
        statut.setMaxWidth(Double.MAX_VALUE);

        Button save = new Button("Enregistrer");
        save.getStyleClass().add("primary-button");

        form.add(label("Type de demande"), 0, 0);
        form.add(type, 1, 0);
        form.add(label("Description"), 0, 1);
        form.add(description, 1, 1);
        form.add(label("Pièce justificative"), 0, 2);
        form.add(file, 1, 2);
        form.add(choose, 2, 2);
        form.add(label("Statut"), 0, 3);
        form.add(statut, 1, 3);
        form.add(save, 1, 4);

        page.getChildren().addAll(title, subtitle, form);
        return page;
    }

    private Label label(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("field-label");
        return label;
    }
}
