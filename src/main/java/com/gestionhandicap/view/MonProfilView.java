package com.gestionhandicap.view;

import com.gestionhandicap.controller.AuthController;
import com.gestionhandicap.model.Utilisateur;
import com.gestionhandicap.util.Session;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MonProfilView {

    private final AuthController auth = new AuthController();

    public Node buildView(Stage stage) {
        Utilisateur user = Session.getUtilisateur();

        VBox root = new VBox(28);
        root.setPadding(new Insets(36, 50, 36, 50));
        root.setStyle("-fx-background-color: white;");

        Label title = new Label("Mon profil");
        title.setStyle(Theme.TITLE_STYLE);

        Label subtitle = new Label("Modifiez vos informations personnelles et votre mot de passe.");
        subtitle.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 13; -fx-text-fill: #5A6578;");

        VBox card = buildProfileCard(user, stage);

        root.getChildren().addAll(title, subtitle, card);
        return root;
    }

    private VBox buildProfileCard(Utilisateur user, Stage stage) {
        VBox card = new VBox(16);
        card.setPadding(new Insets(28, 32, 28, 32));
        card.setMaxWidth(520);
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: #E0E6EF;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 10;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 8, 0, 0, 2);"
        );

        // Role badge
        Label roleBadge = new Label(user.getRole());
        roleBadge.setStyle(
            "-fx-background-color: " + ("ADMIN".equals(user.getRole()) ? "#1976D2" : "#388E3C") + ";" +
            "-fx-text-fill: white; -fx-padding: 3 10 3 10; -fx-background-radius: 4;" +
            "-fx-font-family: 'Segoe UI'; -fx-font-size: 11; -fx-font-weight: bold;"
        );

        GridPane grid = new GridPane();
        grid.setHgap(16); grid.setVgap(14);
        ColumnConstraints lc = new ColumnConstraints(115);
        ColumnConstraints fc = new ColumnConstraints();
        fc.setHgrow(Priority.ALWAYS); fc.setFillWidth(true);
        grid.getColumnConstraints().addAll(lc, fc);

        TextField nomField    = tf(user.getNom());
        TextField prenomField = tf(user.getPrenom());
        TextField emailField  = tf(user.getEmail());
        PasswordField passField = new PasswordField();
        passField.setPromptText("Nouveau mot de passe");
        passField.setMaxWidth(Double.MAX_VALUE);

        Label passHint = new Label("Laissez vide pour conserver le mot de passe actuel.");
        passHint.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 11; -fx-text-fill: #9E9E9E;");

        grid.add(fl("Nom"),           0, 0); grid.add(nomField,    1, 0);
        grid.add(fl("Prénom"),        0, 1); grid.add(prenomField, 1, 1);
        grid.add(fl("E-mail"),        0, 2); grid.add(emailField,  1, 2);
        grid.add(fl("Mot de passe"),  0, 3);
        VBox passBox = new VBox(4, passField, passHint);
        grid.add(passBox, 1, 3);

        Label errLabel = new Label();
        errLabel.setStyle("-fx-text-fill: #C62828; -fx-font-size: 12; -fx-font-family: 'Segoe UI';");
        errLabel.setVisible(false); errLabel.setManaged(false);

        Button saveBtn = new Button("Enregistrer les modifications");
        saveBtn.setStyle(Theme.BTN_PRIMARY);
        saveBtn.setOnAction(e -> {
            String nom    = nomField.getText().trim();
            String prenom = prenomField.getText().trim();
            String email  = emailField.getText().trim();
            String pass   = passField.getText();

            if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
                errLabel.setText("Nom, prénom et e-mail sont obligatoires.");
                errLabel.setVisible(true); errLabel.setManaged(true);
                return;
            }
            if (!email.contains("@") || !email.contains(".")) {
                errLabel.setText("Adresse e-mail invalide.");
                errLabel.setVisible(true); errLabel.setManaged(true);
                return;
            }
            // Keep old password if field is blank
            String motDePasse = pass.isEmpty() ? user.getMotDePasse() : pass;

            auth.modifierProfil(user.getId(), nom, prenom, email, motDePasse);
            errLabel.setVisible(false); errLabel.setManaged(false);

            Alert ok = new Alert(Alert.AlertType.INFORMATION);
            ok.setTitle("Profil mis à jour");
            ok.setHeaderText(null);
            ok.setContentText("Vos informations ont été enregistrées avec succès.");
            ok.initOwner(stage);
            ok.showAndWait();
        });

        card.getChildren().addAll(roleBadge, grid, errLabel, saveBtn);
        return card;
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private static TextField tf(String value) {
        TextField tf = new TextField(value != null ? value : "");
        tf.setMaxWidth(Double.MAX_VALUE);
        return tf;
    }

    private static Label fl(String text) {
        Label l = new Label(text + " :");
        l.setStyle(Theme.LABEL_STYLE);
        return l;
    }
}
