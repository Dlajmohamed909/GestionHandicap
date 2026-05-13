package com.gestionhandicap.view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class DashboardView {

    public Parent getView() {
        VBox page = new VBox(25);
        page.getStyleClass().add("page");
        page.setPadding(new Insets(45, 55, 45, 55));

        Label title = new Label("Tableau de bord");
        title.getStyleClass().add("page-title");

        Label subtitle = new Label("Vue générale sur les demandes et les réclamations.");
        subtitle.getStyleClass().add("page-subtitle");

        GridPane cards = new GridPane();
        cards.setHgap(25);
        cards.setVgap(25);

        cards.add(card("Demandes en cours", "12"), 0, 0);
        cards.add(card("Demandes acceptées", "08"), 1, 0);
        cards.add(card("Demandes refusées", "03"), 0, 1);
        cards.add(card("Réclamations", "05"), 1, 1);

        page.getChildren().addAll(title, subtitle, cards);
        return page;
    }

    private VBox card(String label, String value) {
        VBox card = new VBox(8);
        card.getStyleClass().add("card");
        card.setPrefSize(250, 135);

        Label number = new Label(value);
        number.getStyleClass().add("card-number");

        Label text = new Label(label);
        text.getStyleClass().add("card-text");

        card.getChildren().addAll(number, text);
        return card;
    }
}
