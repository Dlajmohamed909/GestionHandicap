package com.gestionhandicap.view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class ReclamationView {

    public Parent getView() {
        VBox page = new VBox(25);
        page.getStyleClass().add("page");
        page.setPadding(new Insets(45, 55, 45, 55));

        Label title = new Label("Gestion des réclamations");
        title.getStyleClass().add("page-title");

        Label subtitle = new Label("Saisir une réclamation et suivre son traitement.");
        subtitle.getStyleClass().add("page-subtitle");

        GridPane form = new GridPane();
        form.getStyleClass().add("form-box");
        form.setHgap(18);
        form.setVgap(16);

        TextField sujet = new TextField();
        sujet.setPromptText("Sujet de la réclamation");

        TextArea message = new TextArea();
        message.setPromptText("Écrire la réclamation...");
        message.setPrefRowCount(6);

        ComboBox<String> statut = new ComboBox<>();
        statut.getItems().addAll("En cours", "Traitée", "Clôturée");
        statut.setPromptText("Statut");
        statut.setMaxWidth(Double.MAX_VALUE);

        Button send = new Button("Envoyer");
        send.getStyleClass().add("primary-button");

        form.add(label("Sujet"), 0, 0);
        form.add(sujet, 1, 0);
        form.add(label("Message"), 0, 1);
        form.add(message, 1, 1);
        form.add(label("Statut"), 0, 2);
        form.add(statut, 1, 2);
        form.add(send, 1, 3);

        page.getChildren().addAll(title, subtitle, form);
        return page;
    }

    private Label label(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("field-label");
        return label;
    }
}
