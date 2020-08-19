package com.andreas.main.app;

import javafx.scene.control.Control;

public abstract class AppController extends Control {
    
    protected AppStage stage;

    public void setStage(AppStage stage) {
        this.stage = stage;
    }

    public abstract void init();
}
