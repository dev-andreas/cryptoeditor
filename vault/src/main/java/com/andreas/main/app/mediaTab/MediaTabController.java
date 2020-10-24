package com.andreas.main.app.mediaTab;

import java.net.MalformedURLException;
import java.nio.file.Path;

import com.andreas.main.app.AppController;
import com.andreas.main.save.Register;
import com.andreas.main.stages.StageUtils;
import com.andreas.main.temp.TempUtils;

import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

public class MediaTabController extends AppController {

    @FXML
    public MediaView video;
    @FXML
    public ToggleButton play;
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

    private Media media;
    private MediaPlayer mediaPlayer;

    private Register register;

    @Override
    public void init() {
        Media media = loadMedia();
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

        

        DoubleProperty widthProp = video.fitWidthProperty();
        DoubleProperty heightProp = video.fitHeightProperty();

        widthProp.bind(tab.widthProperty());
        heightProp.bind(tab.heightProperty());
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
        if (play.isSelected()) {
            mediaPlayer.play();
            playIcon.setStyle("-fx-shape: -fx-play-shape;");
        } else {
            mediaPlayer.pause();
            playIcon.setStyle("-fx-shape: -fx-pause-shape;");
            mediaPlayer.dispose();
        }
    }

    @FXML
    private void replayPressed() {
        mediaPlayer.seek(Duration.seconds(0));
        time.setValue(0);

        timeIndex.setText(StageUtils.secondsToNiceLayout(0) + "/"
                + StageUtils.secondsToNiceLayout((int) media.getDuration().toSeconds()));
    }

    private Media loadMedia() {
        Path path = TempUtils.createTempFile(register.getContent());
        Media media = null;
        try {
            media = new Media(path.toAbsolutePath().toUri().toURL().toExternalForm());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return media;
    }

    // GETTERS AND SETTERS

    public Register getRegister() {
        return register;
    }

    public void setRegister(Register register) {
        this.register = register;
    }
}
