package com.gestionhandicap.view;

import com.gestionhandicap.controller.AuthController;
import com.gestionhandicap.model.Utilisateur;
import com.gestionhandicap.util.Session;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoginView {

    private final AuthController authController = new AuthController();

    public Scene createScene(Stage stage) {
        HBox root = new HBox();
        root.setPrefSize(860, 560);

        VBox leftPanel = buildLeftPanel();
        leftPanel.setPrefWidth(320);
        leftPanel.setMinWidth(320);
        leftPanel.setMaxWidth(320);

        VBox rightPanel = buildRightPanel(stage);
        HBox.setHgrow(rightPanel, Priority.ALWAYS);

        root.getChildren().addAll(leftPanel, rightPanel);

        Scene scene = new Scene(root);
        String css = getClass().getResource("/com/gestionhandicap/login.css").toExternalForm();
        scene.getStylesheets().add(css);
        return scene;
    }

    private VBox buildLeftPanel() {
        VBox panel = new VBox(20);
        panel.getStyleClass().add("left-panel");
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(60, 36, 60, 36));

        Label icon = new Label("♿");
        icon.getStyleClass().add("brand-icon");

        Label title = new Label("GestionHandicap");
        title.getStyleClass().add("brand-title");

        Region divider = new Region();
        divider.getStyleClass().add("brand-divider");

        Label subtitle = new Label("Gestion des demandes et réclamations\ndes étudiants en situation de handicap");
        subtitle.getStyleClass().add("brand-subtitle");
        subtitle.setTextAlignment(TextAlignment.CENTER);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Label footer = new Label("Université Internationale de Rabat — Service Handicap");
        footer.getStyleClass().add("brand-footer");

        panel.getChildren().addAll(icon, title, divider, subtitle, spacer, footer);
        return panel;
    }

    private VBox buildRightPanel(Stage stage) {
        VBox panel = new VBox();
        panel.getStyleClass().add("right-panel");
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(60, 72, 60, 72));

        VBox form = buildForm(stage);
        form.setMaxWidth(380);
        VBox.setVgrow(form, Priority.NEVER);

        panel.getChildren().add(form);
        return panel;
    }

    private VBox buildForm(Stage stage) {
        VBox form = new VBox();
        form.setSpacing(0);

        Label formTitle = new Label("Connexion");
        formTitle.getStyleClass().add("form-title");

        Label formHint = new Label("Entrez vos identifiants pour accéder à votre espace.");
        formHint.getStyleClass().add("form-hint");
        formHint.setWrapText(true);
        VBox.setMargin(formHint, new Insets(6, 0, 28, 0));

        Label emailLabel = new Label("ADRESSE E-MAIL");
        emailLabel.getStyleClass().add("field-label");
        VBox.setMargin(emailLabel, new Insets(0, 0, 6, 0));

        TextField emailField = new TextField();
        emailField.setPromptText("exemple@uir.ac.ma");
        emailField.getStyleClass().add("custom-field");
        emailField.setMaxWidth(Double.MAX_VALUE);
        VBox.setMargin(emailField, new Insets(0, 0, 18, 0));

        Label passwordLabel = new Label("MOT DE PASSE");
        passwordLabel.getStyleClass().add("field-label");
        VBox.setMargin(passwordLabel, new Insets(0, 0, 6, 0));

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("••••••••");
        passwordField.getStyleClass().add("custom-field");
        passwordField.setMaxWidth(Double.MAX_VALUE);
        VBox.setMargin(passwordField, new Insets(0, 0, 4, 0));

        Hyperlink forgotLink = new Hyperlink("Mot de passe oublié ?");
        forgotLink.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12; -fx-text-fill: #1976D2; -fx-padding: 0;");
        forgotLink.setOnAction(e -> openForgotPassword(stage));
        HBox forgotRow = new HBox(forgotLink);
        forgotRow.setAlignment(Pos.CENTER_RIGHT);
        VBox.setMargin(forgotRow, new Insets(0, 0, 10, 0));

        VBox errorBox = new VBox();
        errorBox.getStyleClass().add("error-box");
        Label errorText = new Label();
        errorText.getStyleClass().add("error-text");
        errorText.setMaxWidth(Double.MAX_VALUE);
        errorText.setWrapText(true);
        errorBox.getChildren().add(errorText);
        errorBox.setVisible(false);
        errorBox.setManaged(false);
        VBox.setMargin(errorBox, new Insets(0, 0, 14, 0));

        Button loginBtn = new Button("Se connecter");
        loginBtn.getStyleClass().add("login-btn");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        VBox.setMargin(loginBtn, new Insets(14, 0, 0, 0));

        loginBtn.setOnAction(e -> handleLogin(
                emailField.getText(),
                passwordField.getText(),
                errorBox, errorText,
                stage
        ));
        emailField.setOnAction(e -> passwordField.requestFocus());
        passwordField.setOnAction(e -> loginBtn.fire());

        Hyperlink registerLink = new Hyperlink("Pas encore de compte ? S'inscrire");
        registerLink.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12; -fx-text-fill: #1976D2;");
        registerLink.setOnAction(e -> openInscription(stage));
        HBox registerRow = new HBox(registerLink);
        registerRow.setAlignment(Pos.CENTER);
        VBox.setMargin(registerRow, new Insets(12, 0, 0, 0));

        form.getChildren().addAll(
                formTitle, formHint,
                emailLabel, emailField,
                passwordLabel, passwordField,
                forgotRow,
                errorBox,
                loginBtn,
                registerRow
        );
        return form;
    }

    private void openForgotPassword(Stage owner) {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Récupération du mot de passe");
        dlg.initOwner(owner);
        dlg.getDialogPane().setPrefWidth(380);

        Label hint = new Label("Saisissez votre adresse e-mail pour retrouver votre mot de passe.");
        hint.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12; -fx-text-fill: #5A6578;");
        hint.setWrapText(true);

        TextField emailField = new TextField();
        emailField.setPromptText("exemple@uir.ac.ma");
        emailField.setMaxWidth(Double.MAX_VALUE);

        Label resultLabel = new Label();
        resultLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12;");
        resultLabel.setWrapText(true);
        resultLabel.setVisible(false);
        resultLabel.setManaged(false);

        Button rechercherBtn = new Button("Rechercher");
        rechercherBtn.setStyle(Theme.BTN_PRIMARY);
        rechercherBtn.setOnAction(e -> {
            String email = emailField.getText().trim();
            if (email.isEmpty()) return;
            Utilisateur u = authController.getByEmail(email);
            if (u == null) {
                resultLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12; -fx-text-fill: #C62828;");
                resultLabel.setText("Aucun compte trouvé pour cette adresse.");
            } else {
                resultLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12; -fx-text-fill: #388E3C;");
                resultLabel.setText("Votre mot de passe est : " + u.getMotDePasse());
            }
            resultLabel.setVisible(true);
            resultLabel.setManaged(true);
        });

        VBox content = new VBox(12, hint, emailField, rechercherBtn, resultLabel);
        content.setPadding(new Insets(16, 20, 4, 20));
        dlg.getDialogPane().setContent(content);
        dlg.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dlg.showAndWait();
    }

    private void openInscription(Stage owner) {
        Stage inscriptionStage = new Stage();
        inscriptionStage.initOwner(owner);
        inscriptionStage.initModality(Modality.APPLICATION_MODAL);
        inscriptionStage.setTitle("Créer un compte — GestionHandicap");
        inscriptionStage.setResizable(false);
        inscriptionStage.setScene(new InscriptionView().createScene(inscriptionStage));
        inscriptionStage.showAndWait();
    }

    private void handleLogin(String email, String password,
                             VBox errorBox, Label errorText,
                             Stage stage) {
        hideError(errorBox);

        if (email.isBlank() || password.isBlank()) {
            showError(errorBox, errorText, "Veuillez remplir tous les champs.");
            return;
        }

        if (authController.login(email, password)) {
            navigateAfterLogin(Session.getUtilisateur(), stage);
        } else {
            showError(errorBox, errorText, "Identifiants incorrects. Vérifiez votre e-mail et mot de passe.");
        }
    }

    private void showError(VBox errorBox, Label errorText, String message) {
        errorText.setText(message);
        errorBox.setVisible(true);
        errorBox.setManaged(true);
    }

    private void hideError(VBox errorBox) {
        errorBox.setVisible(false);
        errorBox.setManaged(false);
    }

    private void navigateAfterLogin(Utilisateur user, Stage stage) {
        try {
            MainView mainView = new MainView();
            stage.setScene(mainView.createScene(stage));
            stage.setTitle("GestionHandicap — " + user.getNomComplet());
            stage.setMinWidth(860);
            stage.setMinHeight(560);
            Platform.runLater(() -> stage.setMaximized(true));
        } catch (Exception ex) {
            ex.printStackTrace();
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setTitle("Erreur de chargement");
            err.setHeaderText("Impossible d'ouvrir l'interface principale.");
            err.setContentText(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            err.initOwner(stage);
            err.showAndWait();
        }
    }
}
