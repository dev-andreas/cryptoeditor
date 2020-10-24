package com.andreas.main.app.imageTab;

import java.net.MalformedURLException;
import java.nio.file.Path;

import com.andreas.main.app.AppController;
import com.andreas.main.save.Register;
import com.andreas.main.temp.TempUtils;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageTabController extends AppController {

    @FXML
    public Label sizeIndicator;
    @FXML
    public Slider sizeSlider;
    @FXML
    public ImageView image;

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
        image.setFitWidth((int) (imageWidth * sizeSlider.getValue()));
        image.setFitHeight((int) (imageHeight * sizeSlider.getValue()));
    }

    private void setSizeIndicator(double scale) {
        sizeIndicator.setText("Size: " + Math.round(scale * 100) + "%");
    }

    public Image loadImage() {
        Path path = TempUtils.createTempFile(register.getContent());
        Image image = null;
        try {
            image = new Image(path.toAbsolutePath().toUri().toURL().toExternalForm());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

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
