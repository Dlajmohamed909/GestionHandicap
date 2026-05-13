package com.gestionhandicap;

import com.gestionhandicap.view.LoginView;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        LoginView loginView = new LoginView();
        stage.setScene(loginView.createScene(stage));
        stage.setTitle("GestionHandicap — Connexion");
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
