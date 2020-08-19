package com.andreas.main.app;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import com.andreas.main.App;

public abstract class AppStage extends Stage {
    
    private App app;
    private FXMLLoader loader;

    private AppController controller;

    public AppStage(App app, String fxmlPath) {
        this.app = app;

        try {
            loader = new FXMLLoader(app.getClass().getResource(fxmlPath));
            Parent root = loader.load();

            controller = loader.getController();
    
            Scene scene = new Scene(root);
            scene.getStylesheets().add("stylesheets/stylesheet.css");
            setScene(scene);
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        controller.setStage(this);

        getIcons().add(new Image("lockb0.5-256px.png"));
    }

    public void init() {
        controller.init();
    }

    public AppController getController() {
        return controller;
    }

    public App getApp() {
        return app;
    }
}