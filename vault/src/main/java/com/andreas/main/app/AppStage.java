package com.andreas.main.app;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.function.Consumer;

import com.andreas.main.App;
import com.andreas.main.FileUtils;
import com.andreas.main.scenes.loadingScene.LoadingScene;

import org.jdom2.Element;

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
    private List<ObservableMap<KeyCombination, Runnable>> accelerators;

    private boolean loading;

    /**
     * @param app The <code>javafx.application.Application</code> of this stage.
     * @param fxmlPath The path where the .fxml file of this stage is located.
     */
    public AppStage(Application app) {
        this.app = app;

        root = new StackPane();
        accelerators = new ArrayList<>();

        scene = new Scene(root);

        Element root = FileUtils.readXmlFile("data/data.xml");
        applyStylesheet(root.getChildText("theme"));

        super.setScene(scene);

        getIcons().add(new Image("ce-logo_alpha.png"));

        loading = false;
    }

    /**
     * This method replaces the uppermost scene.
     * @param scene The scene you want the old scene to replace with.
     */
    public void setScene(AppScene scene) {
        popScene();
        pushScene(scene);
    }

    /**
     * This method stacks another scene on top of the current scene.
     * @param scene The scene to stack.
     */
    public void pushScene(AppScene scene) {
        if (loading) 
            return;
        if (scene instanceof LoadingScene) 
            loading = true;

        root.getChildren().add(scene.getRoot());
        setTitle(scene.getTitle());
        scene.setStage(this);
        scene.init();

        accelerators.add(scene.getAccelerators());
        setAccelerators();
    }

    /**
     * This method removes the uppermost scene of that stage.
     */
    public void popScene() {
        loading = false;
        if (root.getChildren().size() > 1) {
            root.getChildren().remove(root.getChildren().size() - 1);
            accelerators.remove(accelerators.size() - 1);
            setAccelerators();
        }
    }

    /**
     * This method sets accelerators for the uppermost stage.
     */
    public void setAccelerators() {
        try {
            getScene().getAccelerators().clear();
        } catch (ConcurrentModificationException e) { }
        getScene().getAccelerators().putAll(accelerators.get(accelerators.size() - 1));
    }

    /**
     * This method applies a loading scene to the stage.
     * @param action What should be loaded.
     * @return Returns the created loading scene.
     */
    public LoadingScene applyLoadingScene(Consumer<LoadingScene> action) {
        LoadingScene loadingScene = new LoadingScene(app, action);
        pushScene(loadingScene);
        return loadingScene;
    }

    /**
     * This method applies a stylesheet to this stage. Stylesheets must be inside the data/themes folder.
     * @param name Stylesheet name.
     */
    public void applyStylesheet(String name) {
        scene.getStylesheets().clear();
        scene.getStylesheets().add("stylesheets/shapes.css");
        scene.getStylesheets().add("file:///" + Paths.get("data/themes/" + name).toAbsolutePath().toString().replace("\\", "/"));
    }

    /**
     * This method should be called instead of <code>show()</code>.
     */
    public void start() {
        show();
        App.stages.add(this);
    }

    /**
     * This method should be called instead of <code>close()</code>.
     */
    public void stop() {
        close();
        App.stages.add(this);
    }

    // GETTERS AND SETTERS

    public Application getApp() {
        return app;
    }

    public StackPane getRoot() {
        return root;
    }
}