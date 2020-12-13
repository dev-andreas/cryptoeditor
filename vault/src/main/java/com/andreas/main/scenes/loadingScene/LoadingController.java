package com.andreas.main.scenes.loadingScene;

import com.andreas.main.app.AppController;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

public class LoadingController extends AppController{

    @FXML public ProgressIndicator indicator;

    @FXML public Label text;

    @FXML public BorderPane pane;

    @Override
    public void init() {
    }

    public void fade(double opacity, EventHandler<ActionEvent> e) {
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(pane.opacityProperty(), 0.75, Interpolator.EASE_BOTH);
        KeyFrame kf = new KeyFrame(Duration.millis(250), kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(e);
        timeline.play();
    }
}
