package com.andreas.main.app;

/**
 * An extension to the <code>javafx.scene.control.Control</code> class.
 * @author Andreas Gerasimow.
 * @version 1.0.
 */
public abstract class AppController {

    private AppScene scene;

    /**
     * This method is the initializing method of this class.
     */
    public abstract void init();

    // GETTERS AND SETTERS

    public AppScene getScene() {
        return scene;
    }

    public void setScene(AppScene scene) {
        this.scene = scene;
    }
}
