package com.andreas.main.app.mediaTab;

import com.andreas.main.app.AppController;
import com.andreas.main.stages.StageUtils;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

public class MediaTabController extends AppController {

    @FXML
    public MediaView video;
    @FXML
    public Button play;
    @FXML
    public Button replay;
    @FXML
    public Slider time;
    @FXML
    public Slider volume;
    @FXML
    public BorderPane tab;
    @FXML
    public Label playIcon;
    @FXML
    public Label timeIndex;
    @FXML
    public Label volumeIcon;
    @FXML
    public StackPane view;

    private Media media;
    private MediaPlayer mediaPlayer;

    private boolean playing;

    @Override
    public void init() {
        mediaPlayer = new MediaPlayer(media);
        video.setMediaPlayer(mediaPlayer);

        mediaPlayer.setOnReady(new Runnable() {

            @Override
            public void run() {
                time.setMax(media.getDuration().toSeconds());
                timeIndex.setText(StageUtils.secondsToNiceLayout(0) + "/"
                        + StageUtils.secondsToNiceLayout((int) media.getDuration().toSeconds()));
            }
        });

        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {

            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                time.setValue(newValue.toSeconds());
            }
        });

        InvalidationListener resizeMediaView = this::resizeVideo;
        view.heightProperty().addListener(resizeMediaView);
        view.widthProperty().addListener(resizeMediaView);

        playing = false;
    }

    private void resizeVideo(Observable observable) {
        video.setFitWidth(view.getWidth());
        video.setFitHeight(view.getHeight());
    
       
        Bounds actualVideoSize = video.getLayoutBounds();
        video.setX((video.getFitWidth() - actualVideoSize.getWidth()) / 2);
        video.setY((video.getFitHeight() - actualVideoSize.getHeight()) / 2);
    }

    @FXML
    private void timeChanged() {
        mediaPlayer.seek(Duration.seconds(time.getValue()));
        timeIndex.setText(StageUtils.secondsToNiceLayout((int) time.getValue()) + "/"
                + StageUtils.secondsToNiceLayout((int) media.getDuration().toSeconds()));
    }

    @FXML
    private void volumeChanged() {
        double value = volume.getValue();
        mediaPlayer.setVolume(value);
        if (value == 0)
            volumeIcon.setStyle("-fx-shape: -fx-volume-off-shape;");
        else if (value < 0.33)
            volumeIcon.setStyle("-fx-shape: -fx-volume-quiet-shape;");
        else if (value < 0.66)
            volumeIcon.setStyle("-fx-shape: -fx-volume-middle-shape;");
        else
            volumeIcon.setStyle("-fx-shape: -fx-volume-loud-shape;");
    }

    @FXML
    private void playPressed() {

        if (!playing) {
            mediaPlayer.play();
            playIcon.setStyle("-fx-shape: -fx-pause-shape;");
            playing = true;
        } else {
            mediaPlayer.pause();
            playIcon.setStyle("-fx-shape: -fx-play-shape;");
            playing = false;
        }
    }

    @FXML
    private void replayPressed() {
        mediaPlayer.seek(Duration.seconds(0));
        time.setValue(0);

        timeIndex.setText(StageUtils.secondsToNiceLayout(0) + "/"
                + StageUtils.secondsToNiceLayout((int) media.getDuration().toSeconds()));
    }

    // GETTERS AND SETTERS

    public void setMedia(Media media) {
        this.media = media;
    }
}
