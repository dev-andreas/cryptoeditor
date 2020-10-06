package com.andreas.main.app;

import java.util.ConcurrentModificationException;
import java.util.Stack;
import java.util.function.Consumer;

import com.andreas.main.scenes.loadingScene.LoadingScene;

import javafx.application.Application;
import javafx.collections.ObservableMap;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * An extension to the <code>javafx.stage.Stage</code> class.
 * @author Andreas Gerasimow.
 * @version 1.0.
 */
public abstract class AppStage extends Stage {
    
    private Application app;

    private Scene scene;
    private StackPane root;
    private Stack<ObservableMap<KeyCombination, Runnable>> accelerators;

    private boolean loading;

    /**
     * @param app The <code>javafx.application.Application</code> of this stage.
     * @param fxmlPath The path where the .fxml file of this stage is located.
     */
    public AppStage(Application app) {
        this.app = app;

        root = new StackPane();
        accelerators = new Stack<>();

        scene = new Scene(root);
        scene.getStylesheets().add("stylesheets/stylesheet.css");
        super.setScene(scene);

        getIcons().add(new Image("lockb0.5-256px.png"));

        loading = false;
    }

    public void setScene(AppScene scene) {
        popScene();
        pushScene(scene);
    }

    public void pushScene(AppScene scene) {
        if (loading) 
            return;
        if (scene instanceof LoadingScene) 
            loading = true;

        root.getChildren().add(scene.getRoot());
        setTitle(scene.getTitle());
        scene.setStage(this);
        scene.init();

        accelerators.push(scene.getAccelerators());
        setAccelerators();
    }

    public void popScene() {
        loading = false;
        if (root.getChildren().size() > 1) {
            root.getChildren().remove(root.getChildren().size() - 1);
            accelerators.pop();
            setAccelerators();
        }
    }

    public void setAccelerators() {
        try {
            getScene().getAccelerators().clear();
        } catch (ConcurrentModificationException e) { }
        getScene().getAccelerators().putAll(accelerators.peek());
    }

    public LoadingScene applyLoadingScene(Consumer<LoadingScene> action) {
        LoadingScene loadingScene = new LoadingScene(app, action);
        pushScene(loadingScene);
        return loadingScene;
    }

    // GETTERS AND SETTERS

    public Application getApp() {
        return app;
    }

    public StackPane getRoot() {
        return root;
    }
}