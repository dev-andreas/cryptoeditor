package com.andreas.main.scenes.loadingScene;

import com.andreas.main.app.AppController;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

public class LoadingController extends AppController{

    @FXML
    public ProgressIndicator indicator;

    @FXML
    public Label text;

    @Override
    public void init() {
    
    }
}
