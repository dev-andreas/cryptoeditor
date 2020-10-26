package com.andreas.main.app;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class AppScene extends Scene {

    private Application app;
    private FXMLLoader loader;
    private AppController controller;
    private String title;

    private AppStage stage;

    public AppScene(Application app, String fxmlPath, String title) {
        super(new Parent() {});

        this.app = app;
        this.title = title;

        try {
            loader = new FXMLLoader(app.getClass().getResource(fxmlPath));
            Parent parent = loader.load();
            controller = loader.getController();
            setRoot(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        if (controller == null)
            return;
        controller.setScene(this);
        controller.init();
    }

    // GETTERS AND SETTERS

    public AppController getController() {
        return controller;
    }

    public Application getApp() {
        return app;
    }

    public String getTitle() {
        return title;
    }

    public AppStage getStage() {
        return stage;
    }

    public void setStage(AppStage stage) {
        this.stage = stage;
    }
}
