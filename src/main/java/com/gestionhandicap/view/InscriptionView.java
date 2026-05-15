package com.gestionhandicap.view;

import com.gestionhandicap.controller.AuthController;
import com.gestionhandicap.model.Administrateur;
import com.gestionhandicap.model.PersonneHandicap;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class InscriptionView {

    private final AuthController auth = new AuthController();

    public Scene createScene(Stage stage) {
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setStyle("-fx-background-color: white; -fx-background: white;");

        VBox root = new VBox(0);
        root.setStyle("-fx-background-color: white;");
        root.getChildren().addAll(buildHeader(), buildForm(stage));
        scroll.setContent(root);
        return new Scene(scroll, 500, 650);
    }

    // ── Header ────────────────────────────────────────────────────────────

    private VBox buildHeader() {
        Label icon = new Label("♿");
        icon.setStyle("-fx-font-size: 36; -fx-text-fill: white;");

        Label title = new Label("Créer un compte");
        title.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 22; -fx-font-weight: bold; -fx-text-fill: white;");

        Label sub = new Label("Rejoignez le système de gestion du handicap");
        sub.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 13; -fx-text-fill: #BBDEFB;");

        VBox box = new VBox(6, icon, title, sub);
        box.setPadding(new Insets(28, 40, 24, 40));
        box.setStyle("-fx-background-color: #1565C0;");
        return box;
    }

    // ── Form ──────────────────────────────────────────────────────────────

    private VBox buildForm(Stage stage) {
        VBox form = new VBox(14);
        form.setPadding(new Insets(28, 40, 28, 40));

        // Nom + Prénom side by side
        TextField nomField    = field("Nom");
        TextField prenomField = field("Prénom");
        HBox nameRow = new HBox(12, labeled("Nom", nomField), labeled("Prénom", prenomField));

        TextField     emailField = field("exemple@uir.ac.ma");
        PasswordField passField  = passField("••••••••");

        // Role selection
        Label roleLabel = new Label("Rôle");
        roleLabel.setStyle(Theme.LABEL_STYLE);

        ToggleGroup roleGroup   = new ToggleGroup();
        RadioButton etudiantRB  = new RadioButton("Étudiant (personne handicapée)");
        RadioButton adminRB     = new RadioButton("Administrateur");
        etudiantRB.setToggleGroup(roleGroup);
        adminRB.setToggleGroup(roleGroup);
        etudiantRB.setSelected(true);
        styleRadio(etudiantRB); styleRadio(adminRB);
        HBox roleRow = new HBox(20, etudiantRB, adminRB);
        roleRow.setAlignment(Pos.CENTER_LEFT);

        // Student-only section
        TextField     numEtField   = field("N° étudiant");
        ComboBox<String> handicapBox = new ComboBox<>();
        handicapBox.getItems().addAll("Moteur", "Visuel", "Auditif", "Mental", "Autre");
        handicapBox.setPromptText("Type de handicap");
        handicapBox.setMaxWidth(Double.MAX_VALUE);
        TextField filiereField = field("Filière / Formation");

        VBox etudiantSection = new VBox(10,
            labeled("N° étudiant",     numEtField),
            labeled("Type de handicap", handicapBox),
            labeled("Filière",          filiereField)
        );
        etudiantSection.setStyle(
            "-fx-background-color: #F0F3F7; -fx-background-radius: 8; -fx-padding: 14;"
        );

        etudiantRB.selectedProperty().addListener((obs, old, sel) -> {
            etudiantSection.setVisible(sel);
            etudiantSection.setManaged(sel);
        });

        // Error label
        Label errLabel = new Label();
        errLabel.setStyle("-fx-text-fill: #C62828; -fx-font-size: 12; -fx-font-family: 'Segoe UI';");
        errLabel.setWrapText(true);
        errLabel.setVisible(false);
        errLabel.setManaged(false);

        // Register button
        Button registerBtn = new Button("Créer le compte");
        registerBtn.setStyle(Theme.BTN_PRIMARY);
        registerBtn.setMaxWidth(Double.MAX_VALUE);
        registerBtn.setOnAction(e -> {
            boolean isEtudiant = etudiantRB.isSelected();
            String nom    = nomField.getText().trim();
            String prenom = prenomField.getText().trim();
            String email  = emailField.getText().trim();
            String pass   = passField.getText();

            if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                showErr(errLabel, "Veuillez remplir tous les champs obligatoires.");
                return;
            }
            if (!email.contains("@") || !email.contains(".")) {
                showErr(errLabel, "Adresse e-mail invalide.");
                return;
            }
            if (pass.length() < 4) {
                showErr(errLabel, "Le mot de passe doit contenir au moins 4 caractères.");
                return;
            }
            if (auth.getByEmail(email) != null) {
                showErr(errLabel, "Cette adresse e-mail est déjà utilisée.");
                return;
            }
            if (isEtudiant) {
                String numEt   = numEtField.getText().trim();
                String handicap = handicapBox.getValue();
                String filiere  = filiereField.getText().trim();
                if (numEt.isEmpty() || handicap == null || filiere.isEmpty()) {
                    showErr(errLabel, "Veuillez remplir tous les champs étudiant.");
                    return;
                }
                PersonneHandicap ph = new PersonneHandicap(0, email, pass, nom, prenom, numEt, handicap, filiere);
                auth.inscrirePersonneHandicap(ph);
            } else {
                Administrateur admin = new Administrateur(0, email, pass, nom, prenom, "", "");
                auth.inscrireAdmin(admin);
            }

            Alert ok = new Alert(Alert.AlertType.INFORMATION);
            ok.setTitle("Compte créé");
            ok.setHeaderText(null);
            ok.setContentText("Votre compte a été créé avec succès. Vous pouvez maintenant vous connecter.");
            ok.initOwner(stage);
            ok.showAndWait();
            stage.close();
        });

        // Back link
        Hyperlink backLink = new Hyperlink("Déjà un compte ? Se connecter");
        backLink.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12; -fx-text-fill: " + Theme.BLUE + ";");
        backLink.setOnAction(e -> stage.close());
        HBox backRow = new HBox(backLink);
        backRow.setAlignment(Pos.CENTER);

        form.getChildren().addAll(
            nameRow,
            labeled("Adresse e-mail", emailField),
            labeled("Mot de passe", passField),
            new VBox(4, roleLabel, roleRow),
            etudiantSection,
            errLabel,
            registerBtn,
            backRow
        );
        return form;
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private static TextField field(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setMaxWidth(Double.MAX_VALUE);
        return tf;
    }

    private static PasswordField passField(String prompt) {
        PasswordField pf = new PasswordField();
        pf.setPromptText(prompt);
        pf.setMaxWidth(Double.MAX_VALUE);
        return pf;
    }

    private static VBox labeled(String text, Control ctrl) {
        Label l = new Label(text);
        l.setStyle(Theme.LABEL_STYLE);
        VBox box = new VBox(4, l, ctrl);
        HBox.setHgrow(box, Priority.ALWAYS);
        return box;
    }

    private static void styleRadio(RadioButton rb) {
        rb.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 13; -fx-text-fill: #2D3746;");
    }

    private static void showErr(Label lbl, String msg) {
        lbl.setText(msg);
        lbl.setVisible(true);
        lbl.setManaged(true);
    }
}
