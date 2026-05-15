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
        stage.setResizable(true);
        stage.setMinWidth(700);
        stage.setMinHeight(460);
        stage.setMaximized(true);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
