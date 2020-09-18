package com.andreas.main.app;

import javafx.scene.control.Control;

/**
 * An extension to the <code>javafx.scene.control.Control</code> class.
 * @author Andreas Gerasimow.
 * @version 1.0.
 */
public abstract class AppController extends Control {
    
    /**
     * The corresponding <code>AppStage</code>.
     */
    protected AppStage stage;

    /**
     * This method sets the corresponding <code>AppStage</code>.
     * @param stage
     */
    public void setStage(AppStage stage) {
        this.stage = stage;
    }

    /**
     * This method is the initializing method of this class.
     */
    public abstract void init();
}
