package com.andreas.main.app;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * An extension to the <code>javafx.stage.Stage</code> class.
 * @author Andreas Gerasimow.
 * @version 1.0.
 */
public abstract class AppStage extends Stage {
    
    private Application app;
    private FXMLLoader loader;

    private AppController controller;

    /**
     * @param app The <code>javafx.application.Application</code> of this stage.
     * @param fxmlPath The path where the .fxml file of this stage is located.
     */
    public AppStage(Application app, String fxmlPath) {
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

    /**
     * This method must be called inside every constructor of a class that inherits <code>AppStage</code>.
     */
    public void init() {
        controller.init();
    }

    // GETTERS AND SETTERS

    /**
     * @return This method returns the corresponding <code>AppController</code> of this stage.
     */
    public AppController getController() {
        return controller;
    }

    /**
     * @return This method returns the corresponding <code>javafx.application.Application</code> of this stage.
     */
    public Application getApp() {
        return app;
    }
}