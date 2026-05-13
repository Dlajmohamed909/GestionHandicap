package com.gestionhandicap.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MainView {
    private final BorderPane root = new BorderPane();

    public MainView() {
        root.setLeft(createMenu());
        root.setCenter(new DashboardView().getView());
    }

    public Parent getRoot() {
        return root;
    }

    private VBox createMenu() {
        VBox menu = new VBox(14);
        menu.getStyleClass().add("sidebar");
        menu.setPrefWidth(270);
        menu.setPadding(new Insets(35, 22, 35, 22));
        menu.setAlignment(Pos.TOP_CENTER);

        Label icon = new Label("♿");
        icon.getStyleClass().add("sidebar-icon");

        Label title = new Label("GestionHandicap");
        title.getStyleClass().add("sidebar-title");

        Label desc = new Label("Gestion des demandes et réclamations\n"
                + "des étudiants en situation de handicap");
        desc.getStyleClass().add("sidebar-text");

        menu.getChildren().addAll(
                icon, title, desc,
                menuButton("Tableau de bord", new DashboardView().getView()),
                menuButton("Demandes", new DemandeView().getView()),
                menuButton("Réclamations", new ReclamationView().getView()),
                menuButton("Archivage", new ArchivageView().getView())
        );

        return menu;
    }

    private Button menuButton(String text, Parent page) {
        Button btn = new Button(text);
        btn.getStyleClass().add("menu-button");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setOnAction(e -> root.setCenter(page));
        return btn;
    }
}
