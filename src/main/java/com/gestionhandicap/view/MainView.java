package com.gestionhandicap.view;

import com.gestionhandicap.controller.AuthController;
import com.gestionhandicap.util.Session;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainView {

    private final StackPane contentArea = new StackPane();

    private static final String NAV_STYLE =
        "-fx-background-color: #1976D2; -fx-text-fill: white; -fx-font-family: 'Segoe UI';" +
        " -fx-font-size: 13; -fx-font-weight: bold; -fx-padding: 11 14 11 14;" +
        " -fx-background-radius: 6; -fx-cursor: hand;";
    private static final String NAV_HOVER =
        "-fx-background-color: #0D47A1; -fx-text-fill: white; -fx-font-family: 'Segoe UI';" +
        " -fx-font-size: 13; -fx-font-weight: bold; -fx-padding: 11 14 11 14;" +
        " -fx-background-radius: 6; -fx-cursor: hand;";

    public Scene createScene(Stage stage) {
        BorderPane root = new BorderPane();
        root.setLeft(buildSidebar(stage));
        contentArea.setStyle("-fx-background-color: white;");
        BorderPane.setAlignment(contentArea, javafx.geometry.Pos.TOP_LEFT);
        root.setCenter(contentArea);
        Node landing = Session.isAdmin()
                ? new DashboardView().buildView(stage)
                : new EtudiantDashboardView().buildView(stage,
                        () -> showView(new DemandeView().buildView(stage)),
                        () -> showView(new ReclamationView().buildView(stage)));
        showView(landing);
        return new Scene(root);
    }

    private VBox buildSidebar(Stage stage) {
        VBox sidebar = new VBox(8);
        sidebar.setPrefWidth(240);
        sidebar.setMinWidth(240);
        sidebar.setStyle("-fx-background-color: #1565C0;");
        sidebar.setPadding(new Insets(30, 16, 24, 16));

        Label logo = new Label("♿  GestionHandicap");
        logo.setStyle("-fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-size: 20; -fx-font-weight: bold;");
        VBox.setMargin(logo, new Insets(0, 0, 20, 0));

        Button demandesBtn = navButton("Demandes");
        Button reclamBtn   = navButton("Réclamations");
        Button logoutBtn   = navButton("Déconnexion");

        demandesBtn.setOnAction(e -> showView(new DemandeView().buildView(stage)));
        reclamBtn.setOnAction(e   -> showView(new ReclamationView().buildView(stage)));
        logoutBtn.setOnAction(e -> {
            new AuthController().logout();
            if (stage.isMaximized()) stage.setMaximized(false);
            stage.setScene(new LoginView().createScene(stage));
            stage.setWidth(860);
            stage.setHeight(560);
            stage.setMinWidth(700);
            stage.setMinHeight(460);
            stage.centerOnScreen();
        });

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button profilBtn = navButton("Mon profil");
        profilBtn.setOnAction(e -> showView(new MonProfilView().buildView(stage)));

        sidebar.getChildren().addAll(logo);

        if (Session.isAdmin()) {
            Button dashBtn        = navButton("Tableau de bord");
            Button archivBtn      = navButton("Archivage");
            Button comptesBtn     = navButton("Gestion des comptes");
            dashBtn.setOnAction(e    -> showView(new DashboardView().buildView(stage)));
            archivBtn.setOnAction(e  -> showView(new ArchivageView().buildView(stage)));
            comptesBtn.setOnAction(e -> showView(new GestionComptesView().buildView(stage)));
            sidebar.getChildren().addAll(dashBtn, demandesBtn, reclamBtn, archivBtn, comptesBtn);
        } else {
            Button etudiantDashBtn = navButton("Tableau de bord");
            etudiantDashBtn.setOnAction(e -> showView(new EtudiantDashboardView().buildView(stage,
                () -> showView(new DemandeView().buildView(stage)),
                () -> showView(new ReclamationView().buildView(stage))
            )));
            sidebar.getChildren().addAll(etudiantDashBtn, demandesBtn, reclamBtn);
        }

        sidebar.getChildren().addAll(spacer, profilBtn, logoutBtn);
        return sidebar;
    }

    private Button navButton(String label) {
        Button btn = new Button(label);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setStyle(NAV_STYLE);
        btn.setOnMouseEntered(e -> btn.setStyle(NAV_HOVER));
        btn.setOnMouseExited(e  -> btn.setStyle(NAV_STYLE));
        return btn;
    }

    private void showView(Node view) {
        if (view instanceof Region r) {
            r.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        }
        contentArea.getChildren().setAll(view);
        StackPane.setAlignment(view, Pos.TOP_LEFT);
    }
}
