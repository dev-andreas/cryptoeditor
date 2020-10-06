package com.andreas.main.app.imageTab;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.andreas.main.FileUtils;
import com.andreas.main.app.AppController;
import com.andreas.main.save.Register;
import com.andreas.main.stages.StageUtils;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageTabController extends AppController {

    @FXML public Label sizeIndicator;
    @FXML public Slider sizeSlider;
    @FXML public ImageView image;

    private double imageWidth, imageHeight;

    private Register register;

    @Override
    public void init() {

        image.setImage(loadImage());

        imageWidth = image.getImage().getWidth();
        imageHeight = image.getImage().getHeight();

        setSizeIndicator(sizeSlider.getValue());
    }

    @FXML
    private void scaled() {
        setSizeIndicator(sizeSlider.getValue());
        image.setFitWidth((int)(imageWidth * sizeSlider.getValue()));
        image.setFitHeight((int)(imageHeight * sizeSlider.getValue()));
    }
    
    
    private void setSizeIndicator(double scale) {
        sizeIndicator.setText("Size: " + Math.round(scale * 100) + "%");
    }

    public Image loadImage() {
        FileUtils.createDirectories(StageUtils.SAVES_PATH + "temp");
        Path path = Paths.get(StageUtils.SAVES_PATH + "temp/" + System.nanoTime() + ".tmp");
        FileUtils.createBinaryFile(path.toString(), register.getContent());
        Image image = new Image("file:///" + path.toAbsolutePath().toString());
        FileUtils.deleteDirectory(path.getParent().toString());

        return image;
    }
    

    // GETTERS AND SETTERS

    public Register getRegister() {
        return register;
    }

    public void setRegister(Register register) {
        this.register = register;
    }
}
