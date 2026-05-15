package com.gestionhandicap.view;

import com.gestionhandicap.controller.AuthController;
import com.gestionhandicap.model.Utilisateur;
import com.gestionhandicap.util.Session;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class GestionComptesView {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final AuthController auth = new AuthController();
    private final ObservableList<Utilisateur> items = FXCollections.observableArrayList();

    public Node buildView(Stage stage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(36, 50, 36, 50));
        root.setStyle("-fx-background-color: white;");
        VBox.setVgrow(root, Priority.ALWAYS);

        Label title = new Label("Gestion des comptes");
        title.setStyle(Theme.TITLE_STYLE);

        Label subtitle = new Label("Consultez, modifiez ou supprimez les comptes utilisateurs.");
        subtitle.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 13; -fx-text-fill: #5A6578;");

        VBox tableSection = buildTable(stage);
        VBox.setVgrow(tableSection, Priority.ALWAYS);

        root.getChildren().addAll(title, subtitle, tableSection);
        loadData();
        return root;
    }

    // ── Table ─────────────────────────────────────────────────────────────

    private VBox buildTable(Stage stage) {
        VBox section = new VBox(10);
        VBox.setVgrow(section, Priority.ALWAYS);

        TableView<Utilisateur> table = new TableView<>(items);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);
        table.setPlaceholder(new Label("Aucun utilisateur."));

        TableColumn<Utilisateur, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getId()));
        idCol.setMaxWidth(55); idCol.setMinWidth(55);

        TableColumn<Utilisateur, String> nomCol = new TableColumn<>("Nom complet");
        nomCol.setCellValueFactory(c -> new SimpleStringProperty(
            c.getValue().getPrenom() + " " + c.getValue().getNom()
        ));
        nomCol.setMinWidth(160);

        TableColumn<Utilisateur, String> emailCol = new TableColumn<>("E-mail");
        emailCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEmail()));
        emailCol.setMinWidth(190);

        TableColumn<Utilisateur, String> roleCol = new TableColumn<>("Rôle");
        roleCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getRole()));
        roleCol.setMinWidth(110);
        roleCol.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String role, boolean empty) {
                super.updateItem(role, empty);
                if (empty || role == null) { setGraphic(null); return; }
                Label badge = new Label(role);
                badge.setStyle(
                    "-fx-background-color: " + ("ADMIN".equals(role) ? "#1976D2" : "#388E3C") + ";" +
                    "-fx-text-fill: white; -fx-padding: 3 8 3 8; -fx-background-radius: 4;" +
                    "-fx-font-family: 'Segoe UI'; -fx-font-size: 11; -fx-font-weight: bold;"
                );
                setGraphic(badge); setText(null);
            }
        });

        TableColumn<Utilisateur, String> dateCol = new TableColumn<>("Créé le");
        dateCol.setCellValueFactory(c -> {
            var d = c.getValue().getDateCreation();
            return new SimpleStringProperty(d != null ? d.format(DATE_FMT) : "—");
        });
        dateCol.setMinWidth(100);

        TableColumn<Utilisateur, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setMinWidth(160);
        actionsCol.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Void v, boolean empty) {
                super.updateItem(v, empty);
                if (empty || getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null); return;
                }
                Utilisateur u = getTableView().getItems().get(getIndex());
                boolean isSelf = Session.getUtilisateur() != null
                        && u.getId() == Session.getUtilisateur().getId();

                Button modifierBtn  = new Button("Modifier");   modifierBtn.setStyle(Theme.BTN_SECONDARY);
                Button supprimerBtn = new Button("Supprimer");  supprimerBtn.setStyle(Theme.BTN_DANGER);

                modifierBtn.setOnAction(e  -> openEditDialog(u, stage));
                supprimerBtn.setOnAction(e -> confirmSupprimer(u, stage));
                supprimerBtn.setDisable(isSelf); // can't delete own account

                HBox box = new HBox(6, modifierBtn, supprimerBtn);
                box.setAlignment(Pos.CENTER_LEFT);
                setGraphic(box);
            }
        });

        table.getColumns().addAll(idCol, nomCol, emailCol, roleCol, dateCol, actionsCol);
        section.getChildren().add(table);
        return section;
    }

    // ── Edit dialog ───────────────────────────────────────────────────────

    private void openEditDialog(Utilisateur u, Stage stage) {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Modifier le compte — " + u.getPrenom() + " " + u.getNom());
        dlg.initOwner(stage);
        dlg.getDialogPane().setPrefWidth(400);

        TextField     nomField    = tf(u.getNom());
        TextField     prenomField = tf(u.getPrenom());
        TextField     emailField  = tf(u.getEmail());
        PasswordField passField   = new PasswordField();
        passField.setPromptText("Laisser vide pour conserver");
        passField.setMaxWidth(Double.MAX_VALUE);

        Label errLbl = new Label();
        errLbl.setStyle("-fx-text-fill: #C62828; -fx-font-size: 12;");
        errLbl.setVisible(false); errLbl.setManaged(false);

        GridPane grid = new GridPane();
        grid.setHgap(12); grid.setVgap(10);
        grid.setPadding(new Insets(16, 20, 4, 20));
        ColumnConstraints lc = new ColumnConstraints(105);
        ColumnConstraints fc = new ColumnConstraints();
        fc.setHgrow(Priority.ALWAYS); fc.setFillWidth(true);
        grid.getColumnConstraints().addAll(lc, fc);
        grid.add(fl("Nom :"),           0, 0); grid.add(nomField,    1, 0);
        grid.add(fl("Prénom :"),        0, 1); grid.add(prenomField, 1, 1);
        grid.add(fl("E-mail :"),        0, 2); grid.add(emailField,  1, 2);
        grid.add(fl("Mot de passe :"),  0, 3); grid.add(passField,   1, 3);
        grid.add(errLbl, 1, 4);

        dlg.getDialogPane().setContent(grid);

        ButtonType saveType   = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelType = new ButtonType("Annuler",     ButtonBar.ButtonData.CANCEL_CLOSE);
        dlg.getDialogPane().getButtonTypes().addAll(saveType, cancelType);

        Node saveNode = dlg.getDialogPane().lookupButton(saveType);
        saveNode.addEventFilter(ActionEvent.ACTION, ae -> {
            if (nomField.getText().isBlank() || prenomField.getText().isBlank() || emailField.getText().isBlank()) {
                errLbl.setText("Nom, prénom et e-mail sont obligatoires.");
                errLbl.setVisible(true); errLbl.setManaged(true);
                ae.consume();
            }
        });

        dlg.setResultConverter(bt -> bt);
        Optional<ButtonType> result = dlg.showAndWait();
        result.ifPresent(bt -> {
            if (bt == saveType) {
                String newPass = passField.getText().isEmpty() ? u.getMotDePasse() : passField.getText();
                auth.modifierProfil(u.getId(),
                    nomField.getText().trim(),
                    prenomField.getText().trim(),
                    emailField.getText().trim(),
                    newPass
                );
                loadData();
            }
        });
    }

    private void confirmSupprimer(Utilisateur u, Stage stage) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
            "Supprimer le compte de " + u.getPrenom() + " " + u.getNom() + " ?",
            ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);
        confirm.initOwner(stage);
        confirm.showAndWait()
            .filter(r -> r == ButtonType.YES)
            .ifPresent(r -> { auth.supprimerUtilisateur(u.getId()); loadData(); });
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private void loadData() {
        items.setAll(auth.getAllUtilisateurs());
    }

    private static TextField tf(String v) {
        TextField tf = new TextField(v != null ? v : "");
        tf.setMaxWidth(Double.MAX_VALUE);
        return tf;
    }

    private static Label fl(String text) {
        Label l = new Label(text); l.setStyle(Theme.LABEL_STYLE); return l;
    }
}
